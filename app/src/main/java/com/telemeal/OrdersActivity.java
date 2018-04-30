package com.telemeal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    private Button clearAllOrders;
    DatabaseReference dbOrders, dbImages;
    private boolean changed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        setTitle("Current Orders");
        orders = new ArrayList<>();
        images = new ArrayList<>();
        ordersList = (RecyclerView) findViewById(R.id.orders);
        clearAllOrders = (Button) findViewById(R.id.btn_clear_all_orders);

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

                    OrderAdapter adapter = new OrderAdapter(OrdersActivity.this, orders, dbOrders);
                    ordersList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    ordersList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        clearAllOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrdersActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Clear All Orders");
                builder.setMessage("Are you sure you want to clear all orders from the database?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                changed = false;
                                dbOrders.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                                            dbOrders.child(d.getKey()).removeValue();
                                        }
                                        changed = true;
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                if(changed)
                                {
                                    Toast.makeText(OrdersActivity.this, "Successfully cleared all orders.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }
}
