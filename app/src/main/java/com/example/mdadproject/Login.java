package com.example.mdadproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private TextView textView;
    private EditText txtUsername;
    private TextView textView3;
    private EditText txtPassword;
    private Button btnLogin;
    private TextView btnSignUp;
    private TextView textView5;
    // url to update product
//    private static final String url_login = Login.ipBaseAddress+"/loginJ.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView = (TextView) findViewById(R.id.textView);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        textView3 = (TextView) findViewById(R.id.textView3);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (TextView) findViewById(R.id.btnSignUp);
        textView5 = (TextView) findViewById(R.id.textView5);

        getSupportActionBar().hide();
        setTitle("login");
//        btnLogin.setEnabled(true);

        btnSignUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                btnLogin.setEnabled(false);
                btnSignUp.setClickable(false);
                Intent i = new Intent(v.getContext(), RegisterDetails.class);
                startActivity(i);
                return false;
            }
        });

    }

}
