package com.example.mdadproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.mdadproject.Models.Owners;
import com.example.mdadproject.Models.Pet;
import com.example.mdadproject.PetAppointments;
import com.example.mdadproject.PetDetails;
import com.example.mdadproject.R;
import com.example.mdadproject.StaffAllOwners;
import com.example.mdadproject.UserDetails;
import com.example.mdadproject.UserPets;


import java.util.ArrayList;

public class OwnerListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Owners> owners = new ArrayList<>();

    public OwnerListAdapter(Context context, ArrayList<Owners> owner) {
        mContext = context;
        this.owners = owner;
    }

    @Override
    public int getCount() {
        return  owners.size();
    }

    @Override
    public Object getItem(int position) {
        return owners.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.row6, parent,
                    false);
        }
        final Owners owner = (Owners) getItem(position);

        TextView txtOwnerUsername = (TextView) convertView.findViewById(R.id.txtOwnerUsername);
        TextView txtOwnerName = (TextView) convertView.findViewById(R.id.txtOwnerName);
        ImageView imgDots = (ImageView) convertView.findViewById(R.id.imgDots);

        txtOwnerName.setText(owner.getName());
        txtOwnerUsername.setText(owner.getUsername());

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
                            popup.getMenu().findItem(R.id.menu_apt).setVisible(false);
                            popup.getMenu().findItem(R.id.menu_details).setVisible(false);

                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.menu_profile:
                                            if (mContext instanceof StaffAllOwners) {
                                                Intent in = new Intent(mContext, UserDetails.class);
                                                in.putExtra("owner_username", owner.getUsername());

                                                mContext.startActivity(in);
                                            }
                                            break;
                                        case R.id.menu_pets:
                                            if (mContext instanceof StaffAllOwners) {
                                                Intent in = new Intent(mContext, UserPets.class);
                                                in.putExtra("owner_username", owner.getUsername());

                                                mContext.startActivity(in);
                                            }
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
