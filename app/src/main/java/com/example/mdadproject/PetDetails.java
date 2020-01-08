package com.example.mdadproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

//import com.android.volley.Request;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class PetDetails extends AppCompatActivity {

    private TextInputLayout etPetType;
    private TextInputLayout etPetName;
    private TextInputLayout etPetSex;
    private TextInputLayout etPetBreed;
    private TextInputLayout etPetAge;
    private TextInputLayout etPetDate;
    private TextInputEditText etPetDateIn;
    private TextInputLayout etPetHeight;
    private TextInputLayout etPetWeight;
    private TextInputLayout etPetImage;
    private ProgressDialog pDialog;
    private CircleImageView imgPet;
    private RelativeLayout btmToolbar;
    private Button btnNext;
    private Button btnUpdate;
    private Button btnDelete;

    final Calendar myCalendar = Calendar.getInstance();

    String[] typeOptions = new String[]{"cat", "dog", "bird", "rabbit", "hamster", "terrapin"};
    String[] sexOptions = new String[]{"female", "male"};

    public static String pid, pet, name, sex, breed, age, dateofadoption, height, weight, username, image_path, image_name, qr;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_SEX = "sex";
    private static final String TAG_BREED = "breed";
    private static final String TAG_AGE = "age";
    private static final String TAG_DATEOFADOPTION = "dateofadoption";
    private static final String TAG_HEIGHT = "height";
    private static final String TAG_WEIGHT = "weight";
    private static final String TAG_PET = "pet";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_IMAGEPATH = "image_path";
    private static final String TAG_IMAGENAME = "image_name";

    public static String url_create_pet = Login.ipBaseAddress + "/create_petJson2.php";
    private static final String url_update_pets = Login.ipBaseAddress + "/update_pet_detailsJson.php";
    private static final String url_delete_pets = Login.ipBaseAddress + "/delete_pets.php";
    public static String url_get_pet = Login.ipBaseAddress + "/get_pet_detailsJson.php";
    String timeStamp;
    String imageFileName;
    Bitmap fixBitmap;
    String ImageTag = "image_tag";
    String ImageName = "image_data";
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

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pet_details);

        etPetType = (TextInputLayout) findViewById(R.id.etPetType);
        etPetType = (TextInputLayout) findViewById(R.id.etPetType);
        etPetName = (TextInputLayout) findViewById(R.id.etPetName);
        etPetSex = (TextInputLayout) findViewById(R.id.etPetSex);
        etPetBreed = (TextInputLayout) findViewById(R.id.etPetBreed);
        etPetAge = (TextInputLayout) findViewById(R.id.etPetAge);
        etPetDate = (TextInputLayout) findViewById(R.id.etPetDate);
        etPetDateIn = (TextInputEditText) findViewById(R.id.etPetDateIn);
        etPetHeight = (TextInputLayout) findViewById(R.id.etPetHeight);
        etPetWeight = (TextInputLayout) findViewById(R.id.etPetWeight);
        etPetImage = (TextInputLayout) findViewById(R.id.etPetImage);
        imgPet = (CircleImageView) findViewById(R.id.imgPet);
        btmToolbar = (RelativeLayout) findViewById(R.id.btmToolbar);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.black),
                    PorterDuff.Mode.SRC_ATOP);
        }

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, sexOptions);
        AutoCompleteTextView editTextFilledExposedDropdown = findViewById(R.id.filled_exposed_dropdown);
        editTextFilledExposedDropdown.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, typeOptions);
        AutoCompleteTextView editTextFilledExposedDropdown2 = findViewById(R.id.filled_exposed_dropdown_2);
        editTextFilledExposedDropdown2.setAdapter(adapter2);

        Intent intent = getIntent();
        pid = intent.getStringExtra(TAG_PID);
        username = intent.getStringExtra(TAG_USERNAME);
        qr = intent.getStringExtra("qr");

        JSONObject dataJson = new JSONObject();
        try {
            dataJson.put(TAG_PID, pid);
        } catch (JSONException e) {

        }

        if (username != null && username.equals("staff") && Constants.IS_STAFF.equals("yes")) {
            btnUpdate.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.GONE);

            postData(url_get_pet, dataJson, 1);
        } else if (username != null && pid != null) {
            btmToolbar.setVisibility(View.GONE);
            etPetType.getEditText().setEnabled(false);
            etPetName.getEditText().setEnabled(false);
            etPetSex.getEditText().setEnabled(false);
            etPetBreed.getEditText().setEnabled(false);
            etPetDate.getEditText().setEnabled(false);
            etPetAge.getEditText().setEnabled(false);
            etPetHeight.getEditText().setEnabled(false);
            etPetWeight.getEditText().setEnabled(false);
            etPetImage.getEditText().setEnabled(false);
            etPetImage.setEndIconVisible(false);

            postData(url_get_pet, dataJson, 1);
        } else {
            btnUpdate.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pet = etPetType.getEditText().getText().toString().toUpperCase();
                name = etPetName.getEditText().getText().toString().toUpperCase();
                sex = etPetSex.getEditText().getText().toString().toUpperCase();
                breed = etPetBreed.getEditText().getText().toString().toUpperCase();
                age = etPetAge.getEditText().getText().toString().toUpperCase();
                dateofadoption = etPetDate.getEditText().getText().toString().toUpperCase();
                height = etPetHeight.getEditText().getText().toString().toUpperCase();
                weight = etPetWeight.getEditText().getText().toString().toUpperCase();

                pDialog = new ProgressDialog(PetDetails.this);
                pDialog.setMessage("Saving details ...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();

                JSONObject dataJson = new JSONObject();
                try {
                    dataJson.put(TAG_PID, pid);
                    dataJson.put(TAG_PET, pet);
                    dataJson.put(TAG_NAME, name);
                    dataJson.put(TAG_SEX, sex);
                    dataJson.put(TAG_BREED, breed);
                    dataJson.put(TAG_AGE, age);
                    dataJson.put(TAG_DATEOFADOPTION, dateofadoption);
                    dataJson.put(TAG_HEIGHT, height);
                    dataJson.put(TAG_WEIGHT, weight);
                    dataJson.put(TAG_USERNAME, qr);

                } catch (JSONException e) {
                }
                postData(url_update_pets, dataJson, 2);

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                pDialog = new ProgressDialog(PetDetails.this);
                pDialog.setMessage("Deleting owner ...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
                // deleting product in background thread
                JSONObject dataJson = new JSONObject();
                try {
                    dataJson.put(TAG_PID, pid);
                } catch (JSONException e) {

                }
                postData(url_delete_pets, dataJson, 3);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pet.isEmpty()) {
                    etPetType.setError(getString(R.string.error_field_required));
                } else if (name.isEmpty()) {
                    etPetName.setError(getString(R.string.error_field_required));
                } else if (sex.isEmpty()) {
                    etPetSex.setError(getString(R.string.error_field_required));
                } else if (breed.isEmpty()) {
                    etPetBreed.setError(getString(R.string.error_field_required));
                } else if (age.isEmpty()) {
                    etPetAge.setError(getString(R.string.error_field_required));
                } else if (dateofadoption.isEmpty()) {
                    etPetDate.setError(getString(R.string.error_field_required));
                } else if (height.isEmpty()) {
                    etPetHeight.setError(getString(R.string.error_field_required));
                } else if (weight.isEmpty()) {
                    etPetWeight.setError(getString(R.string.error_field_required));
                } else if (GetImageNameFromEditText.isEmpty()) {
                    etPetImage.setError(getString(R.string.error_field_required));
                } else {
                    if (mUploadTask != null && mUploadTask.isInProgress()) {
                        Toast.makeText(PetDetails.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                    } else {

                        pet = etPetType.getEditText().getText().toString().toUpperCase();
                        name = etPetName.getEditText().getText().toString().toUpperCase();
                        sex = etPetSex.getEditText().getText().toString().toUpperCase();
                        breed = etPetBreed.getEditText().getText().toString().toUpperCase();
                        age = etPetAge.getEditText().getText().toString().toUpperCase();
                        dateofadoption = etPetDate.getEditText().getText().toString().toUpperCase();
                        height = etPetHeight.getEditText().getText().toString().toUpperCase();
                        weight = etPetWeight.getEditText().getText().toString().toUpperCase();
                        GetImageNameFromEditText = etPetImage.getEditText().getText().toString();

                        uploadFile();
                    }

//                pDialog = new ProgressDialog(PetDetails.this);
//                pDialog.setMessage("Welcome to the bark side..");
//                pDialog.setIndeterminate(false);
//                pDialog.setCancelable(true);
//                pDialog.show();

//                    Log.i("name", pet);
//                    Log.i("name", name);
//                    Log.i("name", sex);
//                    Log.i("name", breed);
//                    Log.i("name", age);
//                    Log.i("name", dateofadoption);
//                    Log.i("name", height);
//                    Log.i("name", weight);
//                    Log.i("name", username);
//                    Log.i("name", GetImageNameFromEditText);
//                    Log.i("name", ConvertImage);

//                checkResponseAddPet();
//                }
                }
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        etPetDateIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new DatePickerDialog(PetDetails.this, R.style.CustomDatePickerDialogTheme, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH)
                        , myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        etPetImage.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                byteArrayOutputStream = new ByteArrayOutputStream();
//                showPictureDialog();
                openFileChooser();
            }
        });
    }

    public void createNewPet(JSONObject response) {
        Log.i("----Response", response + " ");
        try {
            if (response.getInt(TAG_SUCCESS) == 1) {

                finish();
//                Intent i = new Intent(this, AllProductsActivity.class);
//                startActivity(i);

                // dismiss the dialog once product uupdated
//                pDialog.dismiss();

            } else {
                // product with pid not found
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                                    mProgressBar.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(PetDetails.this, "Upload successful", Toast.LENGTH_LONG).show();
                            Upload upload = new Upload(etPetImage.getEditText().getText().toString().trim(),
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                            imageUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    JSONObject dataJson = new JSONObject();
                                    try {
                                        dataJson.put(TAG_NAME, name);
                                        dataJson.put(TAG_PET, pet);
                                        dataJson.put(TAG_SEX, sex);
                                        dataJson.put(TAG_BREED, breed);
                                        dataJson.put(TAG_AGE, age);
                                        dataJson.put(TAG_DATEOFADOPTION, dateofadoption);
                                        dataJson.put(TAG_HEIGHT, height);
                                        dataJson.put(TAG_WEIGHT, weight);
                                        dataJson.put(TAG_USERNAME, username);
                                        dataJson.put(TAG_IMAGEPATH, downloadUrl.toString());
                                        dataJson.put(TAG_IMAGENAME, GetImageNameFromEditText);

                                    } catch (JSONException e) {

                                    }
                                    postData(url_create_pet, dataJson, 4);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PetDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
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
                        checkResponseReadPet(response, json);
                        break;
                    case 2:
                        checkResponseEditPet(response);
                        break;
                    case 3:
                        checkResponseDeletePet(response);
                        break;
                    case 4:
                        createNewPet(response);
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

    private void checkResponseReadPet(JSONObject response, JSONObject creds) {
        try {
            if (response.getInt(TAG_SUCCESS) == 1) {

                JSONArray ownerObj = response.getJSONArray(TAG_PET);

                JSONObject petObj = ownerObj.getJSONObject(0);
                pet = petObj.getString(TAG_PET).toLowerCase();
                name = petObj.getString(TAG_NAME).toLowerCase();
                sex = petObj.getString(TAG_SEX).toLowerCase();
                breed = petObj.getString(TAG_BREED).toLowerCase();
                age = petObj.getString(TAG_AGE).toLowerCase();
                dateofadoption = petObj.getString(TAG_DATEOFADOPTION).toLowerCase();
                height = petObj.getString(TAG_HEIGHT).toLowerCase();
                weight = petObj.getString(TAG_WEIGHT).toLowerCase();
                image_path = petObj.get(TAG_IMAGEPATH).toString();
                image_name = petObj.get(TAG_IMAGENAME).toString();

                etPetType.getEditText().setText(pet);
                etPetName.getEditText().setText(name);
                etPetSex.getEditText().setText(sex);
                etPetBreed.getEditText().setText(breed);
                etPetAge.getEditText().setText(age);
                etPetDate.getEditText().setText(dateofadoption);
                etPetHeight.getEditText().setText(height);
                etPetWeight.getEditText().setText(weight);
                Picasso.get().load(image_path).resize(50, 50).centerCrop().into(imgPet);
                etPetImage.getEditText().setText(image_name);

            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkResponseDeletePet(JSONObject response) {

        try {
            // dismiss the dialog once product updated
            pDialog.dismiss();
            if (response.getInt("success") == 1) {
                // successfully updated
                Intent i = getIntent();
                // send result code 100 to notify about product update
                setResult(100, i);
                finish();

            } else {
                // product with pid not found
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void checkResponseEditPet(JSONObject response) {
        try {
            pDialog.dismiss();
            if (response.getInt("success") == 1) {

                JSONArray petObj = response.getJSONArray(TAG_PET);
                Intent i = getIntent();
                // send result code 100 to notify about product update
                setResult(100, i);
                finish();
//                JSONObject pets = petObj.getJSONObject(0);
//                pet = pets.getString(TAG_PET);
//                name = pets.getString(TAG_NAME);
//                sex = pets.getString(TAG_SEX);
//                breed = pets.getString(TAG_BREED);
//                dateofadoption = pets.getString(TAG_DATEOFADOPTION);
//                height = pets.getString(TAG_HEIGHT);
//                weight = pets.getString(TAG_WEIGHT);
//                GetImageNameFromEditText = pets.getString(ImageTag);

//
//                etPetType.getEditText().setText(pet);
//                etPetName.getEditText().setText(name);
//                etPetSex.getEditText().setText(sex);
//                etPetBreed.getEditText().setText(breed);
//                etPetDate.getEditText().setText(dateofadoption);
//                etPetHeight.getEditText().setText(height);
//                etPetWeight.getEditText().setText(weight);

            } else {
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        etPetDate.getEditText().setText(sdf.format(myCalendar.getTime()));
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
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp;
        etPetImage.getEditText().setText(imageFileName);
//        if (resultCode == this.RESULT_CANCELED) {
//            return;
//        }
//        if (requestCode == GALLERY) {
//            if (data != null) {
//                Uri contentURI = data.getData();
//                try {
//                    fixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                    imgPet.setImageBitmap(fixBitmap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(PetDetails.this, "Failed!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        } else if (requestCode == CAMERA) {
//            fixBitmap = (Bitmap) data.getExtras().get("data");
//            imgPet.setImageBitmap(fixBitmap);
//        }
//        imgPet.setClipToOutline(true);
//        imgPet.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imgPet.setBackgroundResource(R.drawable.roundcorners);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(imgPet);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera

            } else {

                Toast.makeText(PetDetails.this, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();

            }
        }
    }

}
