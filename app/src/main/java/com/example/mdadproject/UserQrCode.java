package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class UserQrCode extends AppCompatActivity {

    private String LOG_TAG = "GenerateQRCode";
    String username;
    private static final String TAG_USERNAME = "username";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_qr_code);

        Intent intent = getIntent();
        username = intent.getStringExtra(TAG_USERNAME);
        Log.i("oops", username);

        Button button1 = (Button) findViewById(R.id.btnGenQR);
//        button1.setOnClickListener(this);

        EditText qrInput = (EditText) findViewById(R.id.etQrInput);
//        qrInput.setText(username);
        String qrInputText = username;
        Log.v(LOG_TAG, qrInputText);


        Bitmap bitmap = generateQRCode(qrInputText);
        ImageView myImage = (ImageView) findViewById(R.id.imageView1);
        myImage.setImageBitmap(bitmap);
    }

//    public void onClick(View v) {
//
//        switch (v.getId()) {
//            case R.id.btnGenQR:
//
//                break;
//        }
//    }


    @SuppressLint("NewApi") public  Bitmap generateQRCode(String qrInputText)
    {
        //Find screen size
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        Bitmap bitmap=null ;
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3/4;


        //Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrInputText,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);

        try {
            bitmap = qrCodeEncoder.encodeAsBitmap();

        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;

    }


}
