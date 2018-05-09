package com.telemeal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    CheckBox isTakeOut;
    Order order;
    private DatabaseReference dbOrder;
    private DatabaseReference dbinvoice;

    /**
     * Create new activity that serves as a confirmation page
     * @param savedInstanceState Save the state of the activity in a Bundle object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        setTitle("Confirm Your Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Bundle b = getIntent().getExtras();
        cartItems = b.getParcelableArrayList("cartItems");
        cartImages = b.getParcelableArrayList("cartImages");
        tax = b.getString("tax");
        total = b.getString("total");
        subtotal = b.getString("subtotal");
        order = b.getParcelable("order");

        Log.d("ORDER PARCEL: ", order.getOrderID() + ", " + order.getFoods().size());

        finalTax = (TextView) this.findViewById(R.id.final_order_tax);
        finalTotal = (TextView) this.findViewById(R.id.final_order_total);
        finalSubtotal = (TextView) this.findViewById(R.id.final_order_subtotal);

        isTakeOut = (CheckBox) this.findViewById(R.id.take_out_check);

        finalTotal.setText(total);
        finalTax.setText(tax);
        finalSubtotal.setText(subtotal);

        paypal = (Button) this.findViewById(R.id.btn_payPayPal);
        cash = (Button) this.findViewById(R.id.btn_payCash);

        isTakeOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox) view).isChecked();

                if (checked)
                    order.setTakeOut(true);
                else
                    order.setTakeOut(false);
            }
        });

        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setCashPayment(true);
                dbOrder = FirebaseDatabase.getInstance().getReference("order");
                String key = dbOrder.push().getKey();
                dbOrder.child(key).setValue(order);
                dbinvoice = FirebaseDatabase.getInstance().getReference("invoice");
                String inv_key = dbinvoice.push().getKey();
                dbinvoice.child(key).setValue(order);
                AlertDialog alert = new AlertDialog.Builder(ConfirmOrderActivity.this).create();
                alert.setTitle("Pay For Your Order");
                alert.setMessage("Please meet with a cashier to finalize your order.\n" +
                        "Your order number is: " + order.getOrderID());
                alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(ConfirmOrderActivity.this, InitialPage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        });
                alert.setCancelable(false);
                alert.setCanceledOnTouchOutside(false);
                alert.show();

            }
        });

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
        ConfirmOrderAdapter cartOrderAdapter = new ConfirmOrderAdapter(this, cartItems, cartImages, tax);
        orderView.setLayoutManager(new LinearLayoutManager(this));
        orderView.setAdapter(cartOrderAdapter);
    }
}
