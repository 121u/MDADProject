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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

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

        txtNric.setEnabled(false);
        txtFirstName.setEnabled(false);
        txtLastName.setEnabled(false);
        txtTel.setEnabled(false);
        txtEmail.setEnabled(false);
        txtAddress.setEnabled(false);
        txtZipcode.setEnabled(false);

        txtNric.setText(UserPets.ownNric);
        txtFirstName.setText(UserPets.ownFirstName);
        txtLastName.setText(UserPets.ownLastName);
        txtTel.setText(UserPets.ownTel);
        txtEmail.setText(UserPets.ownEmail);
        txtAddress.setText(UserPets.ownAddress);
        txtZipcode.setText(UserPets.ownZipcode);
    }
}
