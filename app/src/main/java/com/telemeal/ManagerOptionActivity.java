package com.telemeal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManagerOptionActivity extends AppCompatActivity {

    private Button btn_employee;
    private Button btn_menu;
    private Button btn_invoice;
    private Button btn_order;
    private boolean privilage;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manger_option);
        privilage = getIntent().getExtras().getBoolean("privilege");
        initializer();
    }

    /**
     * Initializes all the input fields
     * and set the event listener
     */
    private void initializer(){
        //initializes input fields
        btn_employee = (Button) findViewById(R.id.mngr_btn_editemp);
        btn_menu = (Button) findViewById(R.id.mngr_btn_editfd);
        btn_invoice = (Button) findViewById(R.id.mngr_btn_invoice);
        btn_order = (Button) findViewById(R.id.mngr_btn_orders);

        //validate the user privilege and decide which screen to show
        if(!privilage)
        {
            //when user has no privilege to manage employees and view invoice
            btn_employee.setVisibility(View.GONE);
            btn_invoice.setVisibility(View.GONE);
        }
        else
        {
            //when user has a privilege to manage employees and view invoice
            btn_employee.setVisibility(View.VISIBLE);
            btn_invoice.setVisibility(View.VISIBLE);
        }

        //Click event listener which opens employee management activity
        btn_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent empIntent = new Intent(ManagerOptionActivity.this, EditEmployeeActivity.class);
                startActivity(empIntent);
            }
        });

        //Click event listener which opens food item management activity
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fdIntent = new Intent(ManagerOptionActivity.this, EditMenuActivity.class);
                startActivity(fdIntent);
            }
        });

        //Click event listener which opens invoice activity
        btn_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent invIntent = new Intent(ManagerOptionActivity.this, InvoiceActivity.class);
                startActivity(invIntent);
            }
        });

        //Click event listener which opens order activity
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ordIntent = new Intent(ManagerOptionActivity.this, OrdersActivity.class);
                startActivity(ordIntent);
            }
        });
    }
}
