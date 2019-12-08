package com.example.mdadproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
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

public class UserProfile extends AppCompatActivity {

    private TextView textView5;
    private TextView textView;
    private EditText txtNric;
    private TextView textView2;
    private EditText txtFirstName;
    private TextView textView3;
    private EditText txtLastName;
    private TextView textView4;
    private EditText txtTel;
    private TextView textView6;
    private EditText txtEmail;
    private TextView textView7;
    private EditText txtAddress;
    private TextView textView8;
    private EditText txtZipcode;
    private ProgressDialog pDialog;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_OWNERS = "owner";
    private static final String TAG_OWNERID = "ownerid";
    private static final String TAG_NRIC = "nric";
    private static final String TAG_FIRSTNAME = "firstname";
    private static final String TAG_LASTNAME = "lastname";
    private static final String TAG_TELEPHONE = "telephone";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_ZIPCODE = "zipcode";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PASSWORD = "password";

    private static  String ownNric = "";
    private static  String ownFirstName = "";
    private static  String ownLastName = "";
    private static  String ownTel = "";
    private static  String ownEmail = "";
    private static  String ownAddress = "";
    private static  String ownZipcode = "";
    private static  String ownUsername = "";

    JSONObject json=null;
    public static String url_owner = Login.ipBaseAddress + "/get_owner_detailsJson.php";
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("We're seeing a lot of pet-tential..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.black),
                PorterDuff.Mode.SRC_ATOP);

        textView5 = (TextView) findViewById(R.id.textView5);
        textView = (TextView) findViewById(R.id.textView);
        txtNric = (EditText) findViewById(R.id.txtUsername);
        textView2 = (TextView) findViewById(R.id.textView2);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        textView3 = (TextView) findViewById(R.id.textView3);
        txtLastName = (EditText) findViewById(R.id.txtBreed);
        textView4 = (TextView) findViewById(R.id.textView4);
        txtTel = (EditText) findViewById(R.id.txtTel);
        textView6 = (TextView) findViewById(R.id.textView6);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        textView7 = (TextView) findViewById(R.id.textView7);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        textView8 = (TextView) findViewById(R.id.textView8);
        txtZipcode = (EditText) findViewById(R.id.txtZipcode);

        txtNric.setEnabled(false);
        txtFirstName.setEnabled(false);
        txtLastName.setEnabled(false);
        txtTel.setEnabled(false);
        txtEmail.setEnabled(false);
        txtAddress.setEnabled(false);
        txtZipcode.setEnabled(false);

        Log.i("url_owner", url_owner);
        Intent intent = getIntent();

        username = intent.getStringExtra(TAG_USERNAME);

        JSONObject dataJson = new JSONObject();
        try {
            dataJson.put("username", username);
        } catch (JSONException e) {

        }
        postData(url_owner, dataJson);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void postData(String url, final JSONObject json){
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

    private void checkResponse(JSONObject response, JSONObject creds) {
        try {
            if (response.getInt(TAG_SUCCESS) == 1) {

                JSONArray ownerObj = response.getJSONArray(TAG_OWNERS);

                JSONObject owner = ownerObj.getJSONObject(0);
                ownNric = owner.getString(TAG_NRIC);
                ownFirstName = owner.getString(TAG_FIRSTNAME);
                ownLastName = owner.getString(TAG_LASTNAME);
                ownTel = owner.getString(TAG_TELEPHONE);
                ownEmail = owner.getString(TAG_EMAIL);
                ownAddress = owner.getString(TAG_ADDRESS);
                ownZipcode = owner.getString(TAG_ZIPCODE);

                txtNric.setText(ownNric);
                txtFirstName.setText(ownFirstName);
                txtLastName.setText(ownLastName);
                txtTel.setText(ownTel);
                txtEmail.setText(ownEmail);
                txtAddress.setText(ownAddress);
                txtZipcode.setText(ownZipcode);

                pDialog.dismiss();
            } else {
                Log.i("nric","oops");
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }
}
