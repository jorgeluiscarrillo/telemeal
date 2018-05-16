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
import java.text.DecimalFormat;
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

    /**
     * Helper methods which initializes all the member variables
     */
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

    /**
     * Helper method which sets all the event controllers
     */
    private void setEventHandler(){
        //Attach real time database listener to Invoice database
        dbInvoice.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //create local memory storage for the invoice
                orderList = new ArrayList<Order>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    //gets the invoice as a order format
                    Order o = postSnapshot.getValue(Order.class);
                    orderList.add(o);
                }
                //sort the invoice in ascending order of date
                Collections.sort(orderList);
                //reverse the order so that newest appears on the top
                Collections.reverse(orderList);
                //attach adapter to format the output
                setAdapter(orderList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //gets all the image metadata into the local memory
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

        //Click listener to see the detail of the order
        rcv_InvoiceView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                showItemDetail(position);
            }
        }));

        //Button click listener for the querying the invoice/order
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gets all the inputs
                String id = getTextFromEditText(et_orderId);
                String minDate = getTextFromEditText(et_min_date);
                String maxDate = getTextFromEditText(et_max_date);
                String minTotal = getTextFromEditText(et_min_total);
                String maxTotal = getTextFromEditText(et_max_total);

                //When user query with date and total amount, complexQuery will be the temporal storage for that transaction
                ArrayList<Order> complexQuery = orderList;

                //Validator: query with id and date/total cannot combine together
                //  because id is the unique key which returns single instance
                //  where date/total gives range of the instances
                if(id.length() != 0 &&
                        (minDate.length() != 0 || maxDate.length() != 0 || minTotal.length() != 0 || maxTotal.length() != 0)){
                    //show the message
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
                    //notice this is not "else if"
                    //which means date and total can be combined on querying
                    if(minTotal.length() != 0 || maxTotal.length() != 0){
                        complexQuery = searchByTotal(complexQuery);
                    }
                    //reset the adapter with queried list
                    setAdapter(complexQuery);
                }
            }
        });

        //Click on date fields opens date/time picker dialog
        et_min_date.setOnClickListener(this);
        et_max_date.setOnClickListener(this);
        //since clicking the date fields will open the dialog,
        //cannot backspace or delete on text field.
        //So, long click event will clear the date specified by the date/time picker dialog
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

    /**
     * Helper method which sets the date text fields with formatted date string
     * @param field
     */
    private void updateLabel(View field) {
        //myCalendar has the information of the date/time set by dialog
        //See OnClick listener in this class
        dateFormatter.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        ((EditText) field).setText(dateFormatter.format(myCalendar.getTime()));
        if(field == et_min_date) {
            min_date = myCalendar.getTime();
        }
        else{
            max_date = myCalendar.getTime();
        }
    }

    /**
     * Helper method which reset the adapter with given list
     * @param orderToShow list to format as output
     */
    private void setAdapter(ArrayList<Order> orderToShow){
        adapter = new InvoiceAdapter(InvoiceActivity.this, orderToShow);
        rcv_InvoiceView.setLayoutManager(new LinearLayoutManager(InvoiceActivity.this));
        rcv_InvoiceView.setAdapter(adapter);
    }

    /**
     * Helper method which show the detail of the order which user clicked from the list
     * @param position the position of the order which user selected (clicked)
     */
    private void showItemDetail(int position){
        //the information of the order will be shown in the dialog instead of new activity or fragment
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.invoice_detail_layout);
        dialog.setTitle("Order Detail");
        dialog.getWindow().setLayout(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);

        //gets the order object from the adapter
        Order o = adapter.getItem(position);

        //instantiate the text fields to show the data
        TextView tv_id = dialog.findViewById(R.id.detail_tv_oid);
        TextView tv_date = dialog.findViewById(R.id.detail_tv_date);
        TextView tv_total = dialog.findViewById(R.id.detail_tv_total);
        RecyclerView rcv_items = dialog.findViewById(R.id.detail_rcv_items);
        Button btn_back = dialog.findViewById(R.id.detail_btn_back);

        //fill the text fields with corresponding data
        tv_id.setText(String.format ("%04d", o.getOrderID()));
        dateFormatter = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        tv_date.setText(dateFormatter.format(o.getDate()));
        tv_total.setText(String.format ("%.2f", o.getSubTotal()));

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //specific food items will be shown in format of CartItem which has quantity, name, price as variables
        ArrayList<CartItem> cartList = new ArrayList<CartItem>();

        //flag will be used for converting Food items into CartItem items
        //it will set true if a food is already converted into the CartItem.
        //  In this case, it will just increment the quantity of existing CartItem
        //it will remain false if a food is not in the list
        //  In this case, it will create and add the new CartItem
        boolean flag;

        //Iterate the Food items in the order
        for(Food f : o.getFoods()){
            //initially add one item if the list is empty
            if(cartList.isEmpty()) {
                CartItem c = new CartItem(1, f.getName(), f.getPrice(), f.getSku());
                cartList.add(c);
            }
            //if the list is not empty
            else {
                //iterate each CartItem items and compare its name to the name of the food
                //flag will determine if the item exists in the list
                flag = false;
                for (CartItem i : cartList) {
                    if(i.getName().equals(f.getName())){
                        i.setQuantity(i.getQuantity()+1);
                        flag = true;
                        break;
                    }
                }
                if(!flag) {
                    CartItem c = new CartItem(1, f.getName(), f.getPrice(), f.getSku());
                    cartList.add(c);
                }
            }
        }

        //Set the adapter of ConfirmOrder format
        ConfirmOrderAdapter cartOrderAdapter = new ConfirmOrderAdapter(dialog.getContext(), cartList, imageList, "10.00");
        rcv_items.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
        rcv_items.setAdapter(cartOrderAdapter);

        //display dialog
        dialog.show();
    }

    /**
     * Helper method which query the invoice by an ID
     * @param orderID ID to find the order
     */
    private void searchByID(int orderID){
        ArrayList<Order> newList = new ArrayList<Order>();
        for(Order o : orderList)
        {
            if(o.getOrderID() == orderID)
                newList.add(o);
        }
        setAdapter(newList);
    }

    /**
     * Helper method which query the invoice by the date
     * @param list List of the invoice to query
     * @return Queried list by the date
     */
    private ArrayList<Order> searchByDate(ArrayList<Order> list){
        //gets inputs from the text fields
        String min = getTextFromEditText(et_min_date);
        String max = getTextFromEditText(et_max_date);
        //return object to contain queried items
        ArrayList<Order> newList = new ArrayList<Order>();

        //case 1. user specified both lower and upper bound
        if(min.length() != 0 && max.length() != 0){
            for(Order o : list){
                //Query the order in between the bounds
                if(min_date.before(o.getDate()) && max_date.after(o.getDate()))
                    newList.add(o);
            }
        }
        //case 2. user only specified lower bound
        else if(min.length() != 0 && max.length() == 0)
        {
            for(Order o : list){
                //Query the order from the lower bound to infinity (now)
                if(min_date.before(o.getDate()))
                    newList.add(o);
            }
        }
        //case 3. user only specified upper bound
        else if(min.length() == 0 && max.length() != 0)
        {
            for(Order o : list){
                //Query the order from the past (beginning) to upper bound
                if(max_date.after(o.getDate()))
                    newList.add(o);
            }
        }
        //case 4. user did not specify any bound (validated by listener already)
        return newList;
    }

    /**
     * Helper method which query the invoice by the total amount
     * @param list List of the invoice to query
     * @return Queried list by the total amount
     */
    private ArrayList<Order> searchByTotal(ArrayList<Order> list){
        //gets inputs from the text fields
        String min = getTextFromEditText(et_min_total);
        String max = getTextFromEditText(et_max_total);
        double lowerBound;
        double upperBound;
        //return object to contain queried items
        ArrayList<Order> newList = new ArrayList<Order>();
        //case 1. user specified both lower and upper bound
        if(min.length() != 0 && max.length() != 0){
            lowerBound = Double.parseDouble(min);
            upperBound = Double.parseDouble(max);
            for(Order o : list){
                //Query the order in between the bounds
                if(lowerBound <= o.getSubTotal() && o.getSubTotal() <= upperBound)
                    newList.add(o);
            }
        }
        //case 2. user only specified lower bound
        else if(min.length() != 0 && max.length() == 0)
        {
            lowerBound = Double.parseDouble(min);
            for(Order o : list){
                //Query the order from the lower bound to infinity (now)
                if(lowerBound <= o.getSubTotal())
                    newList.add(o);
            }
        }
        //case 3. user only specified upper bound
        else if(min.length() == 0 && max.length() != 0)
        {
            upperBound = Double.parseDouble(max);
            for(Order o : list){
                //Query the order from the past (beginning) to upper bound
                if(o.getSubTotal() <= upperBound)
                    newList.add(o);
            }
        }
        //case 4. user did not specify any bound (validated by listener already)
        return newList;
    }

    /**
     * OnClick Listener for the item click event. It opens up the Date picker and Time picker dialogs
     * and save the information into myCalendar object
     * @param view View which the listener will be attached
     */
    @Override
    public void onClick(final View view) {
        //Create DatePicker Dialog
        DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //Saves date information to Calendar object
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                //Create TimePicker dialog
                TimePickerDialog.OnTimeSetListener tpd = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        myCalendar.set(Calendar.HOUR_OF_DAY, hour);
                        myCalendar.set(Calendar.MINUTE, min);
                        updateLabel(view);
                    }
                };

                //opens TimePicker Dialog after Date Picker
                new TimePickerDialog(InvoiceActivity.this, tpd,
                        myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        };

        //opens DatePicker Dialog
        new DatePickerDialog(InvoiceActivity.this, dpd,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * Helper method which extract the string from the EditText
     * @param et EditText containing the string
     * @return String contained in the EditText
     */
    private String getTextFromEditText(EditText et){
        return et.getText().toString().trim();
    }
}
