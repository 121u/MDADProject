package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserPetDetails extends AppCompatActivity {

    private TextView textView5;
    private TextView textView;
    private EditText txtName;
    private TextView textView2;
    private Button btnSex;
    private TextView textView3;
    private EditText txtBreed;
    private TextView textView4;
    private EditText txtAge;
    private TextView textView6;
    private TextView txtAdoptionDate;
    private TextView textView7;
    private EditText txtHeight;
    private TextView textView8;
    private EditText txtWeight;
    private TextView textView9;
    private ImageButton btnImage;
    private ProgressDialog pDialog;
    private ListView listView;
    private ListView listView2;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_SEX = "sex";
    private static final String TAG_BREED = "breed";
    private static final String TAG_AGE = "age";
    private static final String TAG_DATEOFADOPTION = "dateofadoption";
    private static final String TAG_HEIGHT = "height";
    private static final String TAG_WEIGHT = "weight";
    private static final String TAG_PET = "pet";
    private static final String TAG_IMAGE = "image";

    public static String url_appointment = Login.ipBaseAddress + "/get_all_appointmentsJson.php";
    private static final String TAG_APPOINTMENT = "appointment";
    private static final String TAG_ID = "id";
    private static final String TAG_DATE = "date";
    private static final String TAG_STARTTIME = "starttime";
    private static final String TAG_ENDTIME = "endtime";
    private static final String TAG_STATUS = "status";
    private static final String TAG_USERNAME = "username";


    private static String petName = "";
    private static String petSex = "";
    private static String petBreed = "";
    private static String petAge = "";
    private static String petDate = "";
    private static String petWeight = "";
    private static String petHeight = "";
    private static String petImage = "";

    public static String url_pet = Login.ipBaseAddress + "/get_pet_detailsJson.php";
    JSONObject json = null;
    String pid;
    String newFormatttedDate;
    JSONArray appointments = null;
    ArrayList<Appointment> AppointmentList = new ArrayList();
    ArrayList<Appointment> completedList = new ArrayList();
    ArrayList<Appointment> pendingList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pet_details);

        textView5 = (TextView) findViewById(R.id.textView5);
        textView = (TextView) findViewById(R.id.textView);
        txtName = (EditText) findViewById(R.id.txtName);
        textView2 = (TextView) findViewById(R.id.textView2);
        btnSex = (Button) findViewById(R.id.btnSex);
        textView3 = (TextView) findViewById(R.id.textView3);
        txtBreed = (EditText) findViewById(R.id.txtLastName);
        textView4 = (TextView) findViewById(R.id.textView4);
        txtAge = (EditText) findViewById(R.id.txtAge);
        textView6 = (TextView) findViewById(R.id.textView6);
        txtAdoptionDate = (TextView) findViewById(R.id.txtAdoptionDate);
        textView7 = (TextView) findViewById(R.id.textView7);
        txtHeight = (EditText) findViewById(R.id.txtHeight);
        textView8 = (TextView) findViewById(R.id.textView8);
        txtWeight = (EditText) findViewById(R.id.txtWeight);
        textView9 = (TextView) findViewById(R.id.textView9);
        btnImage = (ImageButton) findViewById(R.id.btnImage);
        listView = (ListView) findViewById(R.id.listView);
        listView2 = (ListView) findViewById(R.id.listView2);

        txtName.setEnabled(false);
        btnSex.setEnabled(false);
        txtBreed.setEnabled(false);
        txtAge.setEnabled(false);
        txtAdoptionDate.setEnabled(false);
        txtHeight.setEnabled(false);
        txtWeight.setEnabled(false);
        btnImage.setEnabled(false);

        Intent intent = getIntent();

        pid = intent.getStringExtra(TAG_PID);

        JSONObject dataJson = new JSONObject();
        try {
            dataJson.put("pid", pid);
        } catch (JSONException e) {

        }
        postData(url_pet, dataJson, 1);
        postData(url_appointment, dataJson, 2);

        setupTabs();
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
                        checkResponse(response, json);
                        break;
                    case 2:
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

    private void checkResponse(JSONObject response, JSONObject creds) {
        try {
            if (response.getInt(TAG_SUCCESS) == 1) {

                JSONArray ownerObj = response.getJSONArray(TAG_PET);

                JSONObject pet = ownerObj.getJSONObject(0);
                petName = pet.getString(TAG_NAME).toLowerCase();
                petSex = pet.getString(TAG_SEX).toLowerCase();
                petBreed = pet.getString(TAG_BREED).toLowerCase();
                petAge = pet.getString(TAG_AGE).toLowerCase();
                petDate = pet.getString(TAG_DATEOFADOPTION).toLowerCase();
                petHeight = pet.getString(TAG_HEIGHT).toLowerCase();
                petWeight = pet.getString(TAG_WEIGHT).toLowerCase();
                petImage = pet.getString(TAG_IMAGE).toLowerCase();

                txtName.setText(petName);
                btnSex.setText(petSex);
                txtBreed.setText(petBreed);
                txtAge.setText(petAge);
                txtAdoptionDate.setText(petDate);
                txtHeight.setText(petHeight);
                txtWeight.setText(petWeight);
//                btnImage

//                pDialog.dismiss();
            } else {
                Log.i("nric", "oops");
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

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
                    String date = c.getString(TAG_DATE);
                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        Date parsedDate = simpleDateFormat2.parse(date);
                        simpleDateFormat2 = new SimpleDateFormat("dd MMMM yyyy");
                        newFormatttedDate = simpleDateFormat2.format(parsedDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String startime = c.getString(TAG_STARTTIME);
                    String endtime = c.getString(TAG_ENDTIME);
                    String status = c.getString(TAG_STATUS);
                    String pid = c.getString(TAG_PID);

                    a = new Appointment(id, newFormatttedDate, startime, endtime, status, pid);
                    AppointmentList.add(a);
                }

                for (Appointment a1 : AppointmentList) {
                    if(a1.getStatus().equals("completed")){
                        completedList.add(a1);
                    }
                    else{
                        pendingList.add(a1);
                    }
                }

                CustomAdapter2 myCustomAdapter = new CustomAdapter2(UserPetDetails.this, pendingList);
                listView.setAdapter(myCustomAdapter);

                CustomAdapter2 myCustomAdapter2 = new CustomAdapter2(UserPetDetails.this, completedList);
                listView2.setAdapter(myCustomAdapter2);
//                pDialog.dismiss();
            } else {
//                pDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private void setupTabs() {

        TabHost tabs = (TabHost) this.findViewById(R.id.DStabhost);
        tabs.setup();

        //Tab #1 SharedPreferences Example
        TabHost.TabSpec ts1 = tabs.newTabSpec("SharedPreferences");
        ts1.setIndicator("profile");
        ts1.setContent(R.id.content1);
        tabs.addTab(ts1);

        //Tab #2 SQLiteDatabase Example
        TabHost.TabSpec ts2 = tabs.newTabSpec("SQLiteDatabase");
        ts2.setIndicator("appointments");
        ts2.setContent(R.id.content2);
        tabs.addTab(ts2);
    }
}
