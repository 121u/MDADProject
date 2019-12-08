package com.example.mdadproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

public class TestListView extends ListActivity {

    ArrayList<HashMap<String, String>> petsList;

    public static String url_pet = Login.ipBaseAddress + "/get_all_petsJson.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PETS = "pets";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_SEX = "sex";
    private static final String TAG_BREED = "breed";
    private static final String TAG_AGE = "age";
    private static final String TAG_DATEOFADOPTION = "dateofadoption";
    private static final String TAG_HEIGHT = "height";
    private static final String TAG_WEIGHT = "weight";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_PET = "pet";
    private static final String TAG_USERNAME = "username";

    JSONArray pets = null;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list_view);

        petsList = new ArrayList<HashMap<String, String>>();

        Intent intent = getIntent();

        username = intent.getStringExtra(TAG_USERNAME);

        JSONObject dataJson = new JSONObject();
        try {
            dataJson.put("username", username);
        } catch (JSONException e) {

        }
        postData(url_pet, dataJson);

        // Get listview from list_items.xml
        ListView lv = getListView();

        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem

                // Starting new intent

                // sending pid to next activity

                // starting new activity and expecting some response back

            }
        });
    }

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

    private void checkResponse(JSONObject response, JSONObject creds){
        try {
            if(response.getInt(TAG_SUCCESS)==1){

                Log.i("oops", "good");
                // products found
                // Getting Array of Products
                pets = response.getJSONArray(TAG_PETS);

                // looping through All Products
                for (int i = 0; i < pets.length(); i++) {
                    JSONObject c = pets.getJSONObject(i);

                    // Storing each json item in variable
                    String id = c.getString(TAG_PID);
                    String name = c.getString(TAG_NAME);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_PID, id);
                    map.put(TAG_NAME, name);

                    // adding HashList to ArrayList
                    petsList.add(map);
                }

                /**
                 * Updating parsed JSON data into ListView
                 * */
                ListAdapter adapter = new SimpleAdapter(
                        TestListView.this, petsList,
                        R.layout.list_item, new String[] { TAG_PID,
                        TAG_NAME},
                        new int[] { R.id.pid, R.id.name});
                // updating listview
                setListAdapter(adapter);

            }
            else{

            }

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

}
