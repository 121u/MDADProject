package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RegisterDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);

        getSupportActionBar().hide();
    }
}
