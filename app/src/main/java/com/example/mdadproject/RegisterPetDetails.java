package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

//import com.android.volley.Request;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

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
    private Button btnImage;
    private Button btnNext;
    private ProgressDialog pDialog;
    ImageView imgPet;

    public static String pet, name, sex, breed, age, dateofadoption, height, weight, image, username;

    private static String url_create_pet = Login.ipBaseAddress + "/create_petJson.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NAME = "name";
    private static final String TAG_SEX = "sex";
    private static final String TAG_BREED = "breed";
    private static final String TAG_AGE = "age";
    private static final String TAG_DATEOFADOPTION = "dateofadoption";
    private static final String TAG_HEIGHT = "height";
    private static final String TAG_WEIGHT = "weight";
    private static final String TAG_PET = "pet";
    private static final String TAG_USERNAME = "username";

    private Uri imageUri;
    static String urlPhp = "http://vetmdad.atspace.cc/upload-image-to-server.php";
    String timeStamp;
    String imageFileName;
    File file;
    EditText imageName;
    Bitmap fixBitmap;
    String ImageTag = "image_tag";
    String ImageName = "image_data";
    ProgressDialog progressDialog;
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] byteArray;
    String ConvertImage;
    String GetImageNameFromEditText;
    HttpURLConnection httpURLConnection;
    URL url;
    OutputStream outputStream;
    BufferedWriter bufferedWriter;
    int RC;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    boolean check = true;
    private int GALLERY = 1, CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pet_details);

        Intent intent = getIntent();
        username = intent.getStringExtra(TAG_USERNAME);
        Log.i("nric", username);

        Log.i("Ip address CREATE ", url_create_pet);

        textView5 = (TextView) findViewById(R.id.textView5);
        textView = (TextView) findViewById(R.id.textView);
        txtName = (EditText) findViewById(R.id.txtName);
        textView2 = (TextView) findViewById(R.id.textView2);
        final ToggleButton btnSex = (ToggleButton) findViewById(R.id.btnSex);
        textView3 = (TextView) findViewById(R.id.textView3);
        txtBreed = (EditText) findViewById(R.id.txtLastName);
        textView4 = (TextView) findViewById(R.id.textView4);
        txtAge = (EditText) findViewById(R.id.txtAge);
        textView6 = (TextView) findViewById(R.id.textView6);
        txtAdoptionDate = (DatePicker) findViewById(R.id.txtAdoptionDate);
        textView7 = (TextView) findViewById(R.id.textView7);
        txtHeight = (EditText) findViewById(R.id.txtHeight);
        textView8 = (TextView) findViewById(R.id.textView8);
        txtWeight = (EditText) findViewById(R.id.txtWeight);
        textView9 = (TextView) findViewById(R.id.textView9);
        btnImage = (Button) findViewById(R.id.btnImage);
        btnNext = (Button) findViewById(R.id.btnNext);
        imageName = (EditText) findViewById(R.id.txtImage);
        imgPet = (ImageView) findViewById(R.id.imgPet);

        imageName.setEnabled(false);

        getSupportActionBar().hide();

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                getFileUri();
//                i.putExtra(MediaStore.EXTRA_OUTPUT,file_uri);
//                startActivityForResult(i, 10);
                byteArrayOutputStream = new ByteArrayOutputStream();
                showPictureDialog();
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

                pet = RegisterChoosePet.pet;
                name = txtName.getText().toString().toUpperCase();
                sex = btnSex.getText().toString().toUpperCase();
                breed = txtBreed.getText().toString().toUpperCase();
                age = txtAge.getText().toString().toUpperCase();

                //get date from datepicker
                int day = txtAdoptionDate.getDayOfMonth();
                int month = txtAdoptionDate.getMonth();
                int year = txtAdoptionDate.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                dateofadoption = sdf.format(calendar.getTime());
                //

                height = txtHeight.getText().toString().toUpperCase();
                weight = txtWeight.getText().toString().toUpperCase();
                image = "image";

                fixBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byteArray = byteArrayOutputStream.toByteArray();
                ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                pDialog = new ProgressDialog(RegisterPetDetails.this);
                pDialog.setMessage("Welcome to the bark side..");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();

                GetImageNameFromEditText = imageName.getText().toString();

                Log.i("name", RegisterChoosePet.pet);
                Log.i("name", name);
                Log.i("name", sex);
                Log.i("name", breed);
                Log.i("name", age);
                Log.i("name", dateofadoption);
                Log.i("name", height);
                Log.i("name", weight);
                Log.i("name", username);
                Log.i("name", GetImageNameFromEditText);
                Log.i("name", ConvertImage);

                UploadImageToServer();
