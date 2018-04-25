package com.telemeal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Bryan on 4/18/2018.
 */

public class OrdersActivity extends AppCompatActivity {
    private RecyclerView ordersList;
    private ArrayList<Order> orders;
    private ArrayList<UploadImage> images;
    DatabaseReference dbOrders, dbImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        orders = new ArrayList<>();
        images = new ArrayList<>();
        ordersList = (RecyclerView) findViewById(R.id.orders);

        dbImages = FirebaseDatabase
                .getInstance()
                .getReference("image");

        dbOrders = FirebaseDatabase
                .getInstance()
                .getReference("order");

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

        dbOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orders.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    Order o = postSnapshot.getValue(Order.class);
                    orders.add(o);

                    OrderAdapter adapter = new OrderAdapter(getApplicationContext(), orders, images);
                    ordersList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    ordersList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
