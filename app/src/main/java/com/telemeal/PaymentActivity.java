package com.telemeal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    /**
     * Blank activity for use with the PayPal payment process
     * @param savedInstanceState Save the state of the the activity at the time of payment
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
    }
}
