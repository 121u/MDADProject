package com.example.mdadproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class StaffQrScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    static final Integer CAMERA = 0x1;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing_scanner);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
        askForPermission(Manifest.permission.CAMERA, CAMERA);

        getSupportActionBar().hide();
    }

    private void askForPermission(String permission, Integer requestCode) {

        if (ContextCompat.checkSelfPermission(StaffQrScanner.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(StaffQrScanner.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(StaffQrScanner.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(StaffQrScanner.this, new String[]{permission}, requestCode);
            }
        } else {

            // Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Camera
                case 1:
                    mScannerView.setResultHandler(this);
                    mScannerView.startCamera();
                    break;


            }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

        Toast.makeText(this, "Contents = " + result.getText() +
                ", Format = " + result.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this,UserPets.class);
        i.putExtra("qr",result.getText());
        i.putExtra("username", username);
        startActivity(i);

        // * Wait 3 seconds to resume the preview.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(StaffQrScanner.this);
            }
        }, 3000);

    }
}
