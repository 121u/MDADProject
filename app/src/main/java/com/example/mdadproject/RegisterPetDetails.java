package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegisterPetDetails extends AppCompatActivity {

    private TextView textView5;
    private TextView textView;
    private EditText txtName;
    private TextView textView2;
    private ToggleButton btnSex;
    private TextView textView3;
    private EditText txtBreed;
    private TextView textView4;
    private EditText txtAge;
    private TextView textView6;
    private DatePicker txtAdoptionDate;
    private TextView textView7;
    private EditText txtHeight;
    private TextView textView8;
    private EditText txtWeight;
    private TextView textView9;
    private ImageButton btnImage;
    private Button btnNext;
    private ProgressDialog pDialog;

    public static String name, sex, breed, age, dateofadoption, height, weight, image;

    private static String url_create_owner =Login.ipBaseAddress+"/create_ownerJson.php";
    private static String url_create_pet =Login.ipBaseAddress+"/create_petJson.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_OWNER = "owner";
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

    private static final String TAG_NAME = "name";
    private static final String TAG_SEX = "sex";
    private static final String TAG_BREED = "breed";
    private static final String TAG_AGE = "age";
    private static final String TAG_DATEOFADOPTION = "dateofadoption";
    private static final String TAG_HEIGHT = "height";
    private static final String TAG_WEIGHT = "weight";
    private static final String TAG_PET = "pet";
    private static final String TAG_IMAGE = "image";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pet_details);

        Log.i("Ip address CREATE ", url_create_owner);
        Log.i("Ip address CREATE ", url_create_pet);

        textView5 = (TextView)findViewById( R.id.textView5 );
        textView = (TextView)findViewById( R.id.textView );
        txtName = (EditText)findViewById( R.id.txtName );
        textView2 = (TextView)findViewById( R.id.textView2 );
        final ToggleButton btnSex = (ToggleButton)findViewById( R.id.btnSex);
        textView3 = (TextView)findViewById( R.id.textView3 );
        txtBreed = (EditText)findViewById( R.id.txtBreed);
        textView4 = (TextView)findViewById( R.id.textView4 );
        txtAge = (EditText)findViewById( R.id.txtAge );
        textView6 = (TextView)findViewById( R.id.textView6 );
        txtAdoptionDate = (DatePicker)findViewById( R.id.txtAdoptionDate );
        textView7 = (TextView)findViewById( R.id.textView7 );
        txtHeight = (EditText)findViewById( R.id.txtHeight );
        textView8 = (TextView)findViewById( R.id.textView8 );
        txtWeight = (EditText)findViewById( R.id.txtWeight );
        textView9 = (TextView)findViewById( R.id.textView9 );
        btnImage = (ImageButton)findViewById( R.id.btnImage );
        btnNext = (Button)findViewById( R.id.btnNext );

        getSupportActionBar().hide();

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(i);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(name.isEmpty())
//                {
//                    txtName.setError(getString(R.string.error_field_required));
//                }
//                else if(breed.isEmpty())
//                {
//                    txtBreed.setError(getString(R.string.error_field_required));
//                }
//                else if(age.isEmpty())
//                {
//                    txtAge.setError(getString(R.string.error_field_required));
//                }
//                else if(height.isEmpty())
//                {
//                    txtHeight.setError(getString(R.string.error_field_required));
//                }
//                else if(weight.isEmpty())
//                {
//                    txtWeight.setError(getString(R.string.error_field_required));
//                }
//                else
//                {

//                image = txtZipcode.getText().toString().toUpperCase();
//
//
//                }

                name = txtName.getText().toString().toUpperCase();
                sex = btnSex.getText().toString().toUpperCase();
                breed = txtBreed.getText().toString().toUpperCase();
                age = txtAge.getText().toString().toUpperCase();

                //get date from datepicker
                int day  = txtAdoptionDate.getDayOfMonth();
                int month= txtAdoptionDate.getMonth();
                int year = txtAdoptionDate.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                dateofadoption = sdf.format(calendar.getTime());
                //

                height = txtHeight.getText().toString().toUpperCase();
                weight = txtWeight.getText().toString().toUpperCase();
                image = "image";

                Log.i("name",RegisterChoosePet.pet);
                Log.i("name",name);
                Log.i("name",sex);
                Log.i("name",breed);
                Log.i("name", age);
                Log.i("name",dateofadoption);
                Log.i("name",height);
                Log.i("name",weight);
                Log.i("name",image);

                pDialog = new ProgressDialog(RegisterPetDetails.this);
                pDialog.setMessage("Welcoming you to the bark side..");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();

                JSONObject dataJson = new JSONObject();
                try{
                    dataJson.put(TAG_NRIC, RegisterDetails.nric);
                    dataJson.put(TAG_FIRSTNAME, RegisterDetails.firstName);
                    dataJson.put(TAG_LASTNAME, RegisterDetails.lastName);
                    dataJson.put(TAG_TELEPHONE, RegisterDetails.telephone);
                    dataJson.put(TAG_EMAIL, RegisterDetails.email);
                    dataJson.put(TAG_ADDRESS, RegisterDetails.address);
                    dataJson.put(TAG_ZIPCODE, RegisterDetails.zipcode);
                    dataJson.put(TAG_USERNAME, RegisterUserPass.username);
                    dataJson.put(TAG_PASSWORD, RegisterUserPass.password);

                    dataJson.put(TAG_NAME, name);
                    dataJson.put(TAG_SEX, sex);
                    dataJson.put(TAG_BREED, breed);
                    dataJson.put(TAG_AGE, age);
                    dataJson.put(TAG_DATEOFADOPTION, dateofadoption);
                    dataJson.put(TAG_HEIGHT, sex);
                    dataJson.put(TAG_WEIGHT, weight);
                    dataJson.put(TAG_PET, RegisterChoosePet.pet);
                    dataJson.put(TAG_IMAGE, image);

                }catch(JSONException e){

                }

                postData(url_create_owner,dataJson,1 );
                postData(url_create_pet,dataJson,1 );
            }
        });
    }

    public void postData(String url, final JSONObject json, final int option){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest json_obj_req = new JsonObjectRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                switch (option){
                    case 1:checkResponseCreate_Product(response); break;

                }

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

    public void checkResponseCreate_Product(JSONObject response)
    {
        Log.i("----Response", response+" ");
        try {
            if(response.getInt(TAG_SUCCESS)==1){

                finish();
                Intent i = new Intent(this, UserPets.class);
                startActivity(i);

                // dismiss the dialog once product uupdated
                pDialog.dismiss();

            }else{
                // product with pid not found
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }
}
