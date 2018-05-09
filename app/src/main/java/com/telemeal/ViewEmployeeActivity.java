package com.telemeal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewEmployeeActivity extends AppCompatActivity {

    private Spinner spnr_priv;
    private EditText et_minID;
    private EditText et_maxID;
    private Button btn_apply;
    private RecyclerView rcv_empView;
    private listEmployeeAdapter adapter;

    private DatabaseReference dbEmployee;

    private ArrayList<Employee> empList;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employee);

        initialize();
    }

    /**
     * Helper method which initializes all the input/output fields
     */
    private void initialize(){
        //manager privilege picker
        String[] priv = {"All", "Yes", "No"};
        spnr_priv = (Spinner) findViewById(R.id.ve_spnr_priv);
        spnr_priv.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_text_layout, priv));
        //Query fields
        et_minID = (EditText) findViewById(R.id.ve_et_minID);
        et_maxID = (EditText) findViewById(R.id.ve_et_maxID);
        //Output view
        rcv_empView = (RecyclerView) findViewById(R.id.ve_rcv_empList);
        //Button to execute query
        btn_apply = (Button) findViewById(R.id.ve_btn_filter);

        //initializes the database to referencing the employee table
        dbEmployee = FirebaseDatabase.getInstance().getReference("employees");

        //Real-time database listener for any change happen in the referencing database
        dbEmployee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //create local memory container
                //when the database changed in real-time, it will create new local memory for the list
                //and resets the adapter
                empList = new ArrayList<Employee>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Employee e = postSnapshot.getValue(Employee.class);
                    empList.add(e);
                }
                setAdapter(empList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Click event listener for executing query
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //case 1. when the privilege is selected to "All"
                if(spnr_priv.getSelectedItem().toString().equals("All")){
                    //try query further with the ID
                    filterByID(empList, et_minID.getText().toString(), et_maxID.getText().toString());
                }
                //case 2. when the privilege is selected to "Yes"
                else if(spnr_priv.getSelectedItem().toString().equals("Yes")){
                    ArrayList<Employee> newList = new ArrayList<Employee>();
                    //Query by privilege
                    for(Employee e : empList){
                        if(e.getPrivilege()){
                            newList.add(e);
                        }
                    }
                    //and try query further with the ID
                    filterByID(newList, et_minID.getText().toString(), et_maxID.getText().toString());
                }
                //case 3. when the privilege is selected to "No"
                else{
                    ArrayList<Employee> newList = new ArrayList<Employee>();
                    //Query by privilege
                    for(Employee e : empList){
                        if(!e.getPrivilege()){
                            newList.add(e);
                        }
                    }
                    //and try query further with the ID
                    filterByID(newList, et_minID.getText().toString(), et_maxID.getText().toString());
                }
            }
        });
    }

    /**
     * Helper method which set/reset the adapter to update to new list
     * @param empToShow list to format and show
     */
    private void setAdapter(ArrayList<Employee> empToShow){
        adapter = new listEmployeeAdapter(ViewEmployeeActivity.this, empToShow);
        rcv_empView.setLayoutManager(new LinearLayoutManager(ViewEmployeeActivity.this));
        rcv_empView.setAdapter(adapter);
    }

    /**
     * method which query the data with the range of ID
     * @param list list to be queried
     * @param min lower bound of ID
     * @param max upper bound of ID
     */
    private void filterByID(ArrayList<Employee> list, String min, String max){
        //case 1. when both fields are empty
        if(min.length() == 0 && max.length() == 0){
            //set the list to the adapter as it is passed
            setAdapter(list);
        }
        //case 2. lower bound is specified
        else if(min.length() != 0 && max.length() == 0){
            ArrayList<Employee> newList = new ArrayList<Employee>();
            double min_id = Integer.parseInt(min);
            //query from the lower bound to infinity (to end)
            for(Employee e : list){
                if(e.getId() >= min_id){
                    newList.add(e);
                }
            }
            //set the new queried list to the adapter
            setAdapter(newList);
        }
        //case 3. upper bound is specified
        else if(min.length() == 0 && max.length() != 0){
            ArrayList<Employee> newList = new ArrayList<Employee>();
            double max_id = Integer.parseInt(max);
            //query from the beginning to the upper bound
            for(Employee f : list){
                if(f.getId() <= max_id){
                    newList.add(f);
                }
            }
            //set the new queried list to the adapter
            setAdapter(newList);
        }
        //case 4. both lower and upper bounds are specified
        else{
            ArrayList<Employee> newList = new ArrayList<Employee>();
            double min_id = Integer.parseInt(min);
            double max_id = Integer.parseInt(max);
            //query from the lower bound to the upper bound
            for(Employee f : list){
                if(f.getId() >= min_id && f.getId() <= max_id){
                    newList.add(f);
                }
            }
            //set the new queried list to the adapter
            setAdapter(newList);
        }
    }
}
