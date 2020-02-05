package com.example.mdadproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.mdadproject.Models.Pet;
import com.example.mdadproject.PetAppointments;
import com.example.mdadproject.PetDetails;
import com.example.mdadproject.R;
import com.example.mdadproject.UserPets;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PetListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Pet> pets = new ArrayList<>();
    Menu menu;

    public PetListAdapter(Context context, ArrayList<Pet> pets) {
        mContext = context;
        this.pets = pets;
    }

    @Override
    public int getCount() {
        return pets.size();
    }

    @Override
    public Object getItem(int position) {
        return pets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.row2, parent,
                    false);
        }

        final Pet tempPet = (Pet) getItem(position);

        TextView tvId = (TextView) convertView.findViewById(R.id.id);
        TextView tvName = (TextView) convertView.findViewById(R.id.txtName);
        TextView tvBreed = (TextView) convertView.findViewById(R.id.txtBreed);
        ImageView imgPet = (CircleImageView) convertView.findViewById(R.id.imgPet);
        ImageView imgDots = (ImageView) convertView.findViewById(R.id.imgDots);
//        Button btnDetails = (Button) convertView.findViewById(R.id.btnDetails);
//        Button btnAppointments = (Button) convertView.findViewById(R.id.btnAppointments);

        String url = tempPet.getImagepath();

        if (url.isEmpty()) {
            String uri = "drawable/" + tempPet.getPet();
            int imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());

            imgPet.setImageResource(imageResource);
        } else {
            Picasso.get().load(url).resize(50, 50).centerCrop().into(imgPet);
        }

        tvId.setText(tempPet.getPid());
        tvName.setText(tempPet.getName());
        tvBreed.setText(tempPet.getBreed());

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
                            popup.getMenu().findItem(R.id.menu_profile).setVisible(false);
                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.menu_details:
                                            if (mContext instanceof UserPets) {
                                                Intent in = new Intent(mContext, PetDetails.class);
                                                in.putExtra("pid", tempPet.getPid());
                                                in.putExtra("username", tempPet.getUsername());
                                                in.putExtra("qr", ((UserPets) mContext).qr);
                                                mContext.startActivity(in);
                                            }
                                            break;
                                        case R.id.menu_apt:
                                            if (mContext instanceof UserPets) {
                                                Intent in = new Intent(mContext, PetAppointments.class);
                                                in.putExtra("pid", tempPet.getPid());
                                                in.putExtra("username", tempPet.getUsername());
                                                in.putExtra("qr", ((UserPets) mContext).qr);
                                                in.putExtra("name", tempPet.getName());
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
