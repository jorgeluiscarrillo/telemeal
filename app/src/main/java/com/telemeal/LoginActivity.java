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
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    /**What this activity is called when referenced with a tag*/
    private static final String TAG = "LoginActivity";

    /**Area where the user will enter their employee id*/
    private EditText et_eid;

    /**Area where the user will enter their name*/
    private EditText et_name;

    /**Button to go into the next activity - only if valid credentials are entered*/
    private Button btn_enter;

    /**Stores the valid employees that can login to the employee section of the application*/
    private HashMap<Integer, Employee> empList;

    /**Reference to the employee section in the database*/
    private DatabaseReference dbEmployee;

    /**
     * Initializes LoginActivity
     * @param savedInstanceState save the state of the activity in a bundle for other when activity is accessed again later
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializer();
    }

    /**
     * Area where the variables will be initialized
     */
    private void initializer(){
        et_eid = (EditText) findViewById(R.id.login_et_eid);
        et_name = (EditText) findViewById(R.id.login_et_ename);
        btn_enter = (Button) findViewById(R.id.login_btn_enter);

        dbEmployee = FirebaseDatabase
                .getInstance()
                .getReference("employees");

        dbEmployee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                empList = new HashMap<Integer, Employee>();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Employee employee = postSnapshot.getValue(Employee.class);
                    empList.put(employee.getId(), employee);
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
                if(isEmpty(et_eid)) {
                    Toast.makeText(getBaseContext(), "ID is missing", Toast.LENGTH_LONG).show();
                }
                else if(isEmpty(et_name)) {
                    Toast.makeText(getBaseContext(), "Name is missing", Toast.LENGTH_LONG).show();
                }else {
                    int id = Integer.parseInt(et_eid.getText().toString());

                    String name = et_name.getText().toString();
                    if (empList.keySet().contains(id)) {
                        if (empList.get(id).getName().equals(name)) {
                            if (empList.get(id).getPrivilege()) {
                                Intent mngrIntent = new Intent(LoginActivity.this, ManagerOptionActivity.class);
                                mngrIntent.putExtra("privilege",empList.get(id).getPrivilege());
                                startActivity(mngrIntent);
                            } else {
                                Intent edfdIntent = new Intent(LoginActivity.this, ManagerOptionActivity.class);
                                edfdIntent.putExtra("privilege",empList.get(id).getPrivilege());
                                startActivity(edfdIntent);
                            }
                            clearFields();
                        } else {
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

    /**
     * Method to check if the edit text boxes are empty
     * @param etText the edit text box that will be checked
     * @return true if the edit text box is empty
     */
    private boolean isEmpty(EditText etText){
        return etText.getText().toString().trim().length() == 0;
    }

    /**
     * Clears the text boxes
     */
    private void clearFields(){
        et_name.setText(null);
        et_eid.setText(null);
    }
}
