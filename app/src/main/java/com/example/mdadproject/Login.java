package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private TextView textView;
    private EditText txtNric;
    private TextView textView3;
    private EditText txtPassword;
    private Button btnLogin;
    private TextView btnSignUp;
    private TextView textView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView = (TextView)findViewById( R.id.textView );
        txtNric = (EditText)findViewById( R.id.txtUsername);
        textView3 = (TextView)findViewById( R.id.textView3 );
        txtPassword = (EditText)findViewById( R.id.txtPassword );
        btnLogin = (Button)findViewById( R.id.btnLogin );
        btnSignUp = (TextView)findViewById( R.id.btnSignUp );
        textView5 = (TextView)findViewById( R.id.textView5 );
//hello
//        btnLogin.setOnClickListener( this );

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
