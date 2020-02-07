package com.example.mdadproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mdadproject.Adapters.PetListAdapter;
import com.example.mdadproject.Models.Pet;
import com.example.mdadproject.Utils.Constants;
import com.example.mdadproject.Utils.SaveSharedPreference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserPets extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ArrayList<Pet> petsList = new ArrayList();
    JSONArray pets = null;
    ListView listView;
    String username, pid;
    public String owner_username;
    ProgressDialog pDialog;
    FloatingActionButton fab;
    private DrawerLayout drawer;
    private TextView txtGreeting;

    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";

    public static String url_pet = UserLogin.ipBaseAddress + "/get_all_petsJson.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PETS = "pets";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_SEX = "sex";
    private static final String TAG_BREED = "breed";
    private static final String TAG_AGE = "age";
    private static final String TAG_DATEOFADOPTION = "dateofadoption";
    private static final String TAG_HEIGHT = "height";
    private static final String TAG_WEIGHT = "weight";
    private static final String TAG_PET = "pet";
    private static final String TAG_IMAGEPATH = "image_path";
    private static final String TAG_IMAGENAME = "image_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pets);

        mRequestQue = Volley.newRequestQueue(this);

        Log.i("url", url_pet);

        Intent intent = getIntent();
        username = intent.getStringExtra(TAG_USERNAME);

        Intent intent2 = getIntent();
        owner_username = intent2.getStringExtra("owner_username");

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("We're giving it whatevfur we'ge got..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        SpeedDialView sdv = (SpeedDialView) findViewById(R.id.speedDial);
        Toolbar toolbar = findViewById(R.id.toolbar);

        if (SaveSharedPreference.getUserName(UserPets.this).equals("staff")) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Pets for " + owner_username);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.black),
                    PorterDuff.Mode.SRC_ATOP);
            JSONObject dataJson2 = new JSONObject();
            try {
                dataJson2.put(TAG_USERNAME, owner_username);
            } catch (JSONException e) {

            }

            postData(url_pet, dataJson2);

            sdv.setVisibility(View.GONE);
        }
        else {
            setTitle("Your Pets");
            setSupportActionBar(toolbar);
            drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View header = navigationView.getHeaderView(0);
            txtGreeting = (TextView) header.findViewById(R.id.txtGreeting);
            txtGreeting.setText("Hello, " + SaveSharedPreference.getUserName(UserPets.this) + "!");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                    toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            Menu nav_Menu = navigationView.getMenu();
            if (SaveSharedPreference.getUserName(UserPets.this).equals("staff")) {
                nav_Menu.findItem(R.id.nav_qr_scanner).setVisible(true);
                nav_Menu.findItem(R.id.nav_profile).setVisible(false);
                nav_Menu.findItem(R.id.nav_qr).setVisible(false);
            } else {
                nav_Menu.findItem(R.id.nav_qr_scanner).setVisible(false);
                nav_Menu.findItem(R.id.nav_queue_list).setVisible(false);
                nav_Menu.findItem(R.id.nav_all_owner).setVisible(false);
            }

            JSONObject dataJson = new JSONObject();
            try {
                dataJson.put(TAG_USERNAME, username);
            } catch (JSONException e) {

            }

            postData(url_pet, dataJson);
        }

        listView = (ListView) findViewById(R.id.listView);
        listView.setEnabled(false);

        sdv.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_new_pet, R.drawable.dog)
                .setLabel("new pet")
                .create());
        sdv.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_new_appointment, R.drawable.ic_date_range_black_24dp)
                .setLabel("new appointment")
                .create());
        sdv.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                switch (speedDialActionItem.getId()) {
                    case R.id.fab_new_pet:
                        Intent intent = null;
                        intent = new Intent(getApplicationContext(), PetDetails.class);
                        intent.putExtra(TAG_USERNAME, username);
                        startActivityForResult(intent, 100);
                        drawer.closeDrawer(GravityCompat.START);
                        return false; // true to keep the Speed Dial open
                    case R.id.fab_new_appointment:
                        Intent intent2 = null;
                        intent2 = new Intent(getApplicationContext(), UserBookAppointment.class);
                        intent2.putExtra(TAG_USERNAME, username);
                        startActivityForResult(intent2, 100);
                        drawer.closeDrawer(GravityCompat.START);
                        return false; // true to keep the Speed Dial open
                    default:
                        return false;
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100 means Continue
        //https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void postData(String url, final JSONObject json) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest json_obj_req = new JsonObjectRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                checkResponse(response, json);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(json_obj_req);
    }

    private void checkResponse(JSONObject response, JSONObject creds) {
        try {
            if (response.getInt(TAG_SUCCESS) == 1) {

                pets = response.getJSONArray(TAG_PETS);

                // looping through All Products
                for (int i = 0; i < pets.length(); i++) {
                    JSONObject c = pets.getJSONObject(i);

                    Pet p = new Pet();

                    String pid = c.getString(TAG_PID).toLowerCase();
                    String name = c.getString(TAG_NAME).toLowerCase();
                    String pet = c.getString(TAG_PET).toLowerCase();
                    String sex = c.getString(TAG_SEX).toLowerCase();
                    String breed = c.getString(TAG_BREED).toLowerCase();
                    String age = c.getString(TAG_AGE).toLowerCase();
                    String dateofadoption = c.getString(TAG_DATEOFADOPTION).toLowerCase();
                    String height = c.getString(TAG_HEIGHT).toLowerCase();
                    String weight = c.getString(TAG_WEIGHT).toLowerCase();
                    String imagepath = c.getString(TAG_IMAGEPATH);
                    String imagename = c.getString(TAG_IMAGENAME);

                    p = new Pet(pid, name, pet, sex, breed, age, dateofadoption, height, weight, username, imagepath, imagename);
                    petsList.add(p);
                }

                PetListAdapter myCustomAdapter = new PetListAdapter(UserPets.this, petsList);
                myCustomAdapter.notifyDataSetChanged();

                listView.setAdapter(myCustomAdapter);
                pDialog.dismiss();


            } else {
                pDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.nav_qr_scanner:
                intent = new Intent(getApplicationContext(), StaffQrScanner.class);
                intent.putExtra(TAG_USERNAME, username);
                break;
            case R.id.nav_queue_list:
                intent = new Intent(getApplicationContext(), UserQueue.class);
                intent.putExtra(TAG_USERNAME, username);
                break;
            case R.id.nav_profile:
                intent = new Intent(getApplicationContext(), UserDetails.class);
                intent.putExtra(TAG_USERNAME, username);
                break;
            case R.id.nav_qr:
                intent = new Intent(getApplicationContext(), UserQrCode.class);
                intent.putExtra(TAG_USERNAME, username);
                break;
            case R.id.nav_logout:
                FirebaseMessaging.getInstance().unsubscribeFromTopic(SaveSharedPreference.getUserName(UserPets.this));
                SaveSharedPreference.clearUserName(UserPets.this);
                intent = new Intent(getApplicationContext(), UserLogin.class);
                finish();
                break;
        }
        startActivityForResult(intent, 100);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
