package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterUserPass extends AppCompatActivity {

    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnNext;
    private ProgressDialog pDialog;

    public static String username, password;

    private static String url_create_owner = Login.ipBaseAddress + "/create_ownerJson.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NRIC = "nric";
    private static final String TAG_FIRSTNAME = "firstname";
    private static final String TAG_LASTNAME = "lastname";
    private static final String TAG_TELEPHONE = "telephone";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_ZIPCODE = "zipcode";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_pass);

        Log.i("Ip address CREATE ", url_create_owner);

        txtUsername = (EditText) findViewById(R.id.txtNric);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnNext = (Button) findViewById(R.id.btnNext);

        getSupportActionBar().hide();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = txtUsername.getText().toString();
                password = txtPassword.getText().toString();

                if (username.isEmpty()) {
                    txtUsername.setError(getString(R.string.error_field_required));

                } else if (password.isEmpty()) {
                    txtPassword.setError(getString(R.string.error_field_required));

                } else {

                    pDialog = new ProgressDialog(RegisterUserPass.this);
                    pDialog.setMessage("Welcome to the bark side..");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();

                    JSONObject dataJson = new JSONObject();
                    try {

                        dataJson.put(TAG_NRIC, RegisterDetails.nric);
                        dataJson.put(TAG_FIRSTNAME, RegisterDetails.firstName);
                        dataJson.put(TAG_LASTNAME, RegisterDetails.lastName);
                        dataJson.put(TAG_TELEPHONE, RegisterDetails.telephone);
                        dataJson.put(TAG_EMAIL, RegisterDetails.email);
                        dataJson.put(TAG_ADDRESS, RegisterDetails.address);
                        dataJson.put(TAG_ZIPCODE, RegisterDetails.zipcode);
                        dataJson.put(TAG_USERNAME, username);
                        dataJson.put(TAG_PASSWORD, password);

                    } catch (JSONException e) {

                    }
                    postData(url_create_owner, dataJson, 1);
                }
            }
        });
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

    public void checkResponseCreate_Owner(JSONObject response) {
        Log.i("----Response", response + " ");
        try {
            if (response.getInt(TAG_SUCCESS) == 1) {

                finish();
                Intent i = new Intent(this, Login.class);
                startActivity(i);
                // dismiss the dialog once product uupdated
                pDialog.dismiss();

            } else {
                // product with pid not found
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }


}
