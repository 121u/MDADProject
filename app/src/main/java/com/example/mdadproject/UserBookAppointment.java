package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mdadproject.Models.Appointment;
import com.example.mdadproject.Models.Pet;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
//import com.leinardi.android.speeddial.SpeedDialActionItem;
//import com.leinardi.android.speeddial.SpeedDialView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserBookAppointment extends AppCompatActivity {
    private ProgressDialog pDialog;
    private DrawerLayout drawer;
    private TextView txtGreeting;
    private TextInputLayout etAptDate;
    private TextInputEditText etAptDateIn;
    private TextInputLayout etAptTime;
    private TextInputLayout etAptPet;
    private RelativeLayout btmToolbar;
    private Button btnNext;
    private Button btnUpdate;
    private Button btnDelete;
    final Calendar myCalendar = Calendar.getInstance();

    String username;

    public static String url_create_appointment = UserLogin.ipBaseAddress + "/create_appointmentJson.php";
    public static String url_get_PetList = UserLogin.ipBaseAddress + "/get_all_petsJson.php";
    public static String url_get_appintmentDetails = UserLogin.ipBaseAddress + "/get_appointment_detailsJson.php";
    private static final String url_update_apt = UserLogin.ipBaseAddress + "/update_appointment_detailsJson.php";
    private static final String url_delete_apt = UserLogin.ipBaseAddress + "/delete_apt.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_APPOINTMENT = "appointment";
    private static final String TAG_ID = "id";
    private static final String TAG_DATE = "date";
    private static final String TAG_STARTTIME = "starttime";
    private static final String TAG_ENDTIME = "endtime";
    private static final String TAG_STATUS = "status";
    private static final String TAG_PID = "pid";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PETNAME = "petname";

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
    String name, apt_id, date, starttime, endttime, status, pid, petname, id, savedAptDate, chosenPid, updDate, updStartTime;

    String[] timeOptions = new String[]{"1100", "1200", "1300", "1400", "1500", "1600", "1700"};
    AutoCompleteTextView editTextFilledExposedDropdown2;
    ArrayAdapter<Pet> adapter2;
    JSONArray appointments = null;
    String newFormattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_appointment);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("We're giving it whatevfur we'ge got..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        Intent intent = getIntent();
        pid = intent.getStringExtra(TAG_PID);
        username = intent.getStringExtra(TAG_USERNAME);
        name = intent.getStringExtra(TAG_PETNAME);

        JSONObject dataJson = new JSONObject();
        try {
            dataJson.put("username", username);
        } catch (JSONException e) {

        }
        postData(url_get_PetList, dataJson, 2);

        Intent intent2 = getIntent();
        apt_id = intent2.getStringExtra("apt_id");

//        Log.i("apt_id", apt_id);
        JSONObject dataJson2 = new JSONObject();
        try {
            dataJson2.put(TAG_ID, apt_id);
        } catch (JSONException e) {

        }

        postData(url_get_appintmentDetails, dataJson2, 3);

        etAptDate = (TextInputLayout) findViewById(R.id.etAptDate);
        etAptDateIn = (TextInputEditText) findViewById(R.id.etAptDateIn);
        etAptTime = (TextInputLayout) findViewById(R.id.etAptTime);
        etAptPet = (TextInputLayout) findViewById(R.id.etAptPet);
        btmToolbar = (RelativeLayout) findViewById(R.id.btmToolbar);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Book An Appointment");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.black),
                PorterDuff.Mode.SRC_ATOP);

        if (apt_id != null) {
            btnUpdate.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.GONE);
        }
        else {
            btnUpdate.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, timeOptions);
        AutoCompleteTextView editTextFilledExposedDropdown = findViewById(R.id.filled_exposed_dropdown);
        editTextFilledExposedDropdown.setAdapter(adapter);

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

        etAptDateIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new DatePickerDialog(UserBookAppointment.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()+24*60*60*1000);
                datePickerDialog.show();
            }
        });

        editTextFilledExposedDropdown2 = findViewById(R.id.filled_exposed_dropdown_2);
        adapter2 = new ArrayAdapter<Pet>(this, R.layout.dropdown_menu_popup_item, petList);
        editTextFilledExposedDropdown2.setAdapter(adapter2);
        editTextFilledExposedDropdown2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                Pet pet = (Pet) adapter2.getItem(pos);
                getPetPid(pet);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updDate = etAptDate.getEditText().getText().toString();
                updStartTime = etAptTime.getEditText().getText().toString();

