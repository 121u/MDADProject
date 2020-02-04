package com.example.mdadproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mdadproject.Models.Appointment;
import com.example.mdadproject.PetAppointments;
import com.example.mdadproject.PetDetails;
import com.example.mdadproject.R;
import com.example.mdadproject.StaffAppointments;
import com.example.mdadproject.UserDetails;
import com.example.mdadproject.UserPets;
import com.example.mdadproject.Utils.SaveSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StaffAptListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Appointment> appointments = new ArrayList<>();
    Button btnNotifyAll;
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";

    public StaffAptListAdapter(Context context, ArrayList<Appointment> appointments) {
        mContext = context;
        this.appointments = appointments;
        mRequestQue = Volley.newRequestQueue(this.mContext);
    }

    @Override
    public int getCount() {
        return appointments.size();
    }

    @Override
    public Object getItem(int position) {
        return appointments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.row5, parent,
                    false);
        }

        final Appointment tempAppointment = (Appointment) getItem(position);

        TextView tvId = (TextView)convertView.findViewById(R.id.id);
        TextView tvOwnerPet = (TextView)convertView.findViewById(R.id.txtOwnerPet);
        TextView tvTime = (TextView)convertView.findViewById(R.id.txtTime);
        ImageView imgDots = (ImageView)convertView.findViewById(R.id.imgDots);

        tvId.setText(tempAppointment.getId());
        tvOwnerPet.setText(tempAppointment.getUsername() + " - " + tempAppointment.getPetname());
        tvTime.setText(tempAppointment.getStarttime() + " - " + tempAppointment.getEndtime());
        try {
            imgDots.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.imgDots:
                            PopupMenu popup = new PopupMenu(mContext.getApplicationContext(), v);
                            popup.getMenuInflater().inflate(R.menu.dot_menu, popup.getMenu());
                            popup.getMenu().findItem(R.id.reset_queue).setVisible(false);
                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.menu_profile:
                                            if (mContext instanceof StaffAppointments) {
                                                Intent in = new Intent(mContext, UserDetails.class);
                                                in.putExtra("pid", tempAppointment.getPid());
                                                in.putExtra("owner_username", tempAppointment.getUsername());
                                                in.putExtra("username", SaveSharedPreference.getUserName(mContext.getApplicationContext()));
//                                                in.putExtra("qr", ((UserPets) mContext).qr);
                                                mContext.startActivity(in);
                                            }
                                            break;
                                        case R.id.menu_apt:
                                            if (mContext instanceof StaffAppointments) {
                                                Intent in = new Intent(mContext, PetAppointments.class);
                                                in.putExtra("pid", tempAppointment.getPid());
                                                in.putExtra("username", tempAppointment.getUsername());
//                                                in.putExtra("qr", ((UserPets) mContext).qr);
//                                                in.putExtra("name", tempPet.getName());
                                                mContext.startActivity(in);
                                            }
                                            break;
                                        case R.id.menu_details:
                                            if (mContext instanceof StaffAppointments) {
                                                Intent in = new Intent(mContext, PetDetails.class);
                                                in.putExtra("pid", tempAppointment.getPid());
                                                in.putExtra("username", tempAppointment.getUsername());
//                                                in.putExtra("qr", ((UserPets) mContext).qr);
//                                                in.putExtra("name", tempPet.getName());
                                                mContext.startActivity(in);
                                            }
                                            break;
                                        case R.id.menu_notify:
                                            sendNotification(tempAppointment.getDate(), tempAppointment.getStarttime(), tempAppointment.getPetname(), tempAppointment.getUsername());
                                            break;
                                        default:
                                            break;
                                    }
                                    return true;
                                }
                            });
                            break;
                        default:
                            break;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }

    private void sendNotification(String date, String time, String petname, String username) {  

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+ username);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","Appointment Reminder");
            notificationObj.put("body","You have an appointment on " + date + " at " + time + " for " + petname);

//            JSONObject extraData = new JSONObject();
//            extraData.put("brandId","puma");
//            extraData.put("category","Shoes");

            json.put("notification",notificationObj);
//            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AIzaSyDVWUL1u4Ma98vKGNtlrO4d4_9QHlPTSq8");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }
}
