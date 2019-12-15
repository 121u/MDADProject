package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;

public class StaffQrScanner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_qr_scanner);
        EditText etQrcode = findViewById(R.id.etQrcode);


        Intent intent = getIntent();
        String qrcode = intent.getStringExtra("qr"); //Receiving the values from previous Page


        if (qrcode!=null)
        {
            etQrcode.setText(qrcode);
        }

        Button scannerBtn = (Button) findViewById(R.id.btn_scanner);

        scannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ZxingScannerActivity.class);
                startActivity(intent);
            }
        });




    }
}