//                date = etAptDate.getEditText().getText().toString();
//                starttime = etAptTime.getEditText().getText().toString();
                String start1 = updStartTime.replaceAll("[^\\d]", "");
                int start2 = Integer.parseInt(start1) + 100;
                endttime = Integer.toString(start2);
                status = "pending";

                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd MMMM yyyy");
                try {
                    Date parsedDate = simpleDateFormat2.parse(updDate);
                    simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                    newFormattedDate = simpleDateFormat2.format(parsedDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (newFormattedDate.equals(date) && updStartTime.equals(starttime)) {
                    finish();
                } else {
                    pDialog = new ProgressDialog(UserBookAppointment.this);
                    pDialog.setMessage("Saving details ...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();

                    JSONObject dataJson = new JSONObject();
                    try {
                        dataJson.put(TAG_DATE, newFormattedDate);
                        dataJson.put(TAG_STARTTIME, updStartTime);
                        dataJson.put(TAG_ENDTIME, endttime);
                        dataJson.put(TAG_ID, apt_id);

                    } catch (JSONException e) {
                    }

                    postData(url_update_apt, dataJson, 4);
                }

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                pDialog = new ProgressDialog(UserBookAppointment.this);
                pDialog.setMessage("Deleting appointment ...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
                // deleting product in background thread
                JSONObject dataJson = new JSONObject();
                try {
                    dataJson.put(TAG_ID, apt_id);
                } catch (JSONException e) {

                }
                postData(url_delete_apt, dataJson, 5);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date = etAptDate.getEditText().getText().toString();
                starttime = etAptTime.getEditText().getText().toString();
                String start1 = starttime.replaceAll("[^\\d]", "");
                int start2 = Integer.parseInt(start1) + 100;
                endttime = Integer.toString(start2);
                status = "pending";
                petname = etAptPet.getEditText().getText().toString();

                pDialog = new ProgressDialog(UserBookAppointment.this);
                pDialog.setMessage("Creating your a-pet-ment..");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();

                JSONObject dataJson = new JSONObject();
                try {
                    dataJson.put(TAG_DATE, savedAptDate);
                    dataJson.put(TAG_STARTTIME, starttime);
                    dataJson.put(TAG_ENDTIME, endttime);
                    dataJson.put(TAG_STATUS, status);
                    dataJson.put(TAG_PID, pid);
                    dataJson.put(TAG_USERNAME, username);
                    dataJson.put(TAG_PETNAME, petname);

                } catch (JSONException e) {

                }
                postData(url_create_appointment, dataJson, 1);
            }
        });
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        String dateToShow = "dd MMMM yyyy";

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        SimpleDateFormat sdf2 = new SimpleDateFormat(dateToShow, Locale.UK);

        savedAptDate = sdf.format(myCalendar.getTime());
        etAptDate.getEditText().setText(sdf2.format(myCalendar.getTime()));
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
                        checkResponseGetPets(response, json);
                        break;
                    case 3:
                        checkResponseReadApt(response, json);
                        break;
                    case 4:
                        checkResponseEditApt(response);
                        break;
                    case 5:
                        checkResponseDeleteApt(response);
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

                Toast.makeText(this, "Appointment successfully booked!", Toast.LENGTH_SHORT).show();
                Intent q = new Intent(this, PetAppointments.class);
                q.putExtra(TAG_NAME, petname);
                q.putExtra(TAG_PID, pid);
                q.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(q);
                finish();

            } else if (response.getInt(TAG_SUCCESS) == 2){
                pDialog.dismiss();
                Toast.makeText(this, "Sorry! Time slot is already taken please try another one.", Toast.LENGTH_SHORT).show();

            }

            else {

            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private void checkResponseGetPets(JSONObject response, JSONObject creds) {
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
//                ArrayAdapter<Pet> adapter = new ArrayAdapter<Pet>(this, android.R.layout.simple_spinner_item, petList);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinner2.setAdapter(adapter);

                pDialog.dismiss();

            } else {
                pDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private void checkResponseReadApt(JSONObject response, JSONObject creds) {
        try {
            if (response.getInt(TAG_SUCCESS) == 1) {

                appointments = response.getJSONArray(TAG_APPOINTMENT);

                // looping through All Products
                for (int i = 0; i < appointments.length(); i++) {
                    JSONObject c = appointments.getJSONObject(i);

                    Appointment a = new Appointment();
                    // Storing each json item in variable
                    String id = c.getString(TAG_ID);
                    date = c.getString(TAG_DATE);
                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date parsedDate = simpleDateFormat2.parse(date);
                        simpleDateFormat2 = new SimpleDateFormat("dd MMMM yyyy");
                        newFormattedDate = simpleDateFormat2.format(parsedDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    starttime = c.getString(TAG_STARTTIME);
                    String endtime = c.getString(TAG_ENDTIME);
                    String status = c.getString(TAG_STATUS);
                    String pid = c.getString(TAG_PID);

                    etAptDate.getEditText().setText(newFormattedDate);
                    etAptTime.getEditText().setText(starttime);
                    etAptPet.getEditText().setText(name);

                    final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, timeOptions);
                    AutoCompleteTextView editTextFilledExposedDropdown = findViewById(R.id.filled_exposed_dropdown);
                    editTextFilledExposedDropdown.setAdapter(adapter);

//                    a = new Appointment(id, newFormattedDate, startime, endtime, status, pid);
//                    AppointmentList.add(a);
                }

                pDialog.dismiss();
            } else {
                pDialog.dismiss();
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    public void checkResponseDeleteApt(JSONObject response) {

        try {
            // dismiss the dialog once product updated
            pDialog.dismiss();
            if (response.getInt("success") == 1) {
                // successfully updated
                Toast.makeText(this, "Appointment successfully deleted!", Toast.LENGTH_SHORT).show();
                Intent q = new Intent(this, PetAppointments.class);
                q.putExtra(TAG_NAME, name);
                q.putExtra(TAG_PID, pid);
                q.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(q);
                finish();

            } else {
                // product with pid not found
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void checkResponseEditApt(JSONObject response) {
        try {
            pDialog.dismiss();
            if (response.getInt("success") == 1) {

                Toast.makeText(this, "Appointment successfully updated!", Toast.LENGTH_SHORT).show();
                Intent q = new Intent(this, PetAppointments.class);
                q.putExtra(TAG_NAME, name);
                q.putExtra(TAG_PID, pid);
                q.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(q);
                finish();

//                JSONArray aptObj = response.getJSONArray(TAG_APPOINTMENT);
//
//                JSONObject apt = aptObj.getJSONObject(0);
//                date = apt.getString(TAG_DATE);
//                starttime = apt.getString(TAG_STARTTIME);
//                apt_id = apt.getString(TAG_ID);
//
//
//                etAptDate.getEditText().setText(date);
//                etAptTime.getEditText().setText(starttime);

            } else if (response.getInt(TAG_SUCCESS) == 2){
                pDialog.dismiss();
                Toast.makeText(this, "Sorry! Time slot is already taken please try another one.", Toast.LENGTH_SHORT).show();

            }
            else {
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }
}

