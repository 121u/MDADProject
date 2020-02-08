package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mdadproject.Adapters.AptListAdapter;
import com.example.mdadproject.Models.Appointment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PetAppointments extends AppCompatActivity {

    private ListView listView;
    private ListView listView2;
    private ProgressDialog pDialog;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PID = "pid";

    public static String url_appointment = UserLogin.ipBaseAddress + "/get_all_appointmentsJson.php";
    private static final String TAG_APPOINTMENT = "appointment";
    private static final String TAG_ID = "id";
    private static final String TAG_DATE = "date";
    private static final String TAG_STARTTIME = "starttime";
    private static final String TAG_ENDTIME = "endtime";
    private static final String TAG_STATUS = "status";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PETNAME = "petname";

    JSONObject json = null;
    String username, apt_id, pid, name, newFormattedDate;
    JSONArray appointments = null;
    ArrayList<Appointment> AppointmentList = new ArrayList();
    ArrayList<Appointment> completedList = new ArrayList();
    ArrayList<Appointment> pendingList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_appointments);

        Intent intent = getIntent();

        pid = intent.getStringExtra(TAG_PID);
        name = intent.getStringExtra("name");
        username = intent.getStringExtra("username");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Appointments for " + name);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.black),
                    PorterDuff.Mode.SRC_ATOP);
        }

        listView = (ListView) findViewById(R.id.listView);
        listView2 = (ListView) findViewById(R.id.listView2);

        JSONObject dataJson = new JSONObject();
        try {
            dataJson.put(TAG_PID, pid);
        } catch (JSONException e) {

        }

        postData(url_appointment, dataJson, 1);

        pDialog = new ProgressDialog(PetAppointments.this);
        pDialog.setMessage("loading appointments ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                apt_id = ((TextView) view.findViewById(R.id.id)).getText().toString();
                Intent in = new Intent(getApplicationContext(), UserBookAppointment.class);
                in.putExtra(TAG_USERNAME, username);
                in.putExtra(TAG_PETNAME, name);
                in.putExtra(TAG_PID, pid);
                in.putExtra("apt_id", apt_id);
                startActivity(in);
            }
        });

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void postData(String url, final JSONObject json, final int option) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest json_obj_req = new JsonObjectRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                switch (option) {
                    case 1:
                        checkResponse2(response, json);
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

    private void checkResponse2(JSONObject response, JSONObject creds) {
        try {
            if (response.getInt(TAG_SUCCESS) == 1) {

                appointments = response.getJSONArray(TAG_APPOINTMENT);

                // looping through All Products
                for (int i = 0; i < appointments.length(); i++) {
                    JSONObject c = appointments.getJSONObject(i);

                    Appointment a = new Appointment();
                    // Storing each json item in variable
                    String id = c.getString(TAG_ID);
                    apt_id = id;
                    String date = c.getString(TAG_DATE);
                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date parsedDate = simpleDateFormat2.parse(date);
                        simpleDateFormat2 = new SimpleDateFormat("dd MMMM yyyy");
                        newFormattedDate = simpleDateFormat2.format(parsedDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String startime = c.getString(TAG_STARTTIME);
                    String endtime = c.getString(TAG_ENDTIME);
                    String status = c.getString(TAG_STATUS);
                    String pid = c.getString(TAG_PID);
                    String username = c.getString(TAG_USERNAME);
                    String petname = c.getString(TAG_PETNAME);

                    a = new Appointment(id, newFormattedDate, startime, endtime, status, pid, username, petname);
                    AppointmentList.add(a);
                }

                for (Appointment a1 : AppointmentList) {
                    if (a1.getStatus().equals("completed")) {
                        completedList.add(a1);
                    } else {
                        pendingList.add(a1);
                    }
                }

                AptListAdapter myCustomAdapter = new AptListAdapter(PetAppointments.this, pendingList);
                listView.setAdapter(myCustomAdapter);

                AptListAdapter myCustomAdapter2 = new AptListAdapter(PetAppointments.this, completedList);
                listView2.setAdapter(myCustomAdapter2);
                pDialog.dismiss();
            } else {
                pDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

}
