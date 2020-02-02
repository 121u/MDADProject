package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class UserPass extends AppCompatActivity {

    private TextView textView;
    public TextInputLayout etUsername, etPassword, etSecurity;
    private RelativeLayout btmToolbar;
    private Button btnSignUp;
    private ProgressDialog pDialog;

    public static String username, password, qr, security;

    private static String url_create_owner = UserLogin.ipBaseAddress + "/create_ownerJson2.php";
    private static final String url_update = UserLogin.ipBaseAddress + "/updatePassword.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NRIC = "nric";
    private static final String TAG_NAME = "name";
    private static final String TAG_MOBILENUMBER = "mobilenumber";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_ZIPCODE = "zipcode";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_SECURITY = "security";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pass);

        Intent intent = getIntent();
        username = intent.getStringExtra(TAG_USERNAME);
        qr = intent.getStringExtra("qr");
        password = intent.getStringExtra(TAG_PASSWORD);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.black),
                    PorterDuff.Mode.SRC_ATOP);
        }

        Log.i("Ip address CREATE ", url_create_owner);

        etUsername = (TextInputLayout) findViewById(R.id.etUsername);
        etPassword = (TextInputLayout) findViewById(R.id.etPassword);
        etSecurity = (TextInputLayout)findViewById(R.id.etSecurity);

        btmToolbar = (RelativeLayout) findViewById(R.id.btmToolbar);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        if (username != null && username.equals("staff") && Constants.IS_STAFF.equals("yes")) {
            textView.setVisibility(View.GONE);
            btmToolbar.setVisibility(View.GONE);
            etUsername.getEditText().setText(qr);
            etPassword.getEditText().setText(password);


        } else {
            getSupportActionBar().setTitle("Register");
            btmToolbar.setVisibility(View.VISIBLE);
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = etUsername.getEditText().getText().toString();
                password = etPassword.getEditText().getText().toString();
                security = etSecurity.getEditText().getText().toString();

                if (UserDetails.nric != null) {
                    if (username.isEmpty()) {
                        etUsername.setError(getString(R.string.error_field_required));

                    } else if (password.isEmpty()) {
                        etPassword.setError(getString(R.string.error_field_required));

                    } else if (security.isEmpty()) {
                        etSecurity.setError(getString(R.string.error_field_required));

                    } else {

                        pDialog = new ProgressDialog(UserPass.this);
                        pDialog.setMessage("Welcome to the bark side..");
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(true);
                        pDialog.show();

                        JSONObject dataJson = new JSONObject();
                        try {

                            dataJson.put(TAG_NRIC, UserDetails.nric);
                            dataJson.put(TAG_NAME, UserDetails.name);
                            dataJson.put(TAG_MOBILENUMBER, UserDetails.mobilenumber);
                            dataJson.put(TAG_EMAIL, UserDetails.email);
                            dataJson.put(TAG_ADDRESS, UserDetails.address);
                            dataJson.put(TAG_ZIPCODE, UserDetails.zipcode);
                            dataJson.put(TAG_USERNAME, username);
                            dataJson.put(TAG_PASSWORD, password);
                            dataJson.put(TAG_SECURITY, security);

                        } catch (JSONException e) {

                        }
                        postData(url_create_owner, dataJson, 1);
                    }
                }
                else {

                    if (username.isEmpty()) {
                        etUsername.setError(getString(R.string.error_field_required));

                    } else if (password.isEmpty()) {
                        etPassword.setError(getString(R.string.error_field_required));

                    } else if (security.isEmpty()) {
                        etSecurity.setError(getString(R.string.error_field_required));
                    }
                    else {
                        pDialog = new ProgressDialog(UserPass.this);
                        pDialog.setMessage("Welcome to the bark side..");
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(true);
                        pDialog.show();

                        JSONObject dataJson = new JSONObject();
                        try {

                            dataJson.put(TAG_SECURITY, security);
                            dataJson.put(TAG_USERNAME, username);
                            dataJson.put(TAG_PASSWORD, password);

                        } catch (JSONException e) {

                        }
                        postData(url_update, dataJson, 2);
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
                        checkResponseCreate_Owner(response);
                        break;
                    case 2:
                        checkResponseUpdate_PW(response);
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

    public void checkResponseCreate_Owner(JSONObject response) {
        Log.i("----Response", response + " ");
        try {
            if (response.getInt(TAG_SUCCESS) == 1) {

                finish();
                Intent i = new Intent(this, UserLogin.class);
                startActivity(i);
                // dismiss the dialog once product updated
                pDialog.dismiss();
            }

            else {
                // product with pid not found
                pDialog.dismiss();
                Toast.makeText(this, "Username is already in use. Please try again", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("here2","here2");
        }
    }

    public void checkResponseUpdate_PW(JSONObject response) {
        Log.i("----Response", response + " ");
        try {
            if (response.getInt(TAG_SUCCESS) == 1) {

                finish();
                Intent i = new Intent(this, UserLogin.class);
                startActivity(i);
                // dismiss the dialog once product updated
                pDialog.dismiss();
                Log.i("here","here");
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
            }

            else {
                // product with pid not found
                pDialog.dismiss();
                Log.i("here1","here1");
                Toast.makeText(this, "Username and security question do not match", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
