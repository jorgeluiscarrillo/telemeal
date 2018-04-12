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
public class TabFoodFragment extends Fragment {
    private View myView;
    private RecyclerView foodList;
    private ArrayList<Food> foods;
    private ArrayList<Food> catFoods;
    private listFoodAdapter foodAdapter;
    private ProgressBar spinner;
    private DatabaseReference dbFoods;
    private String category;

    public TabFoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_recycle_foods, container, false);
        foods = new ArrayList<>();
        catFoods = new ArrayList<>();

        foodList = (RecyclerView) myView.findViewById(R.id.foods);
        spinner = (ProgressBar) myView.findViewById(R.id.progressBar1);

        spinner.setVisibility(View.VISIBLE);

        dbFoods = FirebaseDatabase
                .getInstance()
                .getReference("foods");

        dbFoods.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                foods.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Food food = postSnapshot.getValue(Food.class);
                    foods.add(food);
                }
                loadAllFood();
                if(!category.equals("All"))
                    loadCategory();
                changeSpinner();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getDetails());
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

    public void setFoods(ArrayList<Food> f)
    {
        foods = f;
    }

    public void changeSpinner()
    {
        spinner.setVisibility(View.GONE);
    }

    public void SetCategory(String c)
    {
        category = c;
    }
}
