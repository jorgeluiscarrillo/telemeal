package com.telemeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText et_eid;
    private EditText et_name;
    private Button btn_enter;

    private List<Employee> empList = new ArrayList<Employee>();

    private DatabaseReference dbEmployee;
    private boolean result;

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

        dbEmployee.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Employee employee = postSnapshot.getValue(Employee.class);
                    empList.add(employee);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getDetails());
            }
        });

        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = Integer.parseInt(et_eid.getText().toString());
                String name = et_name.getText().toString();

                if(isEmpty(et_eid))
                    Toast.makeText(getBaseContext(), "ID is missing", Toast.LENGTH_SHORT).show();
                if(isEmpty(et_name))
                    Toast.makeText(getBaseContext(), "Name is missing", Toast.LENGTH_SHORT).show();

                for(Employee emp : empList){
                    if(emp.getId() == id){
                        if(emp.getName().equals(name)){
                            if(emp.getPrivilege()){
                                Intent mngrIntent = new Intent(LoginActivity.this, ManagerOptionActivity.class);
                                startActivity(mngrIntent);
                            }
                            else{
                                Intent edfdIntent = new Intent(LoginActivity.this, EditMenuActivity.class);
                                startActivity(edfdIntent);
                            }
                        }
                        else{
                            Toast.makeText(getBaseContext(), "Name does not match to the ID provided", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(getBaseContext(), "ID does not exist in the system", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean isEmpty(EditText etText){
        return etText.getText().toString().trim().length() == 0;
    }
}
