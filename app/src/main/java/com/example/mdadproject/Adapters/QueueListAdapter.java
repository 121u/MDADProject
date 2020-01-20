package com.example.mdadproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mdadproject.Models.Pet;
import com.example.mdadproject.Models.Queue;
import com.example.mdadproject.PetAppointments;
import com.example.mdadproject.PetDetails;
import com.example.mdadproject.R;
import com.example.mdadproject.UserDetails;
import com.example.mdadproject.UserPets;
import com.example.mdadproject.UserQueue;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class QueueListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Queue> queues = new ArrayList<>();

    public QueueListAdapter(Context context, ArrayList<Queue> queues) {
        mContext = context;
        this.queues = queues;
    }

    @Override
    public int getCount() {
        return queues.size();
    }

    @Override
    public Object getItem(int position) {
        return queues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.row4, parent,
                    false);
        }

        final Queue tempQ = (Queue) getItem(position);

        TextView tvId = (TextView)convertView.findViewById(R.id.id);
        TextView tvName = (TextView)convertView.findViewById(R.id.txtName);
        TextView tvDateTime = (TextView)convertView.findViewById(R.id.txtDateTime);
        TextView tvQueueNo = (TextView)convertView.findViewById(R.id.txtQueueNo);
        Button btnDetails = (Button)convertView.findViewById(R.id.btnDetails);
        Button btnAppointments = (Button)convertView.findViewById(R.id.btnAppointments);

        tvId.setText(tempQ.getQueue_id());
        tvName.setText(tempQ.getUsername());
        tvDateTime.setText(tempQ.getQueue_date() + " - " + tempQ.getQueue_time());
        tvQueueNo.setText(tempQ.getQueue_no());

        btnDetails.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof UserQueue) {
                    Intent in = new Intent(mContext, UserDetails.class);
                    in.putExtra("username", tempQ.getUsername());
                    mContext.startActivity(in);
                }
            }
        });

        btnAppointments.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof UserQueue) {
                    Intent in = new Intent(mContext, UserPets.class);
                    in.putExtra("username", tempQ.getUsername());
                    mContext.startActivity(in);
                }
            }
        });

        return convertView;
    }
}
