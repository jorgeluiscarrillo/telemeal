package com.telemeal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by jcarrillo on 4/18/18.
 */

public class ConfirmOrderAdapter extends RecyclerView.Adapter<ConfirmOrderAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<CartItem> mItems;
    private String mTax;

    public ConfirmOrderAdapter(Context c, ArrayList<CartItem> i, String t) {
        mContext = c;
        mItems = i;
        mTax = t;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_confirm_order, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        for(int i = 0; i < mImage.size(); i++)
//        {
//            if(mItems.get(position).getName().equals(mImage.get(i).getName()))
//            {
//                Picasso.get()
//                        .load(mImage.get(i).getImageUrl())
//                        .fit()
//                        .centerCrop()
//                        .into(holder.food_image);
//
//                break;
//
//            }
//        }

        holder.food_name.setText(mItems.get(position).getName());
        holder.food_quantity.setText(String.format(Locale.getDefault(), "%d", mItems.get(position).getQuantity()));
        holder.food_total.setText(String.format(Locale.getDefault(),"%.2f",mItems.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

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
