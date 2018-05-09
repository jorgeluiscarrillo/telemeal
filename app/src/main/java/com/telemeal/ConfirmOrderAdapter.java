package com.telemeal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by jcarrillo on 4/18/18.
 */

public class ConfirmOrderAdapter extends RecyclerView.Adapter<ConfirmOrderAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<CartItem> mItems;
    private ArrayList<UploadImage> mImage;
    private String mTax;

    /**
     * Constructor for the confirm order recycler view
     * @param c Application context
     * @param i Items the user has in their cart
     * @param img Images for the cart items
     * @param t Tax price for the user's order
     */
    public ConfirmOrderAdapter(Context c, ArrayList<CartItem> i, ArrayList<UploadImage> img, String t) {
        mContext = c;
        mItems = i;
        mTax = t;
        mImage = img;
    }

    /**
     * Initialize the View holders when the adapter is created.
     * @param parent Parent view
     * @param viewType View type
     * @return Created holder object
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_confirm_order, parent,false);
        return new MyViewHolder(view);
    }

    /**
     * View components to be displayed for each recycler view item
     * @param holder Stores each of the component views
     * @param position Position of item in recycler view
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        for(int i = 0; i < mImage.size(); i++)
        {
            if(mItems.get(position).getName().equals(mImage.get(i).getName()))
            {
                Picasso.get()
                        .load(mImage.get(i).getImageUrl())
                        .fit()
                        .centerCrop()
                        .into(holder.food_image);

                break;

            }
        }

        holder.food_name.setText(mItems.get(position).getName());
        holder.food_quantity.setText(String.format(Locale.getDefault(), "%d", mItems.get(position).getQuantity()));
        holder.food_total.setText(String.format(Locale.getDefault(),"%.2f",mItems.get(position).getPrice()));
    }

    /**
     * Getter for cart items list
     * @return Amount of items in users cart
     */
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    /**
     * Holder class for constructing and initializing recycler view components
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView food_image;
        TextView food_name;
        TextView food_quantity;
        TextView food_total;

        public MyViewHolder(View itemView) {
            super(itemView);

            food_image = (ImageView) itemView.findViewById(R.id.co_foodPic);
            food_name = (TextView) itemView.findViewById(R.id.co_foodName);
            food_quantity = (TextView) itemView.findViewById(R.id.co_foodQty);
            food_total = (TextView) itemView.findViewById(R.id.co_foodTotal);
        }
    }
}
