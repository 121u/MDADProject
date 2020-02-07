package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mdadproject.Utils.Constants;
import com.example.mdadproject.Utils.SaveSharedPreference;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserDetails extends AppCompatActivity {

    private TextInputLayout etNric;
    private TextInputLayout etName;
    private TextInputLayout etMobileNumber;
    private TextInputLayout etEmail;
    private TextInputLayout etAddress;
    private TextInputLayout etZipcode;
    private Button btnNext;
    private Button btnUpdate;
    private Button btnDelete;
    private RelativeLayout btmToolbar;

    public static String nric, name, mobilenumber, email, address, zipcode, username, owner_username, password;

    private ProgressDialog pDialog;
    private static final String url_owner = UserLogin.ipBaseAddress + "/get_owner_detailsJson.php";
    private static final String url_delete = UserLogin.ipBaseAddress + "/delete_owner_detailsJson.php";
    private static final String url_update_details = UserLogin.ipBaseAddress + "/update_owner_detailsJson.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_OWNERS = "owner";
    private static final String TAG_NRIC = "nric";
    private static final String TAG_NAME = "name";
    private static final String TAG_MOBILENUMBER = "mobilenumber";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_ZIPCODE = "zipcode";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        etNric = (TextInputLayout) findViewById(R.id.etNric);
        etName = (TextInputLayout) findViewById(R.id.etName);
        etMobileNumber = (TextInputLayout) findViewById(R.id.etMobileNumber);
        etEmail = (TextInputLayout) findViewById(R.id.etEmail);
        etAddress = (TextInputLayout) findViewById(R.id.etAddress);
        etZipcode = (TextInputLayout) findViewById(R.id.etZipcode);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btmToolbar = (RelativeLayout) findViewById(R.id.btmToolbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.black),
                    PorterDuff.Mode.SRC_ATOP);
        }

        Intent intent = getIntent();
