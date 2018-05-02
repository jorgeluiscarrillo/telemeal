package com.telemeal;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class InvoiceActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference dbInvoice;
    private RecyclerView rcv_InvoiceView;
    private EditText et_orderId;
    private EditText et_min_date;
    private Date min_date;
    private EditText et_max_date;
    private Date max_date;
    private EditText et_min_total;
    private EditText et_max_total;
    private Button btn_search;
    private Map<Integer, Order> orderList;
    private InvoiceAdapter adapter;
    private SimpleDateFormat dateFormatter;
    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        initialize();
        setEventHandler();
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

        dateFormatter = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        dateFormatter.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

        myCalendar = Calendar.getInstance();
        myCalendar.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
    }

    private void setEventHandler(){
        dbInvoice.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList = new HashMap<Integer, Order>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Order o = postSnapshot.getValue(Order.class);
                    orderList.put(o.getOrderID(), o);
                }
                ArrayList<Order> list = new ArrayList<>(orderList.values());
                setAdapter(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rcv_InvoiceView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();
            }
        }));

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = getTextFromEditText(et_orderId);
                Map<Integer, Order> complexQuery = orderList;
                String minDate = getTextFromEditText(et_min_date);
                String maxDate = getTextFromEditText(et_max_date);
                String minTotal = getTextFromEditText(et_min_total);
                String maxTotal = getTextFromEditText(et_max_total);

                if(id.length() != 0 &&
                        (minDate.length() != 0 || maxDate.length() != 0 || minTotal.length() != 0 || maxTotal.length() != 0)){
                    AlertDialog alertDialog = new AlertDialog.Builder(InvoiceActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Search by Order ID and Range cannot be combined. Clear Order ID field or the range fields.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else if(id.length() != 0){
                    searchByID(Integer.parseInt(id));
                }
                else{
                    if(minDate.length() != 0 || maxDate.length() != 0){
                        complexQuery = searchByDate(complexQuery);
                    }
                    if(minTotal.length() != 0 || maxTotal.length() != 0){
                        complexQuery = searchByTotal(complexQuery);
                    }
                    setAdapter((ArrayList<Order>) complexQuery.values());
                }
            }
        });

        et_min_date.setOnClickListener(this);
        et_max_date.setOnClickListener(this);
        et_min_date.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_min_date.setText(null);
                return true;
            }
        });
        et_max_date.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_max_date.setText(null);
                return true;
            }
        });
    }

    private void updateLabel(View field) {
        ((EditText) field).setText(dateFormatter.format(myCalendar.getTime()));
        if(field == et_min_date) {
            min_date = myCalendar.getTime();
        }
        else{
            max_date = myCalendar.getTime();
        }
    }

    private void setAdapter(ArrayList<Order> orderToShow){
        adapter = new InvoiceAdapter(InvoiceActivity.this, orderToShow);
        rcv_InvoiceView.setLayoutManager(new LinearLayoutManager(InvoiceActivity.this));
        rcv_InvoiceView.setAdapter(adapter);
    }

    private void searchByID(int orderID){
        ArrayList<Order> newList = new ArrayList<Order>();
        Order o = orderList.get(orderID);
        newList.add(o);
        setAdapter(newList);
    }

    private Map<Integer, Order> searchByDate(Map<Integer, Order> list){
        String min = getTextFromEditText(et_min_date);
        String max = getTextFromEditText(et_max_date);
        HashMap<Integer, Order> newList = new HashMap<Integer, Order>();
        if(min.length() != 0 && max.length() != 0){
            for(Order o : list.values()){
                if(min_date.before(o.getDate()) && max_date.after(o.getDate()))
                    newList.put(o.getOrderID(), o);
            }
        }
        else if(min.length() != 0 && max.length() == 0)
        {
            for(Order o : list.values()){
                if(min_date.before(o.getDate()))
                    newList.put(o.getOrderID(), o);
            }
        }
        else if(min.length() == 0 && max.length() != 0)
        {
            for(Order o : list.values()){
                if(max_date.after(o.getDate()))
                    newList.put(o.getOrderID(), o);
            }
        }
        else{
            return list;
        }
        return newList;
    }

    private Map<Integer, Order> searchByTotal(Map<Integer, Order> list){
        String min = getTextFromEditText(et_min_total);
        String max = getTextFromEditText(et_max_total);
        double lowerBound;
        double upperBound;
        HashMap<Integer, Order> newList = new HashMap<Integer, Order>();
        if(min.length() != 0 && max.length() != 0){
            lowerBound = Double.parseDouble(min);
            upperBound = Double.parseDouble(max);
            for(Order o : list.values()){
                if(lowerBound <= o.getSubTotal() && o.getSubTotal() <= upperBound)
                    newList.put(o.getOrderID(), o);
            }
        }
        else if(min.length() != 0 && max.length() == 0)
        {
            for(Order o : list.values()){
                lowerBound = Double.parseDouble(min);
                if(lowerBound <= o.getSubTotal())
                    newList.put(o.getOrderID(), o);
            }
        }
        else if(min.length() == 0 && max.length() != 0)
        {
            upperBound = Double.parseDouble(max);
            for(Order o : list.values()){
                if(o.getSubTotal() <= upperBound)
                    newList.put(o.getOrderID(), o);
            }
        }
        else{
            return list;
        }
        return newList;
    }

    @Override
    public void onClick(final View view) {
        DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                TimePickerDialog.OnTimeSetListener tpd = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        myCalendar.set(Calendar.HOUR_OF_DAY, hour);
                        myCalendar.set(Calendar.MINUTE, min);
                        updateLabel(view);
                    }
                };
                new TimePickerDialog(InvoiceActivity.this, tpd,
                        myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        };

        new DatePickerDialog(InvoiceActivity.this, dpd,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private String getTextFromEditText(EditText et){
        return et.getText().toString().trim();
    }
}
