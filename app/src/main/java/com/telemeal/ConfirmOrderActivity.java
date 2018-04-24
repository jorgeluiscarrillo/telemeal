package com.telemeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jcarrillo on 4/18/18.
 */

public class ConfirmOrderActivity extends AppCompatActivity {
    ArrayList<CartItem> cartItems;
    String tax, total, subtotal;
    TextView finalTotal, finalSubtotal, finalTax;
    Button paypal, cash;
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        setTitle("Confirm Your Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Bundle b = getIntent().getExtras();
        cartItems = b.getParcelableArrayList("cartItems");
        tax = b.getString("tax");
        total = b.getString("total");
        subtotal = b.getString("subtotal");
        order = b.getParcelable("order");

        Log.d("ORDER PARCEL: ", order.getOrderID() + ", " + order.getFoods().size());

        finalTax = (TextView) this.findViewById(R.id.final_order_tax);
        finalTotal = (TextView) this.findViewById(R.id.final_order_total);
        finalSubtotal = (TextView) this.findViewById(R.id.final_order_subtotal);

        finalTotal.setText(total);
        finalTax.setText(tax);
        finalSubtotal.setText(subtotal);

        paypal = (Button) this.findViewById(R.id.btn_payPayPal);

        paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), PPActivity.class);
                Bundle b = new Bundle();
                String finalTax = tax;
                b.putString("tax", finalTax);
                b.putParcelableArrayList("cartItems", cartItems);
                b.putParcelable("order", order);
                i.putExtras(b);
                startActivity(i);
            }
        });

        RecyclerView orderView = (RecyclerView) findViewById(R.id.order_items);
        ConfirmOrderAdapter cartOrderAdapter = new ConfirmOrderAdapter(this, cartItems, tax);
        orderView.setLayoutManager(new LinearLayoutManager(this));
        orderView.setAdapter(cartOrderAdapter);
    }

}
