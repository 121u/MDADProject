package com.example.mdadproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    ImageLoader imageLoader;
    Context mContext;
    ArrayList<Pet> pets = new ArrayList<>();

    public CustomAdapter(Context context, ArrayList<Pet> pets) {
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

        Pet tempPet = (Pet) getItem(position);

        TextView tvId = (TextView)convertView.findViewById(R.id.id);
        TextView tvName = (TextView)convertView.findViewById(R.id.txtName);
        TextView tvBreed = (TextView)convertView.findViewById(R.id.txtBreed);
        ImageView imgPet = (ImageView)convertView.findViewById(R.id.imgPet);
//        NetworkImageView imgPic = (NetworkImageView)convertView.findViewById(R.id.imgPic);

        tvId.setText(tempPet.getPid());
        tvName.setText(tempPet.getName());
        tvBreed.setText(tempPet.getBreed());
        String iconSrc = tempPet.getPet();
//        imgPet.setImageResource(mContext.getResources().getIdentifier(iconSrc,"drawable",mContext.getPackageName()));

        return convertView;
    }
}
