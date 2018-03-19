package com.telemeal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class listFoodFragment extends Fragment {
    View myView;
    RecyclerView foodList;
    String category;
    ArrayList<Food> foods;
    ArrayList<CartItem> items;
    Button catAll, catMain, catAppetizer, catDrink, catDessert;

    public listFoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_list_food, container, false);
        foodList = (RecyclerView) myView.findViewById(R.id.foods);

        catAll = (Button) myView.findViewById(R.id.cat_all);
        catAll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loadAllFood();
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

        loadAllFood();

        return myView;
    }

    public void loadAllFood()
    {
        foods = new ArrayList<>();
        foods.add(new Food("Hamburger",2.50,"Meat in a bun", "", "Main"));
        foods.add(new Food("Cheeseburger",3.00,"Meat covered by cheese in a bun this description is being deliberally lengthened so that I can test some shit that I don't normally test", "", "Main"));
        foods.add(new Food("Coke",1.50,"Coca-Cola", "", "Drink"));
        foods.add(new Food("French Fries",1.50,"Potato Sticks", "", "Appetizer"));
        foods.add(new Food("Cookie",1.00,"Cookie","","Dessert"));
        listFoodAdapter foodAdapter = new listFoodAdapter(getContext(),foods);
        foodList.setLayoutManager(new LinearLayoutManager(getActivity()));
        foodList.setAdapter(foodAdapter);
    }

    public void loadCategory()
    {
        ArrayList<Food> mainFood = new ArrayList<>();

        for(Food f:foods)
        {
            if(f.getCategory().equals(category))
            {
                mainFood.add(f);
            }
        }
        listFoodAdapter foodAdapter = new listFoodAdapter(getContext(),mainFood);
        foodList.setLayoutManager(new LinearLayoutManager(getActivity()));
        foodList.setAdapter(foodAdapter);
    }
}