package com.telemeal;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    listFoodFragment foodList;
    itemCartFragment itemCart;
    DatabaseReference dbImages;
    ArrayList<UploadImage> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        dbImages = FirebaseDatabase
                .getInstance()
                .getReference("image");
        
        images = new ArrayList<>();
        dbImages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                images.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    UploadImage image = postSnapshot.getValue(UploadImage.class);
                    images.add(image);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        foodList = new listFoodFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.listFood,foodList, "list")
                .commit();

        itemCart = new itemCartFragment();
        manager.beginTransaction()
                .replace(R.id.itemCart,itemCart, "cart")
                .commit();
    }

    public itemCartFragment getItemCartFrag()
    {
        return itemCart;
    }

    public ArrayList<UploadImage> getImages() { return images; }

}
