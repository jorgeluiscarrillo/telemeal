package com.telemeal;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class listFoodFragment extends Fragment {
    private View myView;
    private RecyclerView foodList;
    private String category;
    private ArrayList<Food> foods;
    private ArrayList<Food> catFoods;
    private listFoodAdapter foodAdapter;
    private Button catAll, catMain, catAppetizer, catDrink, catDessert;
    private DatabaseReference dbFoods;
    private FirebaseStorage storage;
    private ProgressBar spinner;

    public listFoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_list_food, container, false);
        storage = FirebaseStorage.getInstance();
        foods = new ArrayList<>();
        catFoods = new ArrayList<>();

        foodList = (RecyclerView) myView.findViewById(R.id.foods);
        spinner = (ProgressBar) myView.findViewById(R.id.progressBar1);

        dbFoods = FirebaseDatabase
                .getInstance()
                .getReference("foods");

        spinner.setVisibility(View.VISIBLE);

        dbFoods.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                foods.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Food food = postSnapshot.getValue(Food.class);
                    foods.add(food);
                }
                spinner.setVisibility(View.GONE);
                loadAllFood();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getDetails());
            }
        });

        catAll = (Button) myView.findViewById(R.id.cat_all);
        catAll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                foodAdapter = new listFoodAdapter(getContext(),foods);
                foodList.setLayoutManager(new LinearLayoutManager(getActivity()));
                foodList.setAdapter(foodAdapter);
            }
        });

        catMain = (Button) myView.findViewById(R.id.cat_main);
        catMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category="Main";
                loadCategory();
            }
        });

        catAppetizer = (Button) myView.findViewById(R.id.cat_appetizer);
        catAppetizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category="Appetizer";
                loadCategory();
            }
        });

        catDrink = (Button) myView.findViewById(R.id.cat_drink);
        catDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category="Drink";
                loadCategory();
            }
        });

        catDessert = (Button) myView.findViewById(R.id.cat_dessert);
        catDessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category="Dessert";
                loadCategory();
            }
        });

        return myView;
    }

    public void loadAllFood()
    {
        foodAdapter = new listFoodAdapter(getContext(),foods);
        foodList.setLayoutManager(new LinearLayoutManager(getActivity()));
        foodList.setAdapter(foodAdapter);
    }

    public void loadCategory()
    {
        catFoods.clear();

        for(Food f:foods)
        {
            if(f.getCategory().toString().equals(category))
            {
                catFoods.add(f);
            }
        }
        foodAdapter = new listFoodAdapter(getContext(),catFoods);
        foodList.setLayoutManager(new LinearLayoutManager(getActivity()));
        foodList.setAdapter(foodAdapter);
    }
}
