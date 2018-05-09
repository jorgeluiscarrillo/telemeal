package com.telemeal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Bryan on 3/19/2018.
 */

public class ItemCartAdapter extends RecyclerView.Adapter<ItemCartAdapter.ItemCartHolder> {
    Context mContext;
    ArrayList<CartItem> mCart;
    itemCartFragment fragment;

    /**
     * Constructor for the confirm order recycler view
     * @param c Application context
     * @param ca A food item the is in the users cart
     * @param frag Fragment for the user's cart
     */
    public ItemCartAdapter(Context c, ArrayList<CartItem> ca, itemCartFragment frag)
    {
        mContext = c;
        mCart = ca;
        fragment = frag;
    }

    /**
     * Initialize the View holders when the adapter is created.
     * @param parent Parent view
     * @param viewType View type
     * @return Created holder object
     */
    @Override
    public ItemCartHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_cart_layout,parent,false);
        return new ItemCartHolder(v);
    }

    /**
     * View components to be displayed for each recycler view item
     * @param holder Stores each of the component views
     * @param position Position of item in recycler view
     */
    @Override
    public void onBindViewHolder(final ItemCartAdapter.ItemCartHolder holder, final int position)
    {
        holder.tv_quantity.setText(String.valueOf(mCart.get(position).getQuantity()));
        holder.tv_name.setText(mCart.get(position).getName());
        holder.tv_price.setText(String.format(Locale.getDefault(),"%.2f",mCart.get(position).getPrice()));
        holder.clearItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCart.get(position).getQuantity() > 1) {
                    double price = mCart.get(position).getPrice() / mCart.get(position).getQuantity();
                    int current =  mCart.get(position).getQuantity() - 1;

                    mCart.get(position).setQuantity(current);
                    mCart.get(position).setPrice(mCart.get(position).getPrice() - price);
                    notifyDataSetChanged();
                    fragment.AdjustPrices();
                } else {
                    mCart.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mCart.size());
                    fragment.AdjustPrices();
                }
            }
        });
    }

    /**
     * Getter for the size of a user's cart
     * @return Item cart size
     */
    public int getItemCount()
    {
        return mCart.size();
    }

    public static class ItemCartHolder extends RecyclerView.ViewHolder
    {
        private LinearLayout CartItem;
        private TextView tv_quantity;
        private TextView tv_name;
        private TextView tv_price;
        private ImageView clearItem;

        public ItemCartHolder(View itemView) {
            super(itemView);
            CartItem = (LinearLayout) itemView.findViewById(R.id.cart_item);
            tv_quantity = (TextView) itemView.findViewById(R.id.cart_quantity);
            tv_name = (TextView) itemView.findViewById(R.id.cart_name);
            tv_price = (TextView) itemView.findViewById(R.id.cart_price);
            clearItem = (ImageView) itemView.findViewById(R.id.clear_item);
        }
    }
}
