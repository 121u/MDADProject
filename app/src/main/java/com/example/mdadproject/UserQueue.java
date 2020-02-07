package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mdadproject.Adapters.QueueListAdapter;
import com.example.mdadproject.Models.Queue;
import com.example.mdadproject.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class UserQueue extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listView;
    private TextView currentQueueNo;
    private ProgressDialog pDialog;

    public static String url_queue = UserLogin.ipBaseAddress + "/get_all_queueNoJson.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_QUEUE = "queue";
    private static final String TAG_QUEUE_ID = "queue_id";
    private static final String TAG_QUEUE_NO = "queue_no";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_QUEUE_DATE = "queue_date";
    private static final String TAG_QUEUE_TIME = "queue_time";

    ArrayList<Queue> queueList = new ArrayList();
    ArrayList<Queue> todayQueueList = new ArrayList();
    JSONArray queues = null;
    String qr, queueId, queueNo, username, queueDate, queueTime, newFormattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_queue);

        Intent intent = getIntent();
        username = intent.getStringExtra(TAG_USERNAME);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.listView);
        currentQueueNo = (TextView) findViewById(R.id.textView6);


        currentQueueNo.setText("Latest queue number is " + Constants.qNum);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Queue List");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);

        pDialog = new ProgressDialog(UserQueue.this);
        pDialog.setMessage("Loading queue list");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        postData(url_queue, null, 1);
    }

    public void postData(String url, final JSONObject json, final int option) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest json_obj_req = new JsonObjectRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                switch (option) {
                    case 1:
                        checkResponseReadQueue(response);
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

    private void checkResponseReadQueue(JSONObject response) {
        try {
            if (response.getInt(TAG_SUCCESS) == 1) {

                queues = response.getJSONArray(TAG_QUEUE);

                // looping through All Products
                for (int i = 0; i < queues.length(); i++) {
                    JSONObject c = queues.getJSONObject(i);

                    Queue q = new Queue();

                    queueId = c.getString(TAG_QUEUE_ID).toLowerCase();
                    queueNo = c.getString(TAG_QUEUE_NO).toLowerCase();
                    qr = c.getString(TAG_USERNAME).toLowerCase();
                    queueDate = c.getString(TAG_QUEUE_DATE).toLowerCase();
                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date parsedDate = simpleDateFormat2.parse(queueDate);
                        simpleDateFormat2 = new SimpleDateFormat("dd MMMM yyyy");
                        newFormattedDate = simpleDateFormat2.format(parsedDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    queueTime = c.getString(TAG_QUEUE_TIME).toLowerCase();

                    q = new Queue(queueId, queueNo, qr, newFormattedDate, queueTime);
                    queueList.add(q);
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                String currentDate = sdf.format(new Date());
                Log.i("currentDate", currentDate);

                for (Queue a1 : queueList) {
                    if (a1.getQueue_date().equals(currentDate)) {
                        todayQueueList.add(a1);
                    }
                }

                QueueListAdapter myCustomAdapter = new QueueListAdapter(UserQueue.this, todayQueueList);
                listView.setAdapter(myCustomAdapter);
                pDialog.dismiss();
            } else {
//                pDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dot_menu, menu);
        menu.findItem(R.id.menu_apt).setVisible(false);
        menu.findItem(R.id.menu_details).setVisible(false);
        menu.findItem(R.id.menu_notify).setVisible(false);
        menu.findItem(R.id.menu_profile).setVisible(false);
        menu.findItem(R.id.menu_pets).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.reset_queue) {
            Constants.qNum = 0;
            Toast.makeText(this, "Queue number has been reset to " + Constants.qNum, Toast.LENGTH_SHORT).show();
            currentQueueNo.setText("Latest queue number is " + Constants.qNum);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
