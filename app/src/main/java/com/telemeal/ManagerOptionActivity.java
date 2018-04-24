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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manger_option);
        initializer();
    }

    private void initializer(){
        btn_employee = (Button) findViewById(R.id.mngr_btn_editemp);
        btn_menu = (Button) findViewById(R.id.mngr_btn_editfd);
        btn_invoice = (Button) findViewById(R.id.mngr_btn_invoice);
        btn_order = (Button) findViewById(R.id.mngr_btn_orders);

        btn_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent empIntent = new Intent(ManagerOptionActivity.this, EditEmployeeActivity.class);
                startActivity(empIntent);
            }
        });

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fdIntent = new Intent(ManagerOptionActivity.this, EditMenuActivity.class);
                startActivity(fdIntent);
            }
        });


        btn_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent invIntent = new Intent(ManagerOptionActivity.this, InvoiceActivity.class);
                startActivity(invIntent);
            }
        });

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ordIntent = new Intent(ManagerOptionActivity.this, OrdersActivity.class);
                startActivity(ordIntent);
            }
        });
    }
}
