package com.example.mdadproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

public class UserBookAppointment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private TextView txtGreeting;
    private TextView txtUserN;

    JSONObject json = null;
    String username;

    public static String url_owner = Login.ipBaseAddress + "/get_owner_detailsJson.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_OWNERS = "owner";
    private static final String TAG_OWNERID = "ownerid";
    private static final String TAG_NRIC = "nric";
    private static final String TAG_FIRSTNAME = "firstname";
    private static final String TAG_LASTNAME = "lastname";
    private static final String TAG_TELEPHONE = "telephone";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_ZIPCODE = "zipcode";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PASSWORD = "password";

    private static  String ownNric = "";
    private static  String ownFirstName = "";
    private static  String ownLastName = "";
    private static  String ownTel = "";
    private static  String ownEmail = "";
    private static  String ownAddress = "";
    private static  String ownZipcode = "";
    private static  String ownUsername = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_appointment);

        setTitle("");

        Intent intent = getIntent();
        username = intent.getStringExtra(TAG_USERNAME);

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new AppointmentFragment()).commit();
//            navigationView.setCheckedItem(R.id.nav_appointment);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.nav_appointment:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new AppointmentFragment()).commit();
                break;
            case R.id.nav_profile:
                Log.i("page1",username);
                intent = new Intent(getApplicationContext(), UserProfile.class);
                intent.putExtra(TAG_USERNAME,username);
                break;
            case R.id.nav_pet:
                intent = new Intent(getApplicationContext(), UserPets.class);
                intent.putExtra(TAG_USERNAME,username);
                break;
            case R.id.nav_qr:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new QRCodeFragment()).commit();
                break;
        }
        startActivityForResult(intent,100);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
