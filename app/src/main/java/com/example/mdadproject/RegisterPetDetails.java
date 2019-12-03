package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class RegisterPetDetails<ToggleButton> extends AppCompatActivity {

    private TextView textView5;
    private TextView textView;
    private EditText txtName;
    private TextView textView2;
    private ToggleButton btnGender;
    private TextView textView3;
    private EditText txtLastName;
    private TextView textView4;
    private EditText txtAge;
    private TextView textView6;
    private DatePicker txtAdoptionDate;
    private TextView textView7;
    private EditText txtHeight;
    private TextView textView8;
    private EditText txtWeight;
    private TextView textView9;
    private ImageButton btnImage;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pet_details);

        textView5 = (TextView)findViewById( R.id.textView5 );
        textView = (TextView)findViewById( R.id.textView );
        txtName = (EditText)findViewById( R.id.txtName );
        textView2 = (TextView)findViewById( R.id.textView2 );
        btnGender = (ToggleButton)findViewById( R.id.btnGender );
        textView3 = (TextView)findViewById( R.id.textView3 );
        txtLastName = (EditText)findViewById( R.id.txtLastName );
        textView4 = (TextView)findViewById( R.id.textView4 );
        txtAge = (EditText)findViewById( R.id.txtAge );
        textView6 = (TextView)findViewById( R.id.textView6 );
        txtAdoptionDate = (DatePicker)findViewById( R.id.txtAdoptionDate );
        textView7 = (TextView)findViewById( R.id.textView7 );
        txtHeight = (EditText)findViewById( R.id.txtHeight );
        textView8 = (TextView)findViewById( R.id.textView8 );
        txtWeight = (EditText)findViewById( R.id.txtWeight );
        textView9 = (TextView)findViewById( R.id.textView9 );
        btnImage = (ImageButton)findViewById( R.id.btnImage );
        btnNext = (Button)findViewById( R.id.btnNext );

        getSupportActionBar().hide();

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(i);
            }
        });
    }
}
