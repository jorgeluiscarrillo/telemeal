package com.telemeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EditEmployeeActivity extends AppCompatActivity {

    private EditText et_eid;
    private EditText et_name;
    private EditText et_pos;
    private CheckBox cb_priv;

    private TextView tv_ueid;
    private Spinner spnr_uname;
    private EditText et_upos;
    private CheckBox cb_upriv;

    private Button btn_add;
    private Button btn_view;
    private Button btn_edit;
    private Button btn_delete;

    private DatabaseReference dbEmployee;

    private HashMap<Integer, Employee> empMap = new HashMap<Integer, Employee>();
    private EditEmployeeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);
        initializer();
    }

    /**
     * Helper methods which initializes all the member variables
     */
    private void initializer() {
        //get the database reference named "employee" from firebase
        dbEmployee = FirebaseDatabase.getInstance().getReference("employees");

        //initialize all the input fields in Add Employee side
        et_eid = (EditText) findViewById(R.id.edemp_et_eid);
        et_name = (EditText) findViewById(R.id.edemp_et_name);
        et_pos = (EditText) findViewById(R.id.edemp_et_pos);
        cb_priv = (CheckBox) findViewById(R.id.edemp_cb_priv);

        //initialize all the input fields in Update Employee side
        tv_ueid = (TextView) findViewById(R.id.edemp_tv_updateeid);
        spnr_uname = (Spinner) findViewById(R.id.edemp_spnr_updatename);
        et_upos = (EditText) findViewById(R.id.edemp_et_updatepos);
        cb_upriv = (CheckBox) findViewById(R.id.edemp_cb_updatepriv);

        //initialize all the buttons
        btn_add = (Button) findViewById(R.id.edemp_btn_add);
        btn_view = (Button) findViewById(R.id.edemp_btn_view);
        btn_edit = (Button) findViewById(R.id.edemp_btn_edit);
        btn_delete = (Button) findViewById(R.id.edemp_btn_delete);

        //Adding real time change listener on database
        dbEmployee.addValueEventListener(new ValueEventListener() {
            //if data changes,
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                //get the snapshot (structure) of the database, and iterate to
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //convert into Employee objects
                    Employee emp = postSnapshot.getValue(Employee.class);
                    //store converted Employee object in the local memory
                    empMap.put(emp.getId(), emp);
                }

                //get the values of the HashMap which are Employee objects
                ArrayList<Employee> list_emps = new ArrayList<Employee>(empMap.values());
                //and pass to spinner adapter
                adapter = new EditEmployeeAdapter(EditEmployeeActivity.this, R.layout.simple_text_layout, list_emps);
                //set the adapter
                spnr_uname.setAdapter(adapter);
                //attach listener which will auto-set the input fields
                spnr_uname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        //get the object from the adapter
                        Employee emp = (Employee) adapterView.getItemAtPosition(i);

                        //and set the field with corresponding values
                        tv_ueid.setText("" + emp.getId());
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

        //Button click listener to invoke the helper method, addEmployee
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEmployee();
            }
        });

        //Button click listener to open new activity
        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewEmpIntent = new Intent(EditEmployeeActivity.this, ViewEmployeeActivity.class);
                startActivity(viewEmpIntent);
            }
        });

        //Button click listener to update the change into the database
        //Assume that user has changed all the input fields into desired value
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the id, the key of the database object
                String user = tv_ueid.getText().toString();
                //change position value
                dbEmployee.child(user).child("position").setValue(et_upos.getText().toString());
                //change privilege value
                dbEmployee.child(user).child("privilege").setValue(cb_upriv.isChecked());
                //show message
                showMessage("User ID: " + user + "'s data has been changed.");
                clearFields();
            }
        });

        //Button click listener to delete object in the database
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the object to delete
                Employee emp = (Employee) spnr_uname.getSelectedItem();
                //delete object with matching id
                dbEmployee.child("" + emp.getId()).removeValue();
                //clear memory (delete will invoke the data change which ValueEventListener will catch)
                empMap.clear();
                // show message
                showMessage("User Name: " + emp.getName() + " has deleted.");
                clearFields();
            }
        });
    }

    /**
     * Helper method which will get the values from the input fields. Then it will create an object to add into the database
     */
    private void addEmployee() {
        //get the values from the input fields
        String name = et_name.getText().toString();
        String string_eid = et_eid.getText().toString();
        String pos = et_pos.getText().toString();
        boolean hasPriv = cb_priv.isChecked();

        //name and id combination is primary key value
        //Thus, they cannot be null. And if user wish to add the object having same name and id combination,
        //it will overwrite existing object in the database
        if (!TextUtils.isEmpty(name)) {
            if (!TextUtils.isEmpty(string_eid)) {
                //create an object
                Employee emp = new Employee(Integer.parseInt(string_eid), name, pos, hasPriv);

                //add/overwrite the object into the database
                dbEmployee.child(string_eid).setValue(emp);

                //show message
                showMessage("Employee " + name + " added");

                clearFields();
            } else {
                //when id is null, it will show error message
                showMessage("Required Field: ID is missing");
            }
        } else {
            //when name is null, it will show error message
            showMessage("Required Field: Name is missing");
        }
    }

    /**
     * clear input fields after one function is completed
     */
    private void clearFields() {
        //clear Add-side input fields
        et_eid.setText(null);
        et_name.setText(null);
        et_pos.setText(null);
        cb_priv.setChecked(false);

        //clear Update-side input fields
        tv_ueid.setText(null);
        spnr_uname.setSelection(0);
        et_upos.setText(null);
        cb_upriv.setChecked(false);
    }

    /**
     * Show message which user sets
     * @param msg String value to be shown in the message
     */
    private void showMessage(String msg) {
        Toast.makeText(EditEmployeeActivity.this, msg, Toast.LENGTH_LONG).show();
    }
}