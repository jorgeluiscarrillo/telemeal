package com.telemeal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Bryan on 4/18/2018.
 */

public class OrdersActivity extends AppCompatActivity {
    private RecyclerView ordersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        ordersList = (RecyclerView) findViewById(R.id.orders);
    }
}
