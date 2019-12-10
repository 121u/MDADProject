package com.example.mdadproject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter2 extends BaseAdapter {

    Context mContext;
    ArrayList<Appointment> appointments = new ArrayList<>();

    public CustomAdapter2(Context context, ArrayList<Appointment> appointments) {
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

        tvId.setText(tempAppointment.getPid());
        tvDate.setText(tempAppointment.getDate());
        tvTime.setText(tempAppointment.getStarttime() + " - " + tempAppointment.getEndtime());
        tvStatus.setText(tempAppointment.getStatus());

        if (tvStatus.getText().toString().equals("completed") ) {
            convertView.setBackgroundColor(Color.parseColor("#d3d3d3"));
        }

        return convertView;
    }
}
