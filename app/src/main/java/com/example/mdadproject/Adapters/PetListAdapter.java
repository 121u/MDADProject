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

        TextView tvId = (TextView)convertView.findViewById(R.id.id);
        TextView tvName = (TextView)convertView.findViewById(R.id.txtName);
        TextView tvBreed = (TextView)convertView.findViewById(R.id.txtBreed);
        ImageView imgPet = (CircleImageView)convertView.findViewById(R.id.imgPet);
        Button btnDetails = (Button)convertView.findViewById(R.id.btnDetails);
        Button btnAppointments = (Button)convertView.findViewById(R.id.btnAppointments);

        String url = tempPet.getImagepath();
        Picasso.get().load(url).resize(50, 50).centerCrop().into(imgPet);

        tvId.setText(tempPet.getPid());
        tvName.setText(tempPet.getName());
        tvBreed.setText(tempPet.getBreed());
        String iconSrc = tempPet.getPet();

        btnDetails.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof UserPets) {
//                    ((UserPets)mContext).getDetails(tempPet.getPid());
                    Intent in = new Intent(mContext, PetDetails.class);
                    in.putExtra("pid", tempPet.getPid());
                    in.putExtra("username", tempPet.getUsername());
                    in.putExtra("qr", ((UserPets)mContext).qr);
                    mContext.startActivity(in);
                }
            }
        });

        btnAppointments.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof UserPets) {
//                    ((UserPets)mContext).getDetails(tempPet.getPid());
                    Intent in = new Intent(mContext, PetAppointments.class);
                    in.putExtra("pid", tempPet.getPid());
                    in.putExtra("username", tempPet.getUsername());
                    in.putExtra("qr", ((UserPets)mContext).qr);
                    in.putExtra("name",tempPet.getName());
                    mContext.startActivity(in);
                }
            }
        });

        return convertView;
    }
}
