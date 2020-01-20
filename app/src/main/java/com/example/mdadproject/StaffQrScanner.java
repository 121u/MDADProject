package com.example.mdadproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mdadproject.Utils.Constants;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class StaffQrScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    static final Integer CAMERA = 0x1;
    String username, qr;
    int qno = 0;

    private static final String url_queue = UserLogin.ipBaseAddress + "/create_queueNoJson.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_QUEUE = "queue";
    private static final String TAG_QUEUE_NO = "queue_no";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_QUEUE_DATE = "queue_date";
    private static final String TAG_QUEUE_TIME = "queue_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_qr_scanner);

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

    public void postData(String url, final JSONObject json, final int option) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest json_obj_req = new JsonObjectRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                switch (option) {
                    case 1:
                        checkResponseCreate_Queue(response);
                        break;
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(json_obj_req);
    }

    private void checkResponseCreate_Queue(JSONObject response) {
        Log.i("----Response", response + " ");
        try {
            if (response.getInt(TAG_SUCCESS) == 1) {
                finish();
//                Intent i = new Intent(this, UserLogin.class);
//                startActivity(i);
            } else {
                // product with pid not found
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void handleResult(Result result) {
        Constants.qNum++;
        qr = result.getText();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        String currentTime = sdf2.format(new Date());
        Log.i("kappa", "" + Constants.qNum);

        JSONObject dataJson = new JSONObject();
        try {
            dataJson.put(TAG_QUEUE_NO, Constants.qNum);
            dataJson.put(TAG_USERNAME, qr);
            dataJson.put(TAG_QUEUE_DATE, currentDate);
            dataJson.put(TAG_QUEUE_TIME, currentTime);
        } catch (JSONException e) {

        }
        postData(url_queue, dataJson, 1);

        Toast.makeText(this, "Contents = " + result.getText() +
                ", Format = " + result.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this,UserQueue.class);
        i.putExtra("qr",qr);
        i.putExtra("username", username);
        startActivity(i);
        finish();

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
