package com.telemeal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.List;

public class EditMenuActivity extends AppCompatActivity {

    private static final String TAG = "EditMenuActivity";

    private EditText et_name;
    private EditText et_price;
    private EditText et_desc;
    private EditText et_image;
    private Button btn_browse;
    private Spinner spnr_category;

    private Spinner spnr_uname;
    private EditText et_uprice;
    private EditText et_udesc;
    private EditText et_uimage;
    private Button btn_ubrowse;
    private Spinner spnr_ucategory;

    private Button btn_add_fd;
    private Button btn_view_fd;
    private Button btn_edit_fd;
    private Button btn_delete_fd;

    private DatabaseReference dbFoods;

    private ArrayList<Food> foodList = new ArrayList<Food>();
    private EditFoodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);
        initializer();
    }

    private void initializer() {
        dbFoods = FirebaseDatabase.getInstance().getReference("foods");

        et_name = (android.widget.EditText) findViewById(R.id.edfd_et_name);
        et_price = (EditText) findViewById(R.id.edfd_et_price);
        et_desc = (EditText) findViewById(R.id.edfd_et_desc);
        et_image = (EditText) findViewById(R.id.edfd_et_image);
        btn_browse = (Button) findViewById(R.id.edfd_btn_browse);
        spnr_category = (Spinner) findViewById(R.id.edfd_spnr_category);
        spnr_category.setAdapter(new ArrayAdapter<FoodCategory>(this, android.R.layout.simple_list_item_1, FoodCategory.values()));

        spnr_uname = (Spinner) findViewById(R.id.edfd_spnr_updatename);
        et_uprice = (EditText) findViewById(R.id.edfd_et_updateprice);
        et_udesc = (EditText) findViewById(R.id.edfd_et_updatedesc);
        et_uimage = (EditText) findViewById(R.id.edfd_et_updateimage);
        btn_ubrowse = (Button) findViewById(R.id.edfd_btn_updatebrowse);
        spnr_ucategory = (Spinner) findViewById(R.id.edfd_spnr_updatecategory);
        spnr_ucategory.setAdapter(new ArrayAdapter<FoodCategory>(this, android.R.layout.simple_list_item_1, FoodCategory.values()));

        btn_add_fd = (Button) findViewById(R.id.edfd_btn_addfooditem);
        btn_view_fd = (Button) findViewById(R.id.edfd_btn_viewfooditem);
        btn_edit_fd = (Button) findViewById(R.id.edfd_btn_editfooditem);
        btn_delete_fd = (Button) findViewById(R.id.edfd_btn_deletefooditem);

        dbFoods.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Food food = postSnapshot.getValue(Food.class);
                    foodList.add(food);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getDetails());
            }
        });

        adapter = new EditFoodAdapter(EditMenuActivity.this, android.R.layout.simple_spinner_item, foodList);
        spnr_uname.setAdapter(adapter);
        spnr_uname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Food food = adapter.getItem(i);
                et_uprice.setText(food.getPrice().toString());
                et_udesc.setText(food.getDescription());
                et_uimage.setText(food.getImage());
                spnr_ucategory.setSelection(food.getCategory().ordinal());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_add_fd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFood();
            }
        });

        btn_view_fd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewFdIntent = new Intent(EditMenuActivity.this, ViewFoodActivity.class);
                startActivity(viewFdIntent);
            }
        });

        btn_edit_fd.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editFood();
            }
        }));

        btn_delete_fd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFood();
            }
        });

        btn_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_ubrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void addFood(){
        String name = et_name.getText().toString();
        double price = Double.parseDouble(et_price.getText().toString());
        String desc = et_desc.getText().toString();
        String image = et_image.getText().toString();
        FoodCategory category = FoodCategory.values()[spnr_category.getSelectedItemPosition()];

        if(!TextUtils.isEmpty(name)){
            String id = dbFoods.push().getKey();

            Food food = new Food(name, price, desc, image, category);

            dbFoods.child(id).setValue(food);

            Toast.makeText(this, "Food " + name + " added", Toast.LENGTH_LONG).show();
        }
    }

    private void editFood(){

    }

    private void deleteFood(){

    }
}
