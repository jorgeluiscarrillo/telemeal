package com.telemeal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class EditEmployeeActivity extends AppCompatActivity {

    private EditText et_eid;
    private EditText et_name;
    private EditText et_pos;
    private CheckBox cb_priv;

    private EditText et_ueid;
    private Spinner spnr_uname;
    private EditText et_upos;
    private CheckBox cb_upriv;

    private Button btn_add;
    private Button btn_view;
    private Button btn_edit;
    private Button btn_delete;

    private DatabaseReference dbEmployee;

    private HashMap<String, Employee> empMap = new HashMap<String, Employee>();
    private EditEmployeeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);
        initializer();
/*
        Intent empIntent = new Intent(ManagerOptionActivity.this, EditEmployeeActivity.class);
        startActivity(empIntent);
*/

    }

    private void initializer(){
        dbEmployee = FirebaseDatabase.getInstance().getReference("employees");

        et_eid = (EditText) findViewById(R.id.edemp_et_eid);
        et_name = (EditText) findViewById(R.id.edemp_et_name);
        et_pos = (EditText) findViewById(R.id.edemp_et_pos);
        cb_priv = (CheckBox) findViewById(R.id.edemp_cb_priv);

        et_ueid = (EditText) findViewById(R.id.edemp_et_updateeid);
        spnr_uname = (Spinner) findViewById(R.id.edemp_spnr_updatename);
        et_upos = (EditText) findViewById(R.id.edemp_et_updatepos);
        cb_upriv = (CheckBox) findViewById(R.id.edemp_cb_updatepriv);

        btn_add = (Button) findViewById(R.id.edemp_btn_add);
        btn_view = (Button) findViewById(R.id.edemp_btn_view);
        btn_edit = (Button) findViewById(R.id.edemp_btn_edit);
        btn_delete = (Button) findViewById(R.id.edemp_btn_delete);

        dbEmployee.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                  for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                      Employee emp = postSnapshot.getValue(Employee.class);
                      empMap.put(postSnapshot.getKey(), emp);
                  }

                  ArrayList<Employee> list_emps = (ArrayList<Employee>)empMap.values();
                  adapter = new EditEmployeeAdapter(EditEmployeeActivity.this, android.R.layout.simple_spinner_item, list_emps);
                  spnr_uname.setAdapter(adapter);
                  spnr_uname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                      @Override
                      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                          Employee emp = (Employee) adapterView.getItemAtPosition(i);

                          et_ueid.setText(""+emp.getId());
                          et_upos.setText(emp.getPosition());
                          cb_upriv.setChecked(emp.getPrivilege());
                      }

                      @Override
                      public void onNothingSelected(AdapterView<?> adapterView) {

                      }
                  });
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEmployee();
            }
        });

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEmployee();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEmployee();
            }
        });
    }

    private void addEmployee(){
        String name = et_name.getText().toString();
        int eid = Integer.parseInt(et_eid.getText().toString());
        String pos = et_pos.getText().toString();
        boolean hasPriv = cb_priv.isChecked();

        if(!TextUtils.isEmpty(name)){
            String id = dbEmployee.push().getKey();

            Employee emp = new Employee(eid, name, pos, hasPriv);

            dbEmployee.child(id).setValue(emp);

            Toast.makeText(this, "Employee " + name + " added", Toast.LENGTH_LONG).show();

            clearFields();
        }
    }

    private void editEmployee(){


    }

    private void deleteEmployee(){
        Query applesQuery = dbEmployee.orderByChild("id").equalTo(et_ueid.getText().toString());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot: dataSnapshot.getChildren()) {
                    Snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void clearFields(){
        et_eid.setText(null);
        et_name.setText(null);
        et_pos.setText(null);
        cb_priv.setChecked(false);

        et_ueid.setText(null);
        spnr_uname.setSelection(-1);
        et_upos.setText(null);
        cb_upriv.setChecked(false);
    }
}
