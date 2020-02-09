package com.example.mdadproject;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class StaffQrScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    static final Integer CAMERA = 0x1;
    String username, qr;
    int qno = 0;
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";
    TimeZone timeZone1 = TimeZone.getTimeZone("Asia/Singapore");

    private static final String url_queue = UserLogin.ipBaseAddress + "/create_queueNoJson.php";
    private static final String url_update = UserLogin.ipBaseAddress + "/update_apt_statusJson.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_QUEUE = "queue";
    private static final String TAG_QUEUE_NO = "queue_no";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_QUEUE_DATE = "queue_date";
    private static final String TAG_QUEUE_TIME = "queue_time";

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_qr_scanner);

        mRequestQue = Volley.newRequestQueue(this);

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
                Toast.makeText(this, qr + " has been added to queue!", Toast.LENGTH_SHORT).show();
                sendNotification();
                Intent i = new Intent(this,UserQueue.class);
                i.putExtra("qr",qr);
                i.putExtra("username", username);
                startActivity(i);
                finish();
//                Intent i = new Intent(this, UserLogin.class);
//                startActivity(i);
            } else {
                // product with pid not found
                Toast.makeText(this, "This owner is already here!", Toast.LENGTH_SHORT).show();
                SharedPreferences countPref = getSharedPreferences("Counter", MODE_PRIVATE);
                SharedPreferences.Editor editor = countPref.edit();
                int defaultValue = countPref .getInt("count_key",count);
                count = defaultValue-1;  // change this line
                editor.putInt("count_value",defaultValue).commit();
                editor.putInt("count_key",count).commit();
                count = countPref.getInt("count_key",count);
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void handleResult(Result result) {

        SharedPreferences countPref = getSharedPreferences("Counter", MODE_PRIVATE);
        SharedPreferences.Editor editor = countPref.edit();
        int defaultValue = countPref .getInt("count_key",count);
        count = defaultValue+1;  // change this line
        editor.putInt("count_value",defaultValue).commit();
        if(count == 1)
        {
            count = 1;
            editor.putInt("count_key",count).commit();

        }
        else
        {
            editor.putInt("count_key",count).commit();
            count = countPref.getInt("count_key",count);
        }

        Log.i("kappa", "" + count);

        Constants.qNum++;
        qr = result.getText();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sdf.setTimeZone(timeZone1);
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        sdf2.setTimeZone(timeZone1);

        String currentDate = sdf.format(new Date());
        String currentTime = sdf2.format(new Date());
//        Log.i("kappa", "" + Constants.qNum);

        JSONObject dataJson = new JSONObject();
        try {
            dataJson.put(TAG_QUEUE_NO, count);
            dataJson.put(TAG_USERNAME, qr);
            dataJson.put(TAG_QUEUE_DATE, currentDate);
            dataJson.put(TAG_QUEUE_TIME, currentTime);
        } catch (JSONException e) {

        }
        postData(url_queue, dataJson, 1);

        JSONObject dataJson2 = new JSONObject();
        try {
            dataJson2.put(TAG_USERNAME, qr);
            dataJson2.put(TAG_QUEUE_DATE, currentDate);
        } catch (JSONException e) {

        }
        postData(url_update, dataJson2, 0);
//        Toast.makeText(this, "Contents = " + result.getText() + ", Format = " + result.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        // * Wait 3 seconds to resume the preview.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(StaffQrScanner.this);
            }
        }, 3000);

    }

    private void sendNotification() {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+ qr);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","Welcome to MDAD Vet!");
            notificationObj.put("body","Your queue number is " + Constants.qNum + "!");

//            JSONObject extraData = new JSONObject();
//            extraData.put("brandId","puma");
//            extraData.put("category","Shoes");

            json.put("notification",notificationObj);
//            json.put("data",extraData);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AIzaSyDVWUL1u4Ma98vKGNtlrO4d4_9QHlPTSq8");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }
}
