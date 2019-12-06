package com.example.mdadproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private TextView textView5;
    private TextView textView;
    private EditText txtNric;
    private TextView textView2;
    private EditText txtFirstName;
    private TextView textView3;
    private EditText txtLastName;
    private TextView textView4;
    private EditText txtTel;
    private TextView textView6;
    private EditText txtEmail;
    private TextView textView7;
    private EditText txtAddress;
    private TextView textView8;
    private EditText txtZipcode;

    JSONObject json = null;

    String username;

    String url_owner = Login.ipBaseAddress + "/get_owner_detailsJson.php";

    private final String TAG_SUCCESS = "success";
    private final String TAG_OWNERS = "owner";
    private final String TAG_OWNERID = "ownerid";
    private final String TAG_NRIC = "nric";
    private final String TAG_FIRSTNAME = "firstname";
    private final String TAG_LASTNAME = "lastname";
    private final String TAG_TELEPHONE = "telephone";
    private final String TAG_EMAIL = "email";
    private final String TAG_ADDRESS = "address";
    private final String TAG_ZIPCODE = "zipcode";
    private final String TAG_USERNAME = "username";
    private final String TAG_PASSWORD = "password";

    private String ownNric = "";
    private String ownFirstName = "";
    private String ownLastName = "";
    private String ownTel = "";
    private String ownEmail = "";
    private String ownAddress = "";
    private String ownZipcode = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.i("url_owner", url_owner);
        Intent i = getActivity().getIntent();

        username = i.getStringExtra(TAG_USERNAME);

        JSONObject dataJson = new JSONObject();
        try {
            dataJson.put("username", username);
        } catch (JSONException e) {

        }
        postData(url_owner, dataJson);

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView5 = (TextView) getView().findViewById(R.id.textView5);
        textView = (TextView) getView().findViewById(R.id.textView);
        txtNric = (EditText) getView().findViewById(R.id.txtUsername);
        textView2 = (TextView) getView().findViewById(R.id.textView2);
        txtFirstName = (EditText) getView().findViewById(R.id.txtFirstName);
        textView3 = (TextView) getView().findViewById(R.id.textView3);
        txtLastName = (EditText) getView().findViewById(R.id.txtBreed);
        textView4 = (TextView) getView().findViewById(R.id.textView4);
        txtTel = (EditText) getView().findViewById(R.id.txtTel);
        textView6 = (TextView) getView().findViewById(R.id.textView6);
        txtEmail = (EditText) getView().findViewById(R.id.txtEmail);
        textView7 = (TextView) getView().findViewById(R.id.textView7);
        txtAddress = (EditText) getView().findViewById(R.id.txtAddress);
        textView8 = (TextView) getView().findViewById(R.id.textView8);
        txtZipcode = (EditText) getView().findViewById(R.id.txtZipcode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100 means Continue
        //https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html


        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
        }

    }

    public void postData(String url, final JSONObject json){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

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
            if (response.getInt(TAG_SUCCESS) == 1) {

                JSONArray ownerObj = response.getJSONArray(TAG_OWNERS);

                JSONObject owner = ownerObj.getJSONObject(0);
                ownNric = owner.getString(TAG_NRIC);
                ownFirstName = owner.getString(TAG_FIRSTNAME);
                ownLastName = owner.getString(TAG_LASTNAME);
                ownTel = owner.getString(TAG_TELEPHONE);
                ownEmail = owner.getString(TAG_EMAIL);
                ownAddress = owner.getString(TAG_ADDRESS);
                ownZipcode = owner.getString(TAG_ZIPCODE);

                txtNric.setText(ownNric);
                txtFirstName.setText(ownFirstName);
                txtLastName.setText(ownLastName);
                txtTel.setText(ownTel);
                txtEmail.setText(ownEmail);
                txtAddress.setText(ownAddress);
                txtZipcode.setText(ownZipcode);

            } else {
                Log.i("nric","oops");
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }
}
