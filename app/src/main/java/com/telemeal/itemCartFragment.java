package com.telemeal;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class itemCartFragment extends Fragment {
    /**View that this fragment is held in*/
    View myView;

    /**Recycler view that will show the cart item information*/
    RecyclerView cart;

    /**Foods that will be converted to cart items*/
    ArrayList<Food> foodList;

    /**Items that the customer ordered*/
    private ArrayList<CartItem> cartItems;

    /**Used to show the cart items image in the next activity*/
    private ArrayList<UploadImage> cartImages;

    /**The adapter that the recycler view will use to show the data*/
    private ItemCartAdapter cartAdapter;

    /**Button to remove everything in cartItems arraylist*/
    Button clearAll;

    /**Button to move to the next activity*/
    Button checkout;

    /**The prices that will be shown to the customer - calculated using data inside cart items*/
    TextView total, tax, subTotal;
    double taxPrice = 0.1;
    double totalPrice = 0;
    double subTotalPrice = 0;

    /**Order that is generated after customer clicks checkout*/
    Order order;

    /**
     * Required empty public constructor
     */
    public itemCartFragment() {

    }

    /**
     * Initializes the item cart fragment
     * @param inflater Opens a new fragment over the activity
     * @param container view group that the fragment is in
     * @param savedInstanceState saves the state of the fragment when going back into it
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        foodList = new ArrayList<Food>();
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
            /**
             * When clicked asks the customer if they want to remove all items
             * if yes - removes all items from the cart
             * if no - does nothing
             * @param view the view the click is done in
             */
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
            /**
             * Goes to the next activity if there are items inside the cart
             * if there are no items, an alert is made notifying the customer they must order items
             * @param view the view the click is held in
             */
            @Override
            public void onClick(View view) {
                if(cartItems.isEmpty())
                {
                    AlertDialog alert = new AlertDialog.Builder(getContext()).create();
                    alert.setTitle("Cart empty");
                    alert.setMessage("You must have at least one item in the cart to proceed to checkout!");
                    alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    alert.show();
                }
                else
                {
                    long id = System.currentTimeMillis();
                    long id_six_digit = id % 10000;
                    subTotalPrice = Double.parseDouble(subTotal.getText().toString());
                    order = new Order((int)id_six_digit, totalPrice, subTotalPrice, new Date(), false, foodList, false);
                    Log.d("ITEM ADDING: ", ""+id_six_digit);
                    Intent i = new Intent(view.getContext(), ConfirmOrderActivity.class);
                    Bundle b = new Bundle();
                    String finalTotal = total.getText().toString();
                    String finalTax = tax.getText().toString();
                    String finalSubtotal = subTotal.getText().toString();
                    b.putString("tax", finalTax);
                    b.putString("total", finalTotal);
                    b.putString("subtotal", finalSubtotal);
                    b.putParcelableArrayList("cartItems", cartItems);
                    b.putParcelableArrayList("cartImages", cartImages);
                    b.putParcelable("order", order);
                    i.putExtras(b);
                    startActivity(i);
                }
            }
        });

        cartItems = new ArrayList<>();
        cartImages = ((MenuActivity) getActivity()).getImages();

        cartAdapter = new ItemCartAdapter(getContext(),cartItems, itemCartFragment.this);
        cart.setLayoutManager(new LinearLayoutManager(getActivity()));
        cart.setAdapter(cartAdapter);

        // Inflate the layout for this fragment
        return myView;
    }

    public void AddItem(Food f)
    {
        foodList.add(f);
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

    /**
     * Method used to remove all items inside the cart
     */
    public void RemoveAllItems()
    {
        cartItems.clear();
        foodList.clear();
        AdjustPrices();
        cartAdapter.notifyDataSetChanged();
    }

    /**
     * Adjust the prices for the customer to see
     */
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
