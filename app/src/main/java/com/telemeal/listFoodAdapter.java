package com.telemeal;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bryan on 3/18/2018.
 */

public class listFoodAdapter extends RecyclerView.Adapter<listFoodAdapter.ListFoodHolder>
{
    /**Current context adapter is in*/
    Context mContext;

    /**Holds the foods from the database*/
    ArrayList<Food> mfood;

    /**Holds the images from the database*/
    ArrayList<UploadImage> mImage;

    /**The food that the customer selected*/
    Food selectedFood;

    /**
     * Constructor that initalizes the parameters
     * @param c the context the adapter is in
     * @param f the list of foods that will
     * @param i the images for each food item
     */
    public listFoodAdapter(Context c, ArrayList<Food> f, ArrayList<UploadImage> i)
    {
        mContext = c;
        mfood = f;
        mImage = i;
    }

    /**
     * Creates the view holder for items
     * @param parent the view group the adapter is in
     * @param viewType an integer
     * @return the view holder that an item in the view will be held in
     */
    @Override
    public ListFoodHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview_item_food, parent,false);
        final ListFoodHolder vHolder = new ListFoodHolder(v);

        vHolder.foodItem.setOnClickListener(new View.OnClickListener() {
            /**
             * Places an item from the food list into the item cart
             * @param view the view the click was held int
             */
            @Override
            public void onClick(View view) {
                /*boolean inCart = false;
                for(int i = 0; i < cartItems.size(); i++)
                {
                    if(cartItems.get(i).getName().equals(mfood.get(vHolder.getAdapterPosition()).getName()))
                    {
                        int quantity = cartItems.get(i).getQuantity() + 1;
                        cartItems.get(i).setQuantity(quantity);

                        double price = cartItems.get(i).getPrice() + mfood.get(vHolder.getAdapterPosition()).getPrice();
                        cartItems.get(i).setPrice(price);
                        inCart = true;

                    }
                }
                if(!inCart)
                {
                    CartItem newItem = new CartItem(1,mfood.get(vHolder.getAdapterPosition()).getName(),mfood.get(vHolder.getAdapterPosition()).getPrice());
                    cartItems.add(newItem);
                }*/

                MenuActivity activity = (MenuActivity) mContext;

                selectedFood = mfood.get(vHolder.getAdapterPosition());
                activity.getItemCartFrag().AddItem(selectedFood);
            }
        });

        return vHolder;
    }

    /**
     * Initializes each item inside the view holder
     * @param holder the view holder the items are held in
     * @param position the position of the item
     */
    @Override
    public void onBindViewHolder(ListFoodHolder holder, int position)
    {
        //Loads and places images with the appropriate foods
        for(int i = 0; i < mImage.size(); i++)
        {
            if(mfood.get(position).getName().equals(mImage.get(i).getName()))
            {
                Picasso.get()
                        .load(mImage.get(i).getImageUrl())
                        .fit()
                        .centerCrop()
                        .into(holder.vf_image);

                break;
            }
        }

        holder.vf_name.setText(mfood.get(position).getName());
        holder.vf_price.setText(String.format(Locale.getDefault(),"%.2f",mfood.get(position).getPrice()));
    }

    /**
     * Gets the size of the foods array list
     * @return the size of mfood
     */
    public int getItemCount()
    {
        return mfood.size();
    }

    public static class ListFoodHolder extends RecyclerView.ViewHolder
    {
        /**The card the food item is held in*/
        private CardView foodItem;

        /**The food's image*/
        private ImageView vf_image;

        /**Display for the food's name*/
        private TextView vf_name;

        /**Display for the food's price*/
        private TextView vf_price;

        /**
         * Initializes the view holder
         * @param itemView the current view the view holder is in
         */
        public ListFoodHolder(View itemView) {
            super(itemView);

            foodItem = (CardView) itemView.findViewById(R.id.card_view_menu);
            vf_image = (ImageView) itemView.findViewById(R.id.vf_foodPic);
            vf_name = (TextView) itemView.findViewById(R.id.vf_foodName);
            vf_price = (TextView) itemView.findViewById(R.id.vf_foodPrice);
        }
    }
}
