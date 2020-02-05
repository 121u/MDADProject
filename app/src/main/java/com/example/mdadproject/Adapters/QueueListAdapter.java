package com.example.mdadproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
        ImageView imgDots = (ImageView) convertView.findViewById(R.id.imgDots);

        tvId.setText(tempQ.getQueue_id());
        tvName.setText(tempQ.getUsername());
        tvDateTime.setText(tempQ.getQueue_date() + " - " + tempQ.getQueue_time());
        tvQueueNo.setText(tempQ.getQueue_no());

        try {
            imgDots.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.imgDots:
                            PopupMenu popup = new PopupMenu(mContext.getApplicationContext(), v);
                            popup.getMenuInflater().inflate(R.menu.dot_menu, popup.getMenu());
                            popup.getMenu().findItem(R.id.reset_queue).setVisible(false);
                            popup.getMenu().findItem(R.id.menu_notify).setVisible(false);
                            popup.getMenu().findItem(R.id.menu_details).setVisible(false);
                            popup.getMenu().findItem(R.id.menu_apt).setVisible(false);
                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.menu_profile:
                                            if (mContext instanceof UserQueue) {
                                                Intent in = new Intent(mContext, UserDetails.class);
                                                in.putExtra("username", "staff");
                                                in.putExtra("owner_username", tempQ.getUsername());
                                                mContext.startActivity(in);
                                            }
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
}
