package com.telemeal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class itemCartFragment extends Fragment {
    View myView;
    RecyclerView cart;
    private static ArrayList<CartItem> cartItems;
    private static ItemCartAdapter cartAdapter;

    public itemCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_item_cart, container, false);
        cart = myView.findViewById(R.id.menu_cart);
        cartItems = new ArrayList<>();

        cartAdapter = new ItemCartAdapter(getContext(),cartItems);
        cart.setLayoutManager(new LinearLayoutManager(getActivity()));
        cart.setAdapter(cartAdapter);

        // Inflate the layout for this fragment
        return myView;
    }

    public static void AddItem(Food f)
    {
        boolean inCart = false;
        for(int i = 0; i < cartItems.size(); i++)
        {
            if(cartItems.get(i).getName().equals(f.getName()))
            {
                int quantity = cartItems.get(i).getQuantity() + 1;
                cartItems.get(i).setQuantity(quantity);

                double price = cartItems.get(i).getPrice() + f.getPrice();
                cartItems.get(i).setPrice(price);
                inCart = true;

            }
        }
        if(!inCart)
        {
            CartItem newItem = new CartItem(1,f.getName(),f.getPrice());
            cartItems.add(newItem);
            cartAdapter.notifyItemInserted(cartItems.size()-1);
        }
    }

}
