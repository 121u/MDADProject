package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterDetails extends AppCompatActivity {

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
    private Button btnNext;

    public static String nric, firstName, lastName, telephone, email, address, zipcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);

        textView5 = (TextView) findViewById(R.id.textView5);
        textView = (TextView) findViewById(R.id.textView);
        txtNric = (EditText) findViewById(R.id.txtNric);
        textView2 = (TextView) findViewById(R.id.textView2);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        textView3 = (TextView) findViewById(R.id.textView3);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        textView4 = (TextView) findViewById(R.id.textView4);
        txtTel = (EditText) findViewById(R.id.txtTel);
        textView6 = (TextView) findViewById(R.id.textView6);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        textView7 = (TextView) findViewById(R.id.textView7);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        textView8 = (TextView) findViewById(R.id.textView8);
        txtZipcode = (EditText) findViewById(R.id.txtZipcode);
        btnNext = (Button) findViewById(R.id.btnNext);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.black),
                    PorterDuff.Mode.SRC_ATOP);
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nric = txtNric.getText().toString().toUpperCase();
                firstName = txtFirstName.getText().toString().toUpperCase();
                lastName = txtLastName.getText().toString().toUpperCase();
                telephone = txtTel.getText().toString().toUpperCase();
                email = txtEmail.getText().toString().toUpperCase();
                address = txtAddress.getText().toString().toUpperCase();
                zipcode = txtZipcode.getText().toString().toUpperCase();

                if (nric.isEmpty()) {
                    txtNric.setError(getString(R.string.error_field_required));
                } else if (firstName.isEmpty()) {
                    txtFirstName.setError(getString(R.string.error_field_required));
                } else if (lastName.isEmpty()) {
                    txtLastName.setError(getString(R.string.error_field_required));
                } else if (telephone.isEmpty()) {
                    txtTel.setError(getString(R.string.error_field_required));
                } else if (email.isEmpty()) {
                    txtEmail.setError(getString(R.string.error_field_required));
                } else if (address.isEmpty()) {
                    txtAddress.setError(getString(R.string.error_field_required));
                } else if (zipcode.isEmpty()) {
                    txtZipcode.setError(getString(R.string.error_field_required));
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
}
