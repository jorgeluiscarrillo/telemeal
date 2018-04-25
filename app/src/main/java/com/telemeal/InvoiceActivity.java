package com.telemeal;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class InvoiceActivity extends AppCompatActivity {

    private DatabaseReference dbInvoice;
    private RecyclerView rcv_InvoiceView;
    private EditText et_orderId;
    private EditText et_min_date;
    private EditText et_max_date;
    private EditText et_min_total;
    private EditText et_max_total;
    private Button btn_search;
    private ArrayList<Order> orderList;
    private InvoiceAdapter adapter;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        initialize();
        setEventhandler();
    }

    private void initialize(){
        dbInvoice = FirebaseDatabase.getInstance().getReference("order");

        rcv_InvoiceView = (RecyclerView) findViewById(R.id.vi_rcv_invoice);

        et_orderId = (EditText) findViewById(R.id.vi_et_findbyid);
        et_min_date = (EditText) findViewById(R.id.vi_et_minDate);
        et_max_date = (EditText) findViewById(R.id.vi_et_maxDate);
        et_min_total = (EditText) findViewById(R.id.vi_et_minAmount);
        et_max_total = (EditText) findViewById(R.id.vi_et_maxAmount);

        btn_search = (Button) findViewById(R.id.vi_btn_search);

        dateFormatter = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
        dateFormatter.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
    }

    private void setEventhandler(){
        dbInvoice.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList = new ArrayList<Order>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Order o = postSnapshot.getValue(Order.class);
                    orderList.add(o);
                }
                setAdapter(orderList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = et_orderId.getText().toString().trim();

                if(id.length()!=0){
                    searchByID(Integer.parseInt(id));
                }
                else{

                }
            }
        });

        et_min_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DatePickerDialog dpd = new DatePickerDialog();
            }
        });
    }

    private void setAdapter(ArrayList<Order> orderToShow){
        adapter = new InvoiceAdapter(InvoiceActivity.this, orderToShow);
        rcv_InvoiceView.setLayoutManager(new LinearLayoutManager(InvoiceActivity.this));
        rcv_InvoiceView.setAdapter(adapter);
    }

    private void searchByID(int orderID){
        ArrayList<Order> newList = new ArrayList<Order>();
        for(Order o : orderList){
            if(o.getOrderID() == orderID)
                newList.add(o);
        }
        setAdapter(newList);
    }
}
