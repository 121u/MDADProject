package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class RegisterDetails extends AppCompatActivity {

    private TextInputLayout etNric;
    private TextInputLayout etName;
    private TextInputLayout txtLastName;
    private TextInputLayout etMobileNumber;
    private TextInputLayout etEmail;
    private TextInputLayout etAddress;
    private TextInputLayout etZipcode;
    private Button btnNext;

    public static String nric, name, mobilenumber, email, address, zipcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);

        etNric = (TextInputLayout) findViewById(R.id.etNric);
        etName = (TextInputLayout) findViewById(R.id.etName);
        etMobileNumber = (TextInputLayout) findViewById(R.id.etMobileNumber);
        etEmail = (TextInputLayout) findViewById(R.id.etEmail);
        etAddress = (TextInputLayout) findViewById(R.id.etAddress);
        etZipcode = (TextInputLayout) findViewById(R.id.etZipcode);
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
}
