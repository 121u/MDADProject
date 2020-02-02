package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPasswordActivity extends AppCompatActivity {

    public static String ipBaseAddress = "http://vetmdad.atspace.cc";

    Button btnUpdatepw;
    EditText etAnswer2, et_newPW, etUsername;

    String security, password, username;

    private static final String url_update = ipBaseAddress + "/updatePassword.php";

    private static final String TAG_PASSWORD = "password";
    private static final String TAG_SECURITY = "security";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_SUCCESS = "success";
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        setContentView(R.layout.activity_forget_password);
        password = intent.getStringExtra(TAG_PASSWORD);
        security = intent.getStringExtra(TAG_SECURITY);

        btnUpdatepw = (Button)findViewById(R.id.btnUpdatepw);
        et_newPW = (EditText) findViewById(R.id.et_newPW);
        etAnswer2 = (EditText) findViewById(R.id.etAnswer2);
        etUsername = (EditText) findViewById(R.id.etUsername);

        btnUpdatepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                password = et_newPW.getText().toString();
                security = etAnswer2.getText().toString();

                if (security.isEmpty()) {
                    etAnswer2.setError(getString(R.string.error_field_required));

                } else if (password.isEmpty()) {
                    et_newPW.setError(getString(R.string.error_field_required));

                }else if(username.isEmpty()){
                    etUsername.setError(getString(R.string.error_field_required));
                }
                else {

                    pDialog = new ProgressDialog(ForgetPasswordActivity.this);
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
                    postData(url_update, dataJson, 1);
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
                Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