//                ImageProcessClass imageProcessClass = new ImageProcessClass();
//                HashMap<String, String> HashMapParams = new HashMap<String, String>();
//                HashMapParams.put(ImageTag, GetImageNameFromEditText);
//                HashMapParams.put(ImageName, ConvertImage);
//                String FinalData = imageProcessClass.ImageHttpRequest(urlPhp, HashMapParams);

//                upload();
//                JSONObject dataJson = new JSONObject();
//                try {
//                    dataJson.put(TAG_NAME, name);
//                    dataJson.put(TAG_SEX, sex);
//                    dataJson.put(TAG_BREED, breed);
//                    dataJson.put(TAG_AGE, age);
//                    dataJson.put(TAG_DATEOFADOPTION, dateofadoption);
//                    dataJson.put(TAG_HEIGHT, height);
//                    dataJson.put(TAG_WEIGHT, weight);
//                    dataJson.put(TAG_PET, pet);
//                    dataJson.put(TAG_USERNAME, username);
//                    dataJson.put(ImageTag, GetImageNameFromEditText);
//                    dataJson.put(ImageName, ConvertImage);
//
//                } catch (JSONException e) {
//
//                }
//
//                postData(urlPhp, dataJson, 1);
            }
        });
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Photo Gallery",
                "Camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {

//        UploadImageOnServerButton.setVisibility(View.VISIBLE);

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //  cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp;
        name = txtName.getText().toString().toUpperCase();
        imageName.setText(imageFileName);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    fixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    // String path = saveImage(bitmap);
                    //Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imgPet.setImageBitmap(fixBitmap);
//                    UploadImageOnServerButton.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterPetDetails.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA) {
            fixBitmap = (Bitmap) data.getExtras().get("data");
            imgPet.setImageBitmap(fixBitmap);
        }
    }

    public void postData(String url, final JSONObject json, final int option) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest json_obj_req = new JsonObjectRequest(
                Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                switch (option) {
                    case 1:
                        checkResponseCreate_Pet(response);
                        break;

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

    public void checkResponseCreate_Pet(JSONObject response) {
        Log.i("----Response", response + " ");
        try {
            if (response.getInt(TAG_SUCCESS) == 1) {
                finish();
                Intent i = new Intent(this, Login.class);
                startActivity(i);
                // dismiss the dialog once product uupdated
                pDialog.dismiss();

            } else {
                // product with pid not found
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    public void UploadImageToServer() {
        fixBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byteArray = byteArrayOutputStream.toByteArray();
        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                progressDialog = ProgressDialog.show(RegisterPetDetails.this, "Image is Uploading", "Please Wait", false, false);
            }

            @Override
            protected void onPostExecute(String string1) {
                super.onPostExecute(string1);
//                progressDialog.dismiss();
                Intent i = new Intent(RegisterPetDetails.this, Login.class);
                startActivity(i);
                pDialog.dismiss();

//                Toast.makeText(RegisterPetDetails.this, "Face to Face", Toast.LENGTH_LONG).show();
//                UploadImageOnServerButton.setVisibility(View.INVISIBLE);
            }

            @Override
            protected String doInBackground(Void... params) {
                ImageProcessClass imageProcessClass = new ImageProcessClass();
                HashMap<String, String> HashMapParams = new HashMap<String, String>();
                HashMapParams.put(TAG_NAME, name);
                HashMapParams.put(TAG_PET, pet);
                HashMapParams.put(TAG_SEX, sex);
                HashMapParams.put(TAG_BREED, breed);
                HashMapParams.put(TAG_AGE, age);
                HashMapParams.put(TAG_DATEOFADOPTION, dateofadoption);
                HashMapParams.put(TAG_HEIGHT, height);
                HashMapParams.put(TAG_WEIGHT, weight);
                HashMapParams.put(TAG_USERNAME, username);
                HashMapParams.put(ImageTag, GetImageNameFromEditText);
                HashMapParams.put(ImageName, ConvertImage);
                String FinalData = imageProcessClass.ImageHttpRequest(urlPhp, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass {
        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                url = new URL(requestURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(20000);
                httpURLConnection.setConnectTimeout(20000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                outputStream = httpURLConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(
                        new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(bufferedWriterDataFN(PData));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                RC = httpURLConnection.getResponseCode();
                if (RC == HttpsURLConnection.HTTP_OK) {
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    stringBuilder = new StringBuilder();
                    String RC2;
                    while ((RC2 = bufferedReader.readLine()) != null) {
                        stringBuilder.append(RC2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
            stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&");
                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }
            return stringBuilder.toString();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera

            } else {

                Toast.makeText(RegisterPetDetails.this, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();

            }
        }
    }

}
