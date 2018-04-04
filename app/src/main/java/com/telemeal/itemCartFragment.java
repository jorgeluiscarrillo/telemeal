package com.telemeal;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class itemCartFragment extends Fragment {
    View myView;
    RecyclerView cart;
    private ArrayList<CartItem> cartItems;
    private ItemCartAdapter cartAdapter;
    Button clearAll;
    Button checkout;
    TextView total, tax, subTotal;
    double taxPrice = 0.1;
    double totalPrice = 0;

    public itemCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_item_cart, container, false);
        cart = (RecyclerView) myView.findViewById(R.id.menu_cart);
        clearAll = (Button) myView.findViewById(R.id.clear_all);

        total = (TextView) myView.findViewById(R.id.total_price);
        tax = (TextView) myView.findViewById(R.id.tax_price);
        subTotal = (TextView) myView.findViewById(R.id.subtotal_price);

        total.setText(String.format(Locale.getDefault(),"%.2f",totalPrice));
        tax.setText(String.format(Locale.getDefault(),"%.2f",totalPrice*taxPrice));
        subTotal.setText(String.format(Locale.getDefault(),"%.2f",(totalPrice + totalPrice*taxPrice)));

        checkout = (Button) myView.findViewById(R.id.checkout);

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartItems.size() != 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Clearing cart");
                    builder.setMessage("Are you sure you want to clear all items from the cart?");
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    RemoveAllItems();
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), PPActivity.class);
                Bundle b = new Bundle();
                String finalTax = tax.getText().toString();
                b.putString("tax", finalTax);
                b.putParcelableArrayList("cartItems", cartItems);
                i.putExtras(b);
                startActivity(i);
            }
        });

        cartItems = new ArrayList<>();

        cartAdapter = new ItemCartAdapter(getContext(),cartItems, itemCartFragment.this);
        cart.setLayoutManager(new LinearLayoutManager(getActivity()));
        cart.setAdapter(cartAdapter);

        // Inflate the layout for this fragment
        return myView;
    }

    public void AddItem(Food f)
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
                cartAdapter.notifyDataSetChanged();

                AdjustPrices();
            }
        }
        if(!inCart)
        {
            CartItem newItem = new CartItem(1, f.getName(),f.getPrice(), f.getSku());
            cartItems.add(newItem);
            cartAdapter.notifyItemInserted(cartItems.size()-1);

            AdjustPrices();
        }
    }

    public void RemoveAllItems()
    {
        cartItems.clear();
        AdjustPrices();
        cartAdapter.notifyDataSetChanged();
    }

    public void AdjustPrices()
    {
        totalPrice = 0;
        for(CartItem ci: cartItems)
        {
            totalPrice += ci.getPrice();
        }
        total.setText(String.format(Locale.getDefault(),"%.2f",totalPrice));
        tax.setText(String.format(Locale.getDefault(),"%.2f",totalPrice*taxPrice));
        subTotal.setText(String.format(Locale.getDefault(),"%.2f",(totalPrice + totalPrice*taxPrice)));
        cartAdapter.notifyDataSetChanged();

    }
}
