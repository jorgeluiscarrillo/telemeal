package com.telemeal;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
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
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private DatabaseReference dbFoods;
    private FirebaseStorage storage;
    private ArrayList<Food> foods;

    public listFoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_tab_menu, container, false);

        tabLayout = (TabLayout) myView.findViewById(R.id.tablayout_menu);
        appBarLayout = (AppBarLayout) myView.findViewById((R.id.appbar));
        viewPager = (ViewPager) myView.findViewById(R.id.view_pager);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        foods = new ArrayList<>();
        adapter.AddFragment(new TabFoodFragment(), "All");
        ((TabFoodFragment)adapter.getItem(0)).SetCategory("All");
        adapter.AddFragment(new TabFoodFragment(), "Main");
        ((TabFoodFragment)adapter.getItem(1)).SetCategory("Main");
        adapter.AddFragment(new TabFoodFragment(), "Drink");
        ((TabFoodFragment)adapter.getItem(2)).SetCategory("Drink");
        adapter.AddFragment(new TabFoodFragment(), "Appetizer");
        ((TabFoodFragment)adapter.getItem(3)).SetCategory("Appetizer");
        adapter.AddFragment(new TabFoodFragment(), "Dessert");
        ((TabFoodFragment)adapter.getItem(4)).SetCategory("Dessert");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return myView;
    }
}
