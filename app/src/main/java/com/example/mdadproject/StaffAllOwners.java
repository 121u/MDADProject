package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mdadproject.Adapters.OwnerListAdapter;
import com.example.mdadproject.Adapters.PetListAdapter;
import com.example.mdadproject.Models.Owners;
import com.example.mdadproject.Models.Pet;
import com.example.mdadproject.Models.Queue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.acl.Owner;
import java.util.ArrayList;

import static com.example.mdadproject.PetDetails.pet;

public class StaffAllOwners extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private SearchView searchView;
    private ProgressDialog pDialog;

    public static String url_Allowner = UserLogin.ipBaseAddress + "/get_all_owner.php";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_NAME = "name";
    private static final String TAG_NRIC = "nric";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_MOBILENUMBER = "mobilenumber";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_ZIPCODE = "zipcode";
    private static final String TAG_SECURITY = "security";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_Owners = "owner";

    ArrayList<Owners> owners = new ArrayList();
    JSONArray ownerList = null;
    OwnerListAdapter myCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_all_owners);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.listView);
        searchView = findViewById(R.id.searchView);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("All Owners");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.black),
                    PorterDuff.Mode.SRC_ATOP);
        }

        pDialog = new ProgressDialog(StaffAllOwners.this);
        pDialog.setMessage("Getting owner account");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        postData(url_Allowner, null, 1);

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myCustomAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void postData(String url, final JSONObject json, final int option) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest json_obj_req = new JsonObjectRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                switch (option) {
                    case 1:
                        checkResponseRead(response);
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

    private void checkResponseRead(JSONObject response) {
        try{
            if (response.getInt(TAG_SUCCESS) == 1) {

                ownerList = response.getJSONArray(TAG_Owners);

                for (int i = 0; i < ownerList.length(); i++) {
                    JSONObject c = ownerList.getJSONObject(i);

                    Owners o = new Owners();

                    String nric = c.getString(TAG_NRIC).toLowerCase();
                    String name = c.getString(TAG_NAME).toLowerCase();
                    String mobilenumber = c.getString(TAG_MOBILENUMBER).toLowerCase();
                    String zipcode = c.getString(TAG_ZIPCODE).toLowerCase();
                    String password = c.getString(TAG_PASSWORD).toLowerCase();
                    String address = c.getString(TAG_ADDRESS).toLowerCase();
                    String email = c.getString(TAG_EMAIL).toLowerCase();
                    String username = c.getString(TAG_USERNAME).toLowerCase();
                    String security = c.getString(TAG_SECURITY).toLowerCase();


                    o = new Owners(nric,name,mobilenumber,email,address,zipcode,username,password,security);
                    owners.add(o);
                }

                myCustomAdapter = new OwnerListAdapter(StaffAllOwners.this, owners);
                myCustomAdapter.notifyDataSetChanged();

                listView.setAdapter(myCustomAdapter);
                pDialog.dismiss();

            }else{
                pDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
