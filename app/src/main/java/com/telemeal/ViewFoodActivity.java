package com.telemeal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ViewFoodActivity extends AppCompatActivity {

    private Spinner spnr_category;
    private EditText et_minPrice;
    private EditText et_maxPrice;
    private Button btn_apply;
    private RecyclerView rcv_foodView;
    private listFoodAdapter adapter;

    private DatabaseReference dbFood;

    private ArrayList<Food> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food);

        initialize();
    }

    private void initialize(){
        String[] category = {"",
                FoodCategory.Appetizer.toString(),
                FoodCategory.Drink.toString(),
                FoodCategory.Main.toString(),
                FoodCategory.Dessert.toString()};

        spnr_category = (Spinner) findViewById(R.id.vf_spnr_category);
        spnr_category.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_text_layout, category));
        et_minPrice = (EditText) findViewById(R.id.vf_et_minPrice);
        et_maxPrice = (EditText) findViewById(R.id.vf_et_maxPrice);
        rcv_foodView = (RecyclerView) findViewById(R.id.vf_rcv_foodList);
        btn_apply = (Button) findViewById(R.id.vf_btn_filter);

        dbFood = FirebaseDatabase.getInstance().getReference("foods");

        dbFood.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                foodList = new ArrayList<Food>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Food f = postSnapshot.getValue(Food.class);
                    foodList.add(f);
                }
                setAdapter(foodList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spnr_category.getSelectedItem().toString() == ""){
                    filterByPrice(foodList, et_minPrice.getText().toString(), et_maxPrice.getText().toString());
                }else{
                    ArrayList<Food> newList = new ArrayList<Food>();
                    for(Food f : foodList){
                        if(f.getCategory().toString().equals(spnr_category.getSelectedItem().toString())){
                            newList.add(f);
                        }
                    }
                    filterByPrice(newList, et_minPrice.getText().toString(), et_maxPrice.getText().toString());
                }
            }
        });
    }

    private void setAdapter(ArrayList<Food> foodToShow){
        adapter = new listFoodAdapter(ViewFoodActivity.this, foodToShow);
        rcv_foodView.setLayoutManager(new LinearLayoutManager(ViewFoodActivity.this));
        rcv_foodView.setAdapter(adapter);
    }

    private void filterByPrice(ArrayList<Food> list, String min, String max){
        if(min.length() == 0 && max.length() == 0){
            setAdapter(list);
        }
        else if(min.length() != 0 && max.length() == 0){
            ArrayList<Food> newList = new ArrayList<Food>();
            double min_price = Double.parseDouble(min);
            for(Food f : list){
                if(f.getPrice() >= min_price){
                    newList.add(f);
                }
            }
            setAdapter(newList);
        }
        else if(min.length() == 0 && max.length() != 0){
            ArrayList<Food> newList = new ArrayList<Food>();
            double max_price = Double.parseDouble(max);
            for(Food f : list){
                if(f.getPrice() <= max_price){
                    newList.add(f);
                }
            }
            setAdapter(newList);
        }
        else{
            ArrayList<Food> newList = new ArrayList<Food>();
            double min_price = Double.parseDouble(min);
            double max_price = Double.parseDouble(max);
            for(Food f : list){
                if(f.getPrice() >= min_price && f.getPrice() <= max_price){
                    newList.add(f);
                }
            }
            setAdapter(newList);
        }
    }

    private void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private boolean isEmpty(EditText etText){
        return etText.getText().toString().trim().length() == 0;
    }
}
