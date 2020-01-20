package com.example.mdadproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class UserLogin extends AppCompatActivity {

    public static String ipBaseAddress = "http://vetmdad.atspace.cc";

    private TextInputLayout etUsername;
    private TextInputLayout etPassword;
    private Button btnLogin;
    private TextView btnSignUp;
    private ProgressDialog pDialog;
    // url to update product

    private static final String url_login = ipBaseAddress + "/login.php";
    // JSON Node names

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        etUsername = (TextInputLayout) findViewById(R.id.etUsername);
        etPassword = (TextInputLayout) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (TextView) findViewById(R.id.btnSignUp);

        getSupportActionBar().hide();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), UserDetails.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                String pw = etPassword.getEditText().getText().toString();
                String uName = etUsername.getEditText().getText().toString();

                if (pw.isEmpty()) {
                    etPassword.setErrorEnabled(false);
                    etPassword.setError(getString(R.string.error_field_required));
                } else if (uName.isEmpty()) {
                    etUsername.setErrorEnabled(false);
                    etUsername.setError(getString(R.string.error_field_required));

                } else {
                    pDialog = new ProgressDialog(UserLogin.this);
                    pDialog.setMessage("This purr-obably won't take long..");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();

                    JSONObject dataJson = new JSONObject();
                    try {
                        dataJson.put(TAG_USERNAME, uName);
                        dataJson.put(TAG_PASSWORD, pw);
                    } catch (JSONException e) {

                    }

                    postData(url_login, dataJson, 1);

                }
            }
        });
    }

    public void postData(String url, final JSONObject json, final int option) {


        Log.i("=======", url);


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest json_obj_req = new JsonObjectRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                switch (option) {
                    case 1:
                        checkResponseLogin(response);
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

    public void checkResponseLogin(JSONObject response) {
        Log.i("----Response", response + " " + url_login);
        try {
            if (response.getInt(TAG_SUCCESS) == 1) {
                String username = etUsername.getEditText().getText().toString();
                if (username.equals("staff")) {
                    Constants.IS_STAFF = "yes";
                }
                Intent intent = new Intent(getApplicationContext(), UserPets.class);
                intent.putExtra(TAG_USERNAME, username);
                startActivityForResult(intent, 100);
                Log.i("page1", username);
                pDialog.dismiss();
            } else {
                Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


