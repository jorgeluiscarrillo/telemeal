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
    /**Fragment to handle listing foods and actions around it*/
    listFoodFragment foodList;

    /**Fragment to handle the item cart and actions around it*/
    itemCartFragment itemCart;

    /**Reference to the Google Firebase's Realtime Database that holds the images*/
    DatabaseReference dbImages;

    /**Holds the links to the images in the database*/
    ArrayList<UploadImage> images;

    /**
     * Initializes MenuActivity
     * @param savedInstanceState save the state of the activity in a bundle for other when activity is accessed again later
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        dbImages = FirebaseDatabase
                .getInstance()
                .getReference("image");
        
        images = new ArrayList<>();
        dbImages.addValueEventListener(new ValueEventListener() {
            /**
             * Listens to the database and changes images whenever the image database is affected
             * @param dataSnapshot Holds the reference to the images
             */
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

    /**
     * Gets the item cart fragment that the activity uses to perform actions on it
     * @return The item cart fragment
     */
    public itemCartFragment getItemCartFrag()
    {
        return itemCart;
    }

    /**
     * Gets the array list of images to hold
     * @return The list of images obtained from the database
     */
    public ArrayList<UploadImage> getImages() { return images; }

}
