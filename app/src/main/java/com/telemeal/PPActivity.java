package com.telemeal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class PPActivity extends AppCompatActivity {
    private static final String TAG = "PayPal Activity";
    // Use PayPal's Sandbox Environment for testing purposes
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    // Public PayPal Sandbox API key
    private static final String CONFIG_CLIENT_ID =
            "AeNuxCW33my-q4HPJXQhCLZ-usWQsuNpWVs_4G0Ev-P01JvkPtI138YN3mI8wkdI9nHLbnjxnnRNM96t";

    private static final int REQUEST_CODE_PAYMENT = 1;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            .rememberUser(false)
            .merchantName("Telemeal");

    private static final String CURRENCY = "USD";

    String taxPrice;
    ArrayList<CartItem> cartItems;
    Date currentTime = Calendar.getInstance().getTime();
    Order order;
    private DatabaseReference dbOrder;
    private DatabaseReference dbInvoice;

    /**
     * Destroy the PayPal service once a payment is executed
     */
    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    /**
     * Initialize and create a new Intent for the PayPal service
     * @param savedInstanceState Current state of application at the time of PayPal payment
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        PayPalService.clearAllUserData(this);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        processPayment();
    }

    /**
     * Process a PayPal payment. Get the user's items and price to begin the PayPal payment
     * Get the Parcelable data from the user's cart from a Bundle
     */
    private void processPayment() {
        Bundle b = getIntent().getExtras();
        cartItems = b.getParcelableArrayList("cartItems");
        taxPrice = b.getString("tax");
        order = b.getParcelable("order");

        PayPalPayment orderPayment = getOrder(PayPalPayment.PAYMENT_INTENT_SALE, cartItems);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, orderPayment);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    /**
     * Validate if a PayPal payment succeeded and proceed accordingly
     * @param requestCode Valid PayPal request code
     * @param resultCode Valid PayPal result flag
     * @param data Payment data from the previous activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    PayPalService.clearAllUserData(this);
                    Toast.makeText(this, "Payment complete.", Toast.LENGTH_LONG).show();


                    dbOrder = FirebaseDatabase.getInstance().getReference("order");
                    dbInvoice = FirebaseDatabase.getInstance().getReference("invoice");
                    String key = dbOrder.push().getKey();
                    String inv_key = dbInvoice.push().getKey();
                    dbOrder.child(key).setValue(order);
                    dbInvoice.child(inv_key).setValue(order);

                    Intent i = new Intent(this, InitialPage.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "onActivityResult: User cancelled payment.");
                this.finish();
            }
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i(TAG, "onActivityResult: Invalid payment submitted.");
            this.finish();
        }
    }

    /**
     * Get each food item from user's order and add it to the total
     * Once the total is calculated, execute the payment
     * @param paymentIntent Payment activity data
     * @param cartItems The user's cart items
     * @return the payment data to execute and send to PayPal services
     */
    private PayPalPayment getOrder(String paymentIntent, ArrayList<CartItem> cartItems) {
        PayPalItem[] items = new PayPalItem[cartItems.size()];
        for (int i = 0; i < cartItems.size(); i++) {
            items[i] = new PayPalItem
                    (cartItems.get(i).getName(),
                            cartItems.get(i).getQuantity(),
                            new BigDecimal(String.valueOf(cartItems.get(i).getPrice() / cartItems.get(i).getQuantity())),
                            CURRENCY,
                            cartItems.get(i).getSku());
        }
        BigDecimal subtotal = PayPalItem.getItemTotal(items);
        BigDecimal shipping = new BigDecimal("0.00");
        BigDecimal tax = new BigDecimal(taxPrice);
        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(shipping, subtotal, tax);
        BigDecimal amount = subtotal.add(shipping).add(tax);


        PayPalPayment payment = new PayPalPayment(amount, "USD",
                "Restaurant order made with Telemeal on " + currentTime, paymentIntent);
        payment.isNoShipping();
        payment.items(items).paymentDetails(paymentDetails);

        return payment;
    }

    /**
     * Override the back button pressed to prevent user from tampering with the payment
     */
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
