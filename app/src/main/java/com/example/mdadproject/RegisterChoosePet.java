package com.example.mdadproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterChoosePet extends AppCompatActivity {

    public static String pet;
    ListView listView;
    Button btnNext;
    String username = "";
    private static final String TAG_USERNAME = "username";

    String mTitle[] = {"cat", "dog", "bird", "rabbit", "hamster","terrapin"};
    int images[] = {R.drawable.cat, R.drawable.dog, R.drawable.bird, R.drawable.rabbit, R.drawable.hamster, R.drawable.terrapin};
    // so our images and other things are set in array

    // now paste some images in drawable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_choose_pet);

        Intent intent = getIntent();
        username = intent.getStringExtra(TAG_USERNAME);

        Log.i("nric",username);

        listView = (ListView)findViewById(R.id.listView);
        btnNext = (Button)findViewById(R.id.btnNext);

        final MyAdapter adapter = new MyAdapter(this, mTitle, images);
        listView.setAdapter(adapter);

        getSupportActionBar().hide();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(getApplicationContext(), RegisterPetDetails.class);
                intent.putExtra(TAG_USERNAME,username);
                startActivityForResult(intent,100);
            }
        });

        // now set item click on list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                if (position ==  0) {
                    Toast.makeText(RegisterChoosePet.this, "cat", Toast.LENGTH_SHORT).show();
                    pet = "cat";
                }
                if (position ==  1) {
                    Toast.makeText(RegisterChoosePet.this, "dog", Toast.LENGTH_SHORT).show();
                    pet = "dog";
                }
                if (position ==  2) {
                    Toast.makeText(RegisterChoosePet.this, "bird", Toast.LENGTH_SHORT).show();
                    pet = "bird";
                }
                if (position ==  3) {
                    Toast.makeText(RegisterChoosePet.this, "rabbit", Toast.LENGTH_SHORT).show();
                    pet = "rabbit";
                }
                if (position ==  4) {
                    Toast.makeText(RegisterChoosePet.this, "hamster", Toast.LENGTH_SHORT).show();
                    pet = "hamster";
                }
                if (position ==  5) {
                    Toast.makeText(RegisterChoosePet.this, "terrapin", Toast.LENGTH_SHORT).show();
                    pet = "terrapin";
                }
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String rTitle[];
        int rImgs[];

        MyAdapter (Context c, String title[], int imgs[]) {
            super(c, R.layout.row, R.id.txtName, title);
            this.context = c;
            this.rTitle = title;
            this.rImgs = imgs;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.imgPet);
            TextView myTitle = row.findViewById(R.id.txtName);

            // now set our resources on views
            images.setImageResource(rImgs[position]);
            myTitle.setText(rTitle[position]);

            return row;
        }
    }
}
