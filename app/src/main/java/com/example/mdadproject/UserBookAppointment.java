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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserBookAppointment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ProgressDialog pDialog;
    private DrawerLayout drawer;
    private TextView txtGreeting;
    private TextView textView5;
    private CalendarView calendarView;
    private TextView textView6;
    private Spinner spinner;
    private FloatingActionButton button;
    private Spinner spinner2;

    String username;

    public static String url_create_appointment = Login.ipBaseAddress + "/create_appointmentJson.php";
    public static String url_get_PetList = Login.ipBaseAddress + "/get_all_petsJson.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_DATE = "date";
    private static final String TAG_STARTTIME = "starttime";
    private static final String TAG_ENDTIME = "endtime";
    private static final String TAG_STATUS = "status";
    private static final String TAG_PID = "pid";
    private static final String TAG_USERNAME = "username";

    List<Pet> petList = new ArrayList<>();
    private static final String TAG_PETS = "pets";
    private static final String TAG_NAME = "name";
    private static final String TAG_SEX = "sex";
    private static final String TAG_BREED = "breed";
    private static final String TAG_AGE = "age";
    private static final String TAG_DATEOFADOPTION = "dateofadoption";
    private static final String TAG_HEIGHT = "height";
    private static final String TAG_WEIGHT = "weight";
    private static final String TAG_PET = "pet";
    private static final String TAG_IMAGEPATH = "image_path";
    private static final String TAG_IMAGENAME = "image_name";

    JSONArray pets = null;
    String date, starttime, endttime, status, pid;
    boolean disabled;

    private static String ownFirstName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_appointment);
        setTitle("");
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("We're giving it whatevfur we'ge got..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        Intent intent = getIntent();
        username = intent.getStringExtra(TAG_USERNAME);
        Log.i("oops", username);

        JSONObject dataJson = new JSONObject();
        try {
            dataJson.put("username", username);
        } catch (JSONException e) {

        }
        postData(url_get_PetList, dataJson, 2);

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
        ;
        txtGreeting.setText("Hello, " + username + "!");

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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        if (username.equals("effertz")){
            nav_Menu.findItem(R.id.nav_qr_scanner).setVisible(true);
        }
        else {
            nav_Menu.findItem(R.id.nav_qr_scanner).setVisible(false);
        }

//        if (spinner2.getCount() == 0) {
//            disabled = true;
//            button.setBackgroundTintList(getResources().getColorStateList(R.color.disabled));
//        }
//        else {
//            disabled = false;
//            button.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
//        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int d = dayOfMonth;
                int m = month + 1;
                int y = year;
                date = (d + "-" + m + "-" + y);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Toast.makeText(UserBookAppointment.this, "You have no pets yet! Go to the pet(s) page to add one!", Toast.LENGTH_SHORT).show();

                    if (TextUtils.isEmpty(date)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        String selectedDate = sdf.format(new Date(calendarView.getDate()));
                        date = selectedDate;
                    }
                    starttime = spinner.getSelectedItem().toString();
                    String start1 = starttime.replaceAll("[^\\d]", "");
                    int start2 = Integer.parseInt(start1) + 100;
                    endttime = Integer.toString(start2);
                    status = "pending";
                    getSelectedPet();

                    Log.i("yeet", date);
                    Log.i("yeet", starttime);
                    Log.i("yeet", endttime);
                    Log.i("yeet", status);
                    Log.i("yeet", pid);

                    pDialog = new ProgressDialog(UserBookAppointment.this);
                    pDialog.setMessage("Creating your a-pet-ment..");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();

                    JSONObject dataJson = new JSONObject();
                    try {
                        dataJson.put(TAG_DATE, date);
                        dataJson.put(TAG_STARTTIME, starttime);
                        dataJson.put(TAG_ENDTIME, endttime);
                        dataJson.put(TAG_STATUS, status);
                        dataJson.put(TAG_PID, pid);

                    } catch (JSONException e) {

                    }
                    postData(url_create_appointment, dataJson, 1);

            }
        });
    }

    public void getSelectedPet() {
        Pet pet = (Pet) spinner2.getSelectedItem();
        getPetPid(pet);
    }

    private void getPetPid(Pet pet) {
        pid = pet.getPid();
    }

    public void postData(String url, final JSONObject json, final int option) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest json_obj_req = new JsonObjectRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                switch (option) {
                    case 1:
                        checkResponseCreate_Appointment(response);
                        break;
                    case 2:
                        checkResponse(response, json);
                        break;
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

    public void checkResponseCreate_Appointment(JSONObject response) {
        Log.i("----Response", response + " ");
        try {
            if (response.getInt(TAG_SUCCESS) == 1) {

                finish();
//                Intent i = new Intent(this, AllProductsActivity.class);
//                startActivity(i);

                // dismiss the dialog once product uupdated
                pDialog.dismiss();

            } else {
                // product with pid not found
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private void checkResponse(JSONObject response, JSONObject creds) {
        try {
            if (response.getInt(TAG_SUCCESS) == 1) {

                pets = response.getJSONArray(TAG_PETS);

                // looping through All Products
                for (int i = 0; i < pets.length(); i++) {
                    JSONObject c = pets.getJSONObject(i);

                    Pet p = new Pet();
                    String pid = c.getString(TAG_PID);
                    String name = c.getString(TAG_NAME).toLowerCase();
                    String pet = c.getString(TAG_PET).toLowerCase();
                    String sex = c.getString(TAG_SEX).toLowerCase();
                    String breed = c.getString(TAG_BREED).toLowerCase();
                    String age = c.getString(TAG_AGE).toLowerCase();
                    String dateofadoption = c.getString(TAG_DATEOFADOPTION).toLowerCase();
                    String height = c.getString(TAG_HEIGHT).toLowerCase();
                    String weight = c.getString(TAG_WEIGHT).toLowerCase();
                    String imagepath = c.getString(TAG_IMAGEPATH);
                    String imagename = c.getString(TAG_IMAGENAME);

                    p = new Pet(pid, name, pet, sex, breed, age, dateofadoption, height, weight, username, imagepath, imagename);
                    petList.add(p);
                }
                ArrayAdapter<Pet> adapter = new ArrayAdapter<Pet>(this, android.R.layout.simple_spinner_item, petList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter);
                pDialog.dismiss();

            } else {
                pDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.nav_qr_scanner:
                intent = new Intent(getApplicationContext(), ZxingScannerActivity.class);
                intent.putExtra(TAG_USERNAME, username);
                break;
            case R.id.nav_profile:
                intent = new Intent(getApplicationContext(), RegisterDetails.class);
                intent.putExtra(TAG_USERNAME, username);
                break;
            case R.id.nav_pet:
                intent = new Intent(getApplicationContext(), UserPets.class);
                intent.putExtra(TAG_USERNAME, username);
                break;
            case R.id.nav_qr:
                intent = new Intent(getApplicationContext(), UserQrCode.class);
                intent.putExtra(TAG_USERNAME, username);
                break;
        }
        startActivityForResult(intent, 100);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
