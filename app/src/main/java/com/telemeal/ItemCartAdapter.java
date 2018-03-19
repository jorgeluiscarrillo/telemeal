package com.telemeal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Bryan on 3/19/2018.
 */

public class ItemCartAdapter extends RecyclerView.Adapter<ItemCartAdapter.ItemCartHolder> {
    Context mContext;
    ArrayList<CartItem> mCart;

    public ItemCartAdapter(Context c, ArrayList<CartItem> ca)
    {
        mContext = c;
        mCart = ca;
    }

    @Override
    public ItemCartAdapter.ItemCartHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_cart_layout,parent,false);
        ItemCartAdapter.ItemCartHolder vHolder = new ItemCartAdapter.ItemCartHolder(v);
        return vHolder;
    }


    @Override
    public void onBindViewHolder(ItemCartAdapter.ItemCartHolder holder, int position)
    {
        holder.tv_quantity.setText(String.valueOf(mCart.get(position).getQuantity()));
        holder.tv_name.setText(mCart.get(position).getName());
        holder.tv_price.setText(String.valueOf(mCart.get(position).getPrice()));
    }

    public int getItemCount()
    {
        return mCart.size();
    }

    public static class ItemCartHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_quantity;
        private TextView tv_name;
        private TextView tv_price;

        public ItemCartHolder(View itemView) {
            super(itemView);

            tv_quantity = (TextView) itemView.findViewById(R.id.cart_quantity);
            tv_name = (TextView) itemView.findViewById(R.id.cart_name);
            tv_price = (TextView) itemView.findViewById(R.id.cart_price);
        }
    }
}
