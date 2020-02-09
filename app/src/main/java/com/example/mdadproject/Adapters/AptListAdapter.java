package com.example.mdadproject.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.mdadproject.Models.Appointment;
import com.example.mdadproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class AptListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Appointment> appointments = new ArrayList<>();
    TimeZone timeZone1 = TimeZone.getTimeZone("Asia/Singapore");

    public AptListAdapter(Context context, ArrayList<Appointment> appointments) {
        mContext = context;
        this.appointments = appointments;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.row3, parent,
                    false);
        }

        Appointment tempAppointment = (Appointment) getItem(position);

        TextView tvId = (TextView)convertView.findViewById(R.id.id);
        TextView tvDate = (TextView)convertView.findViewById(R.id.txtDate);
        TextView tvTime = (TextView) convertView.findViewById(R.id.txtTime);
        TextView tvStatus = (TextView) convertView.findViewById(R.id.status);

        tvId.setText(tempAppointment.getId());
        tvDate.setText(tempAppointment.getDate());
        tvTime.setText(tempAppointment.getStarttime() + " - " + tempAppointment.getEndtime());
        tvStatus.setText(tempAppointment.getStatus());

        try {
            String date1 = tvDate.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
            sdf.setTimeZone(timeZone1);
            Date parsedDate = sdf.parse(date1);
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            date1 = sdf.format(parsedDate);
            Date date2 = sdf.parse(date1);

            if (tvStatus.getText().toString().equals("completed") || new Date().after(date2)) {
                convertView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.roundcornersgrey));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
