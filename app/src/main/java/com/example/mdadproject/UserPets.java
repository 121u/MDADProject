package com.example.mdadproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserPets extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private TextView txtGreeting;
    private TextView txtUserN;

    JSONObject json = null;
    String username;

    public static String url_owner = Login.ipBaseAddress + "/get_owner_detailsJson.php";

    public static final String TAG_SUCCESS = "success";
    public static final String TAG_OWNERS = "owner";
    public static final String TAG_OWNERID = "ownerid";
    public static final String TAG_NRIC = "nric";
    public static final String TAG_FIRSTNAME = "firstname";
    public static final String TAG_LASTNAME = "lastname";
    public static final String TAG_TELEPHONE = "telephone";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_ADDRESS = "address";
    public static final String TAG_ZIPCODE = "zipcode";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_PASSWORD = "password";

    public static  String ownNric = "";
    public static  String ownFirstName = "";
    public static  String ownLastName = "";
    public static  String ownTel = "";
    public static  String ownEmail = "";
    public static  String ownAddress = "";
    public static  String ownZipcode = "";
    public static  String ownUsername = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pets);

        setTitle("");

        Log.i("url_owner", UserPets.url_owner);
        Intent i = getIntent();

        username = i.getStringExtra(UserPets.TAG_USERNAME);

        JSONObject dataJson = new JSONObject();
        try {
            dataJson.put("username", username);
        } catch (JSONException e) {

        }
        postData(UserPets.url_owner, dataJson);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        txtGreeting = (TextView) header.findViewById(R.id.txtGreeting);
        txtUserN = (TextView) header.findViewById(R.id.txtUsern);
        txtGreeting.setText("Hello, " + ownFirstName + "!");
        txtUserN.setText(username);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AppointmentFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_appointment);
        }
    }

    public void postData(String url, final JSONObject json){
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest json_obj_req = new JsonObjectRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                checkResponse(response, json);

//                String alert_message;
//                alert_message = response.toString();

//                showAlertDialogue("Response", alert_message);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

//                String alert_message;
//                alert_message = error.toString();

//                showAlertDialogue("Error", alert_message);

            }

        });

        requestQueue.add(json_obj_req);
    }

    private void checkResponse(JSONObject response, JSONObject creds) {
        try {
            if (response.getInt(UserPets.TAG_SUCCESS) == 1) {

                JSONArray ownerObj = response.getJSONArray(UserPets.TAG_OWNERS);

                JSONObject owner = ownerObj.getJSONObject(0);
                ownNric = owner.getString(TAG_NRIC);
                ownFirstName = owner.getString(TAG_FIRSTNAME);
                ownLastName = owner.getString(TAG_LASTNAME);
                ownTel = owner.getString(TAG_TELEPHONE);
                ownEmail = owner.getString(TAG_EMAIL);
                ownAddress = owner.getString(TAG_ADDRESS);
                ownZipcode = owner.getString(TAG_ZIPCODE);
                ownUsername = owner.getString(TAG_USERNAME);
            } else {
                Log.i("nric","oops");
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_appointment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AppointmentFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
            case R.id.nav_pet:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PetsFragment()).commit();
                break;
            case R.id.nav_qr:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new QRCodeFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
