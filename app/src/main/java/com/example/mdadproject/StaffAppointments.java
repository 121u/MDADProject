package com.example.mdadproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mdadproject.Adapters.AptListAdapter;
import com.example.mdadproject.Adapters.StaffAptListAdapter;
import com.example.mdadproject.Models.Appointment;
import com.example.mdadproject.Utils.Constants;
import com.example.mdadproject.Utils.SaveSharedPreference;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class StaffAppointments extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private TextView txtGreeting, txtChosenDate;
    private ListView listView;
    private Button btnNotifyAll;
    JSONArray appointments = null;
    ArrayList<Appointment> AppointmentList = new ArrayList();
    private ProgressDialog pDialog;
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";

    private static final String TAG_USERNAME = "username";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PID = "pid";
    public static String url_appointment = UserLogin.ipBaseAddress + "/get_date_appointmentsJson.php";
    private static final String TAG_APPOINTMENT = "appointment";
    private static final String TAG_ID = "id";
    private static final String TAG_DATE = "date";
    private static final String TAG_STARTTIME = "starttime";
    private static final String TAG_ENDTIME = "endtime";
    private static final String TAG_STATUS = "status";
    private static final String TAG_PETNAME = "petname";

    private TextInputLayout etDate;
    private TextInputEditText etDateIn;
    String username, chosenDate, newFormattedDate, startime, endtime, status, pid, petname;

    TimeZone timeZone1 = TimeZone.getTimeZone("Asia/Singapore");
    final Calendar myCalendar = Calendar.getInstance(timeZone1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_appointments);
        mRequestQue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        username = intent.getStringExtra(TAG_USERNAME);

        pDialog = new ProgressDialog(StaffAppointments.this);
        pDialog.setMessage("loading appointments ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Appointments");
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        txtGreeting = (TextView) header.findViewById(R.id.txtGreeting);
        txtGreeting.setText("Hello ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_profile).setVisible(false);
        nav_Menu.findItem(R.id.nav_qr).setVisible(false);

        txtChosenDate = (TextView) findViewById(R.id.txtChosenDate);
        etDate = (TextInputLayout) findViewById(R.id.etDate);
        etDateIn = (TextInputEditText) findViewById(R.id.etDateIn);
        listView = (ListView) findViewById(R.id.listView);
        btnNotifyAll = (Button) findViewById(R.id.btnNotifyAll);

        if (etDate.getEditText().getText().toString().trim().isEmpty()) {
            String myFormat = "yyyy-MM-dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

            sdf.setTimeZone(timeZone1);
            chosenDate = sdf.format(myCalendar.getTime());
            etDate.getEditText().setText(chosenDate);
            txtChosenDate.setText("Appointments for: " + chosenDate);
            AppointmentList.clear();
            JSONObject dataJson = new JSONObject();
            try {
                dataJson.put(TAG_DATE, chosenDate);
            } catch (JSONException e) { }
            postData(url_appointment, dataJson, 1);
        }

        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        etDateIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new DatePickerDialog(StaffAppointments.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        TimeZone timeZone1 = TimeZone.getTimeZone("Asia/Singapore");
        sdf.setTimeZone(timeZone1);
        chosenDate = sdf.format(myCalendar.getTime());
        etDate.getEditText().setText(chosenDate);
        txtChosenDate.setText("Appointments for: " + chosenDate);
        AppointmentList.clear();
        JSONObject dataJson = new JSONObject();
        try {
            dataJson.put(TAG_DATE, chosenDate);
        } catch (JSONException e) {

        }
        postData(url_appointment, dataJson, 1);
    }

    public void postData(String url, final JSONObject json, final int option) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest json_obj_req = new JsonObjectRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                switch (option) {
                    case 1:
                        checkResponse(response, json);
                        break;

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });
        requestQueue.add(json_obj_req);
    }

    private void checkResponse(JSONObject response, JSONObject creds) {
        try {
            if (response.getInt(TAG_SUCCESS) == 1) {

                appointments = response.getJSONArray(TAG_APPOINTMENT);

                // looping through All Products
                for (int i = 0; i < appointments.length(); i++) {
                    JSONObject c = appointments.getJSONObject(i);

                    Appointment a = new Appointment();
                    // Storing each json item in variable
                    String aid = c.getString(TAG_ID);
                    String date = c.getString(TAG_DATE);
                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date parsedDate = simpleDateFormat2.parse(date);
                        simpleDateFormat2 = new SimpleDateFormat("dd MMMM yyyy");
                        newFormattedDate = simpleDateFormat2.format(parsedDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                     startime = c.getString(TAG_STARTTIME);
                     endtime = c.getString(TAG_ENDTIME);
                     status = c.getString(TAG_STATUS);
                     pid = c.getString(TAG_PID);
                     username = c.getString(TAG_USERNAME);
                     petname = c.getString(TAG_PETNAME);

                    a = new Appointment(aid, newFormattedDate, startime, endtime, status, pid, username, petname);
                    AppointmentList.add(a);
                    Log.i("Notif11", AppointmentList.toString());
                }

                StaffAptListAdapter myCustomAdapter = new StaffAptListAdapter(StaffAppointments.this, AppointmentList);
                listView.setAdapter(myCustomAdapter);

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
                intent = new Intent(getApplicationContext(), StaffQrScanner.class);
                intent.putExtra(TAG_USERNAME, username);
                break;
            case R.id.nav_queue_list:
                intent = new Intent(getApplicationContext(), UserQueue.class);
                intent.putExtra(TAG_USERNAME, username);
                break;
            case R.id.nav_logout:
                Constants.IS_STAFF = false;
                intent = new Intent(getApplicationContext(), UserLogin.class);
                SaveSharedPreference.clearUserName(StaffAppointments.this);
                finish();
                break;
        }
        startActivityForResult(intent, 100);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void sendNotification(String date, String time, String petname, String username) {

        JSONObject json = new JSONObject();
        try {
//            json.put("to","/topics/"+ date + username + petname);
            json.put("condition","/topics/"+ date + username + petname);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","Appointment Reminder");
            notificationObj.put("body","You have an appointment on " + date + " at " + time + " for " + petname);

//            JSONObject extraData = new JSONObject();
//            extraData.put("brandId","puma");
//            extraData.put("category","Shoes");

            json.put("notification",notificationObj);
//            json.put("data",extraData);

            Log.i("Notif11", json.toString());
            Log.i("Notif11", notificationObj.toString());


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AIzaSyDVWUL1u4Ma98vKGNtlrO4d4_9QHlPTSq8");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }
}
