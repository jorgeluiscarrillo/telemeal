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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employee);

        initialize();
    }

    private void initialize(){
        String[] priv = {"All", "Yes", "No"};
        spnr_priv = (Spinner) findViewById(R.id.ve_spnr_priv);
        spnr_priv.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_text_layout, priv));
        et_minID = (EditText) findViewById(R.id.ve_et_minID);
        et_maxID = (EditText) findViewById(R.id.ve_et_maxID);
        rcv_empView = (RecyclerView) findViewById(R.id.ve_rcv_empList);
        btn_apply = (Button) findViewById(R.id.ve_btn_filter);

        dbEmployee = FirebaseDatabase.getInstance().getReference("employees");

        dbEmployee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spnr_priv.getSelectedItem().toString().equals("All")){
                    filterByID(empList, et_minID.getText().toString(), et_maxID.getText().toString());
                }else if(spnr_priv.getSelectedItem().toString().equals("Yes")){
                    ArrayList<Employee> newList = new ArrayList<Employee>();
                    for(Employee e : empList){
                        if(e.getPrivilege()){
                            newList.add(e);
                        }
                    }
                    filterByID(newList, et_minID.getText().toString(), et_maxID.getText().toString());
                }else{
                    ArrayList<Employee> newList = new ArrayList<Employee>();
                    for(Employee e : empList){
                        if(!e.getPrivilege()){
                            newList.add(e);
                        }
                    }
                    filterByID(newList, et_minID.getText().toString(), et_maxID.getText().toString());
                }
            }
        });
    }

    private void setAdapter(ArrayList<Employee> empToShow){
        adapter = new listEmployeeAdapter(ViewEmployeeActivity.this, empToShow);
        rcv_empView.setLayoutManager(new LinearLayoutManager(ViewEmployeeActivity.this));
        rcv_empView.setAdapter(adapter);
    }

    private void filterByID(ArrayList<Employee> list, String min, String max){
        if(min.length() == 0 && max.length() == 0){
            setAdapter(list);
        }
        else if(min.length() != 0 && max.length() == 0){
            ArrayList<Employee> newList = new ArrayList<Employee>();
            double min_id = Integer.parseInt(min);
            for(Employee e : list){
                if(e.getId() >= min_id){
                    newList.add(e);
                }
            }
            setAdapter(newList);
        }
        else if(min.length() == 0 && max.length() != 0){
            ArrayList<Employee> newList = new ArrayList<Employee>();
            double max_id = Integer.parseInt(max);
            for(Employee f : list){
                if(f.getId() <= max_id){
                    newList.add(f);
                }
            }
            setAdapter(newList);
        }
        else{
            ArrayList<Employee> newList = new ArrayList<Employee>();
            double min_id = Integer.parseInt(min);
            double max_id = Integer.parseInt(max);
            for(Employee f : list){
                if(f.getId() >= min_id && f.getId() <= max_id){
                    newList.add(f);
                }
            }
            setAdapter(newList);
        }
    }
}
