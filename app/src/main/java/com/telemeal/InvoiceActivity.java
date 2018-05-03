package com.telemeal;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class InvoiceActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference dbInvoice;
    private DatabaseReference dbImage;
    private RecyclerView rcv_InvoiceView;
    private EditText et_orderId;
    private EditText et_min_date;
    private Date min_date;
    private EditText et_max_date;
    private Date max_date;
    private EditText et_min_total;
    private EditText et_max_total;
    private Button btn_search;
    private ArrayList<Order> orderList;
    private ArrayList<UploadImage> imageList;
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
        dbInvoice = FirebaseDatabase.getInstance().getReference("invoice");
        dbImage = FirebaseDatabase.getInstance().getReference("image");
        imageList = new ArrayList<UploadImage>();

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
                orderList = new ArrayList<Order>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Order o = postSnapshot.getValue(Order.class);
                    orderList.add(o);
                }
                Collections.sort(orderList);
                Collections.reverse(orderList);
                setAdapter(orderList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dbImage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    UploadImage i = postSnapshot.getValue(UploadImage.class);
                    imageList.add(i);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rcv_InvoiceView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                showItemDetail(position);
            }
        }));

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = getTextFromEditText(et_orderId);
                ArrayList<Order> complexQuery = orderList;
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
                    setAdapter(complexQuery);
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

    private void showItemDetail(int position){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.invoice_detail_layout);
        dialog.setTitle("Order Detail");
        dialog.getWindow().setLayout(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        Order o = adapter.getItem(position);
        TextView tv_id = dialog.findViewById(R.id.detail_tv_oid);
        TextView tv_date = dialog.findViewById(R.id.detail_tv_date);
        TextView tv_total = dialog.findViewById(R.id.detail_tv_total);
        RecyclerView rcv_items = dialog.findViewById(R.id.detail_rcv_items);
        Button btn_back = dialog.findViewById(R.id.detail_btn_back);

        tv_id.setText(""+o.getOrderID());
        dateFormatter = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        tv_date.setText(dateFormatter.format(o.getDate()));
        tv_total.setText(""+o.getSubTotal());

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ArrayList<CartItem> cartList = new ArrayList<CartItem>();
        boolean flag = false;

        for(Food f : o.getFoods()){
            CartItem c = new CartItem(1, f.getName(), f.getPrice(), f.getSku());
            if(cartList.isEmpty())
                cartList.add(c);
            else {
                for (CartItem i : cartList) {
                    if(i.getName().equals(c.getName())){
                        i.setQuantity(i.getQuantity()+1);
                        flag = true;
                        break;
                    }
                }
                if(!flag)
                    cartList.add(c);
            }
        }



        ConfirmOrderAdapter cartOrderAdapter = new ConfirmOrderAdapter(dialog.getContext(), cartList, imageList, "10.00");
        rcv_items.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
        rcv_items.setAdapter(cartOrderAdapter);

        dialog.show();
    }

    private void searchByID(int orderID){
        ArrayList<Order> newList = new ArrayList<Order>();
        Order o = orderList.get(orderID);
        newList.add(o);
        setAdapter(newList);
    }

    private ArrayList<Order> searchByDate(ArrayList<Order> list){
        String min = getTextFromEditText(et_min_date);
        String max = getTextFromEditText(et_max_date);
        ArrayList<Order> newList = new ArrayList<Order>();
        if(min.length() != 0 && max.length() != 0){
            for(Order o : list){
                if(min_date.before(o.getDate()) && max_date.after(o.getDate()))
                    newList.add(o);
            }
        }
        else if(min.length() != 0 && max.length() == 0)
        {
            for(Order o : list){
                if(min_date.before(o.getDate()))
                    newList.add(o);
            }
        }
        else if(min.length() == 0 && max.length() != 0)
        {
            for(Order o : list){
                if(max_date.after(o.getDate()))
                    newList.add(o);
            }
        }
        else{
            return list;
        }
        return newList;
    }

    private ArrayList<Order> searchByTotal(ArrayList<Order> list){
        String min = getTextFromEditText(et_min_total);
        String max = getTextFromEditText(et_max_total);
        double lowerBound;
        double upperBound;
        ArrayList<Order> newList = new ArrayList<Order>();
        if(min.length() != 0 && max.length() != 0){
            lowerBound = Double.parseDouble(min);
            upperBound = Double.parseDouble(max);
            for(Order o : list){
                if(lowerBound <= o.getSubTotal() && o.getSubTotal() <= upperBound)
                    newList.add(o);
            }
        }
        else if(min.length() != 0 && max.length() == 0)
        {
            for(Order o : list){
                lowerBound = Double.parseDouble(min);
                if(lowerBound <= o.getSubTotal())
                    newList.add(o);
            }
        }
        else if(min.length() == 0 && max.length() != 0)
        {
            upperBound = Double.parseDouble(max);
            for(Order o : list){
                if(o.getSubTotal() <= upperBound)
                    newList.add(o);
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
