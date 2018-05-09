package com.telemeal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
    private viewFoodAdapter adapter;

    private DatabaseReference dbFood;
    private DatabaseReference dbImages;

    private ArrayList<Food> foodList;
    private ArrayList<UploadImage> images;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_food);

        initialize();
    }

    /**
     * initializes all the input/output fields and attach the event listener
     */
    private void initialize(){
        //list of category to be bound to the spinner
        String[] category = {"",
                FoodCategory.Appetizer.toString(),
                FoodCategory.Drink.toString(),
                FoodCategory.Main.toString(),
                FoodCategory.Dessert.toString()};
        //spinner (DropDownMenu) for category
        spnr_category = (Spinner) findViewById(R.id.vf_spnr_category);
        spnr_category.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_text_layout, category));
        //Query fields with price
        et_minPrice = (EditText) findViewById(R.id.vf_et_minPrice);
        et_maxPrice = (EditText) findViewById(R.id.vf_et_maxPrice);
        //output fields
        rcv_foodView = (RecyclerView) findViewById(R.id.vf_rcv_foodList);
        //button to execute the query
        btn_apply = (Button) findViewById(R.id.vf_btn_filter);
        //local memory for image to be fetched from the database
        images = new ArrayList<>();

        //initializes the database tables for foods and images
        dbFood = FirebaseDatabase.getInstance().getReference("foods");
        dbImages = FirebaseDatabase
                .getInstance()
                .getReference("image");

        //attach real-time event listener to foods table
        dbFood.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //create local memory container
                //when the database changed in real-time, it will create new local memory for the list
                //and resets the adapter
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

        //attach real-time event listener for the image table
        dbImages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //same for food
                images.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    UploadImage image = postSnapshot.getValue(UploadImage.class);
                    images.add(image);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //attach query execute listener to the button
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when category is not selected, try query with the price
                if(spnr_category.getSelectedItem().toString() == ""){
                    filterByPrice(foodList, et_minPrice.getText().toString(), et_maxPrice.getText().toString());
                }
                //when category is selected, query list with the category, then try query with the price
                else{
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

    /**
     * Helper method which set/reset the adapter to update to new list
     * @param foodToShow list to format and show
     */
    private void setAdapter(ArrayList<Food> foodToShow){
        adapter = new viewFoodAdapter(ViewFoodActivity.this, foodToShow, images);
        rcv_foodView.setLayoutManager(new GridLayoutManager(ViewFoodActivity.this,4));
        rcv_foodView.setAdapter(adapter);
    }

    /**
     * method which query the data with the range of price
     * @param list list to be queried
     * @param min lower bound of price
     * @param max upper bound of price
     */
    private void filterByPrice(ArrayList<Food> list, String min, String max){
        //case 1. when both fields are empty
        if(min.length() == 0 && max.length() == 0){
            //set the list to the adapter as it is passed
            setAdapter(list);
        }
        //case 2. lower bound is specified
        else if(min.length() != 0 && max.length() == 0){
            ArrayList<Food> newList = new ArrayList<Food>();
            double min_price = Double.parseDouble(min);
            //query from the lower bound to infinity (to end)
            for(Food f : list){
                if(f.getPrice() >= min_price){
                    newList.add(f);
                }
            }
            //set the new queried list to the adapter
            setAdapter(newList);
        }
        //case 3. upper bound is specified
        else if(min.length() == 0 && max.length() != 0){
            ArrayList<Food> newList = new ArrayList<Food>();
            double max_price = Double.parseDouble(max);
            //query from the beginning to the upper bound
            for(Food f : list){
                if(f.getPrice() <= max_price){
                    newList.add(f);
                }
            }
            //set the new queried list to the adapter
            setAdapter(newList);
        }
        //case 4. both lower and upper bounds are specified
        else{
            ArrayList<Food> newList = new ArrayList<Food>();
            double min_price = Double.parseDouble(min);
            double max_price = Double.parseDouble(max);
            //query from the lower bound to the upper bound
            for(Food f : list){
                if(f.getPrice() >= min_price && f.getPrice() <= max_price){
                    newList.add(f);
                }
            }
            //set the new queried list to the adapter
            setAdapter(newList);
        }
    }
}
