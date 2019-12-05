package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterUserPass extends AppCompatActivity {

    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnNext;

    public static String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_pass);

        txtUsername = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById( R.id.txtPassword );
        btnNext = (Button)findViewById(R.id.btnNext);

        getSupportActionBar().hide();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = txtUsername.getText().toString();
                password = txtPassword.getText().toString();

                if(username.isEmpty())
                {
                    txtUsername.setError(getString(R.string.error_field_required));

                }
                else if(password.isEmpty())
                {
                    txtPassword.setError(getString(R.string.error_field_required));

                }
                else
                {
//                    Intent i = new Intent(v.getContext(), RegisterChoosePet.class);
//                    startActivity(i);
                }
                Intent i = new Intent(v.getContext(), RegisterChoosePet.class);
                startActivity(i);
            }
        });
    }
}
