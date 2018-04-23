package com.telemeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jcarrillo on 4/18/18.
 */

public class ConfirmOrderActivity extends AppCompatActivity {
    ArrayList<CartItem> cartItems;
    ArrayList<UploadImage> cartImages;
    String tax, total, subtotal;
    TextView finalTotal, finalSubtotal, finalTax;
    Button paypal, cash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        setTitle("Confirm Your Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Bundle b = getIntent().getExtras();
        cartItems = b.getParcelableArrayList("cartItems");
        cartImages = b.getParcelableArrayList("images");
        tax = b.getString("tax");
        total = b.getString("total");
        subtotal = b.getString("subtotal");

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
                i.putExtras(b);
                startActivity(i);
            }
        });

        RecyclerView orderView = (RecyclerView) findViewById(R.id.order_items);
        ConfirmOrderAdapter cartOrderAdapter = new ConfirmOrderAdapter(this, cartItems, cartImages, tax);
        orderView.setLayoutManager(new LinearLayoutManager(this));
        orderView.setAdapter(cartOrderAdapter);
    }

}