//        username = SaveSharedPreference.getUserName(UserDetails.this);
        username = intent.getStringExtra(TAG_USERNAME);

        JSONObject dataJson = new JSONObject();
        try {
            dataJson.put(TAG_USERNAME, username);
        } catch (JSONException e) {

        }

        Intent intent1 = getIntent();
        owner_username = intent1.getStringExtra("owner_username");

        JSONObject dataJson2 = new JSONObject();
        try {
            dataJson2.put(TAG_USERNAME, owner_username);
        } catch (JSONException e) {

        }

        pDialog = new ProgressDialog(UserDetails.this);
        pDialog.setMessage("Loading your profile ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        if (username != null && SaveSharedPreference.getUserName(UserDetails.this).equals("staff")) {
            etNric.getEditText().setEnabled(false);
            etName.getEditText().setEnabled(false);

            btnUpdate.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.GONE);

            postData(url_owner, dataJson2, 1);

        } else if (username != null) {
            setTitle("Your Profile");
            etNric.getEditText().setEnabled(false);
            etName.getEditText().setEnabled(false);

            btnDelete.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);

            postData(url_owner, dataJson, 1);

        } else {
            getSupportActionBar().setTitle("Register");
            btnUpdate.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            pDialog.dismiss();
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nric = etNric.getEditText().getText().toString().toUpperCase();
                name = etName.getEditText().getText().toString().toUpperCase();
                mobilenumber = etMobileNumber.getEditText().getText().toString().toUpperCase();
                email = etEmail.getEditText().getText().toString().toUpperCase();
                address = etAddress.getEditText().getText().toString().toUpperCase();
                zipcode = etZipcode.getEditText().getText().toString().toUpperCase();

                pDialog = new ProgressDialog(UserDetails.this);
                pDialog.setMessage("Saving details ...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();

                if (username != null && SaveSharedPreference.getUserName(UserDetails.this).equals("staff")) {
                    JSONObject dataJson = new JSONObject();
                    try {
                        dataJson.put(TAG_NAME, name);
                        dataJson.put(TAG_NRIC, nric);
                        dataJson.put(TAG_MOBILENUMBER, mobilenumber);
                        dataJson.put(TAG_ZIPCODE, zipcode);
                        dataJson.put(TAG_ADDRESS, address);
                        dataJson.put(TAG_EMAIL, email);
                        dataJson.put(TAG_USERNAME, owner_username);
                        dataJson.put(TAG_PASSWORD, password);
                    } catch (JSONException e) {
                    }
                    postData(url_update_details, dataJson, 2);

                } else {
                    JSONObject dataJson = new JSONObject();
                    try {
                        dataJson.put(TAG_NAME, name);
                        dataJson.put(TAG_NRIC, nric);
                        dataJson.put(TAG_MOBILENUMBER, mobilenumber);
                        dataJson.put(TAG_ZIPCODE, zipcode);
                        dataJson.put(TAG_ADDRESS, address);
                        dataJson.put(TAG_EMAIL, email);
                        dataJson.put(TAG_USERNAME, username);
                        dataJson.put(TAG_PASSWORD, password);
                    } catch (JSONException e) {
                    }
                    postData(url_update_details, dataJson, 2);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                pDialog = new ProgressDialog(UserDetails.this);
                pDialog.setMessage("Deleting owner ...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
                // deleting product in background thread
                JSONObject dataJson = new JSONObject();
                try {
                    dataJson.put(TAG_USERNAME, username);
                } catch (JSONException e) {

                }
                postData(url_delete, dataJson, 3);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (username != null) {
                    Intent i = new Intent(v.getContext(), UserPass.class);
                    i.putExtra(TAG_USERNAME, username);
                    i.putExtra(TAG_PASSWORD, password);
                    startActivity(i);
                }
                else {
                    nric = etNric.getEditText().getText().toString().toUpperCase();
                    name = etName.getEditText().getText().toString().toUpperCase();
                    mobilenumber = etMobileNumber.getEditText().getText().toString().toUpperCase();
                    email = etEmail.getEditText().getText().toString().toUpperCase();
                    address = etAddress.getEditText().getText().toString().toUpperCase();
                    zipcode = etZipcode.getEditText().getText().toString().toUpperCase();

                    if (nric.isEmpty()) {
                        etNric.setError(getString(R.string.error_field_required));
                    } else if (name.isEmpty()) {
                        etName.setError(getString(R.string.error_field_required));
                    } else if (mobilenumber.isEmpty()) {
                        etMobileNumber.setError(getString(R.string.error_field_required));
                    } else if (email.isEmpty()) {
                        etEmail.setError(getString(R.string.error_field_required));
                    } else if (address.isEmpty()) {
                        etAddress.setError(getString(R.string.error_field_required));
                    } else if (zipcode.isEmpty()) {
                        etZipcode.setError(getString(R.string.error_field_required));
                    } else {
                        Intent i = new Intent(v.getContext(), UserPass.class);
                        startActivity(i);
                    }
                }
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
                        checkResponseReadOwner(response);
                        break;
                    case 2:
                        checkResponseEditOwner(response);
                        break;
                    case 3:
                        checkResponseDeleteOwner(response);
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

    private void checkResponseReadOwner(JSONObject response) {
        try {
            if (response.getInt(TAG_SUCCESS) == 1) {

                JSONArray ownerObj = response.getJSONArray(TAG_OWNERS);

                JSONObject owner = ownerObj.getJSONObject(0);
                nric = owner.getString(TAG_NRIC);
                name = owner.getString(TAG_NAME);
                mobilenumber = owner.getString(TAG_MOBILENUMBER);
                email = owner.getString(TAG_EMAIL);
                address = owner.getString(TAG_ADDRESS);
                zipcode = owner.getString(TAG_ZIPCODE);
                password = owner.getString(TAG_PASSWORD);

                etNric.getEditText().setText(nric);
                etName.getEditText().setText(name);
                etMobileNumber.getEditText().setText(mobilenumber);
                etEmail.getEditText().setText(email);
                etAddress.getEditText().setText(address);
                etZipcode.getEditText().setText(zipcode);

                pDialog.dismiss();
            } else {
                pDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkResponseDeleteOwner(JSONObject response) {

        try {
            // dismiss the dialog once product updated
            pDialog.dismiss();
            if (response.getInt("success") == 1) {
                // successfully updated
                finish();
                Intent i = new Intent(getApplicationContext(), StaffAppointments.class);
                // send result code 100 to notify about product update
                setResult(100, i);

            } else {
                // product with pid not found
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkResponseEditOwner(JSONObject response) {
        try {
            pDialog.dismiss();
            if (response.getInt("success") == 1) {
                Toast.makeText(this, "Details successfully updated", Toast.LENGTH_SHORT).show();

//                JSONArray ownerObj = response.getJSONArray(TAG_OWNERS);
//
//                JSONObject owner = ownerObj.getJSONObject(0);
//                nric = owner.getString(TAG_NRIC);
//                name = owner.getString(TAG_NAME);
//                mobilenumber = owner.getString(TAG_MOBILENUMBER);
//                email = owner.getString(TAG_EMAIL);
//                address = owner.getString(TAG_ADDRESS);
//                zipcode = owner.getString(TAG_ZIPCODE);
//                username = owner.getString(TAG_USERNAME);
//                password = owner.getString(TAG_PASSWORD);
//
//                etNric.getEditText().setText(nric);
//                etName.getEditText().setText(name);
//                etMobileNumber.getEditText().setText(mobilenumber);
//                etEmail.getEditText().setText(email);
//                etAddress.getEditText().setText(address);
//                etZipcode.getEditText().setText(zipcode);
            } else {
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
