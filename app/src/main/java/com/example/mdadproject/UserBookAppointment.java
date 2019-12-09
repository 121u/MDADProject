package com.example.mdadproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserBookAppointment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ProgressDialog pDialog;
    private DrawerLayout drawer;
    private TextView txtGreeting;
    private TextView txtUserN;
    private TextView textView5;
    private CalendarView calendarView;
    private TextView textView6;
    private Spinner spinner;
    private FloatingActionButton button;
    private Spinner spinner2;

    JSONObject json = null;
    String username;

    public static String url_create_appointment = Login.ipBaseAddress + "/create_appointmentJson.php";
    public static String url_get_PetList = Login.ipBaseAddress + "/get_petListJson.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_APPOINTMENT = "appointment";
    private static final String TAG_ID = "id";
    private static final String TAG_DATE = "date";
    private static final String TAG_STARTTIME = "starttime";
    private static final String TAG_ENDTIME = "endtime";
    private static final String TAG_STATUS = "status";
    private static final String TAG_PID = "pid";
    private static final String TAG_USERNAME = "username";

    ArrayList<HashMap<String, String>> petsList;
    private static final String TAG_PETS = "pet";
    private static final String TAG_NAME = "name";
    JSONArray pets = null;
    String[] pets2 = new String[]{};

    String date, starttime, endttime, status, pid;

    private static String ownNric = "";
    private static String ownFirstName = "";
    private static String ownLastName = "";
    private static String ownTel = "";
    private static String ownEmail = "";
    private static String ownAddress = "";
    private static String ownZipcode = "";
    private static String ownUsername = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_appointment);

        setTitle("");

        Intent intent = getIntent();
        username = intent.getStringExtra(TAG_USERNAME);

        textView5 = (TextView) findViewById(R.id.textView5);
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        textView6 = (TextView) findViewById(R.id.textView6);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        button = (FloatingActionButton) findViewById(R.id.btnBook);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        txtGreeting = (TextView) header.findViewById(R.id.txtGreeting);
        txtUserN = (TextView) header.findViewById(R.id.txtUsern);
        txtGreeting.setText("Hello, " + ownFirstName + "!");
        txtUserN.setText(username);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new AppointmentFragment()).commit();
//            navigationView.setCheckedItem(R.id.nav_appointment);
        }

        String[] items = new String[]{"1100", "1200", "1300", "1400", "1500", "1600", "1700"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int d = dayOfMonth;
                date =String.valueOf(d);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                starttime = spinner.getSelectedItem().toString();
                String start1 = starttime.replaceAll("[^\\d]", "");
                int start2 = Integer.parseInt(start1) + 100;
                endttime = Integer.toString(start2);
                status = "pending";

                pDialog = new ProgressDialog(UserBookAppointment.this);
                pDialog.setMessage("Adding product ...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();

                JSONObject dataJson = new JSONObject();
                try{
                    dataJson.put(TAG_DATE, date);
                    dataJson.put(TAG_STARTTIME, starttime);
                    dataJson.put(TAG_ENDTIME, endttime);
                    dataJson.put(TAG_STATUS, status);
                    dataJson.put(TAG_PID, pid);

                }catch(JSONException e){

                }

                postData(url_create_appointment,dataJson,1 );
            }
        });

        petsList = new ArrayList<HashMap<String, String>>();

        // Loading products in Background Thread
        username = intent.getStringExtra(TAG_USERNAME);

        JSONObject dataJson = new JSONObject();
        try {
            dataJson.put("username", username);
        } catch (JSONException e) {

        }
        postData2(url_get_PetList, dataJson);
    }

    public void postData(String url, final JSONObject json, final int option){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest json_obj_req = new JsonObjectRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                switch (option){
                    case 1:
                        checkResponseCreate_Appointment(response); break;

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                String alert_message;
//                alert_message = error.toString();
//                showAlertDialogue("Error", alert_message);
            }

        });
        requestQueue.add(json_obj_req);
    }

    public void postData2(String url, final JSONObject json){
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest json_obj_req = new JsonObjectRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                checkResponse(response, json);

//                String alert_message;
//                alert_message = response.toString();

//                showAlertDialogue("Response", alert_message);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

//                String alert_message;
//                alert_message = error.toString();

//                showAlertDialogue("Error", alert_message);

            }

        });

        requestQueue.add(json_obj_req);
    }

    public void checkResponseCreate_Appointment(JSONObject response)
    {
        Log.i("----Response", response+" ");
        try {
            if(response.getInt(TAG_SUCCESS)==1){

                finish();
//                Intent i = new Intent(this, AllProductsActivity.class);
//                startActivity(i);

                // dismiss the dialog once product uupdated
                pDialog.dismiss();

            }else{
                // product with pid not found
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private void checkResponse(JSONObject response, JSONObject creds){
        try {
            if(response.getInt(TAG_SUCCESS)==1){

                // products found
                // Getting Array of Products
                pets = response.getJSONArray(TAG_PETS);

                // looping through All Products
                for (int i = 0; i < pets.length(); i++) {
                    JSONObject c = pets.getJSONObject(i);

                    // Storing each json item in variable
                    String id = c.getString(TAG_PID);
                    String name = c.getString(TAG_NAME);

                    // creating new HashMap
//                    HashMap<String, String> map = new HashMap<String, String>();
                    // adding each child node to HashMap key => value
//                    map.put(TAG_PID, id);
//                    map.put(TAG_NAME, name);
                    pets2[i] = name;
                    // adding HashList to ArrayList
//                    petsList.add(pets2);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pets2);
                spinner2.setAdapter(adapter);

//                /**
//                 * Updating parsed JSON data into ListView
//                 * */
//                ListAdapter adapter = new SimpleAdapter(
//                        AllProductsActivity.this, productsList,
//                        R.layout.list_item, new String[] { TAG_PID,
//                        TAG_NAME},
//                        new int[] { R.id.pid, R.id.name });
//                // updating listview
//                setListAdapter(adapter);

            }
            else{

            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.nav_appointment:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new AppointmentFragment()).commit();
                break;
            case R.id.nav_profile:
                intent = new Intent(getApplicationContext(), UserProfile.class);
                intent.putExtra(TAG_USERNAME, username);
                break;
            case R.id.nav_pet:
                intent = new Intent(getApplicationContext(), UserPets.class);
                intent.putExtra(TAG_USERNAME, username);
                break;
            case R.id.nav_qr:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new QRCodeFragment()).commit();
                break;
        }
        startActivityForResult(intent, 100);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
