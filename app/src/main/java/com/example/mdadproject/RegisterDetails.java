package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterDetails extends AppCompatActivity {

    private TextView txtView;
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

    public static String nric, name, mobilenumber, email, address, zipcode, username;
    public static String url_owner = Login.ipBaseAddress + "/get_owner_detailsJson.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_OWNERS = "owner";
    private static final String TAG_NRIC = "nric";
    private static final String TAG_NAME = "name";
    private static final String TAG_MOBILENUMBER = "mobilenumber";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_ZIPCODE = "zipcode";
    private static final String TAG_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);

        txtView = (TextView) findViewById(R.id.txtView);
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
            getSupportActionBar().setTitle("");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.black),
                    PorterDuff.Mode.SRC_ATOP);
        }

        Intent intent = getIntent();
        username = intent.getStringExtra(TAG_USERNAME);

        if (username != null && username.equals("staff")) {
            btnUpdate.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        } else if (username != null) {
            txtView.setText("your profile");
            txtView.setTextColor(Color.BLACK);
            etNric.getEditText().setEnabled(false);
            etName.getEditText().setEnabled(false);
            etMobileNumber.getEditText().setEnabled(false);
            etEmail.getEditText().setEnabled(false);
            etAddress.getEditText().setEnabled(false);
            etZipcode.getEditText().setEnabled(false);
            btmToolbar.setVisibility(View.GONE);
            JSONObject dataJson = new JSONObject();
            try {
                dataJson.put("username", username);
            } catch (JSONException e) {

            }
            postData(url_owner, dataJson, 1);
        } else {
            btnUpdate.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    Intent i = new Intent(v.getContext(), RegisterUserPass.class);
                    startActivity(i);
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
                        checkResponseRead(response, json);
                        break;
//                    case 2:checkResponseSave_delete_Product(response); break;
//                    case 3:
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

    private void checkResponseRead(JSONObject response, JSONObject creds) {
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

                etNric.getEditText().setText(nric);
                etName.getEditText().setText(name);
                etMobileNumber.getEditText().setText(mobilenumber);
                etEmail.getEditText().setText(email);
                etAddress.getEditText().setText(address);
                etZipcode.getEditText().setText(zipcode);

//                pDialog.dismiss();
            } else {
                Log.i("nric", "oops");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
