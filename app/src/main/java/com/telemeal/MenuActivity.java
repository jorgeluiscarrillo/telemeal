package com.telemeal;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    listFoodFragment foodList;
    itemCartFragment itemCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        foodList = new listFoodFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.listFood,foodList, "list")
                .commit();

        itemCart = new itemCartFragment();
        manager.beginTransaction()
                .replace(R.id.itemCart,itemCart, "cart")
                .commit();
    }

    public itemCartFragment getItemCartFrag()
    {
        return itemCart;
    }

}
