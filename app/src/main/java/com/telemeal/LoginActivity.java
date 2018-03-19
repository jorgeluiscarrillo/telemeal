package com.telemeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText et_eid;
    private EditText et_name;
    private Button btn_enter;

    private DatabaseReference dbEmployee;
    private Query empQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializer();
    }

    private void initializer(){
        et_eid = (EditText) findViewById(R.id.login_et_eid);
        et_name = (EditText) findViewById(R.id.login_et_ename);
        btn_enter = (Button) findViewById(R.id.login_btn_enter);

        dbEmployee = FirebaseDatabase
                .getInstance()
                .getReference("employees");

        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSigningInfo();
                Intent mngrIntent = new Intent(LoginActivity.this, ManagerOptionActivity.class);
                startActivity(mngrIntent);
            }
        });
    }

    private Employee emp;

    private void getSigningInfo(){
        String name = et_name.getText().toString();
        int eid = Integer.parseInt(et_eid.getText().toString());



        System.out.print(emp);
    }

}
