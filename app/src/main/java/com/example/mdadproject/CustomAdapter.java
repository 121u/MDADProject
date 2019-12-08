package com.example.mdadproject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

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

        TextView tvId = (TextView)convertView.findViewById(R.id.pid);
        TextView tvName = (TextView)convertView.findViewById(R.id.txtName);
        ImageView imgPet = (ImageView)convertView.findViewById(R.id.imgPet);

        tvId.setText(tempPet.getPid());
        tvName.setText(tempPet.getName());
        String iconSrc = tempPet.getPet();
        imgPet.setImageResource(mContext.getResources().getIdentifier(iconSrc,"drawable",mContext.getPackageName()));

        return convertView;
    }
}
