package com.telemeal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Bryan on 3/19/2018.
 */

public class ItemCartAdapter extends RecyclerView.Adapter<ItemCartAdapter.ItemCartHolder> {
    Context mContext;
    ArrayList<CartItem> mCart;
    private int selectedPos = RecyclerView.NO_POSITION;
    private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds
    private CartItem selected;
    long lastClickTime = 0;

    public ItemCartAdapter(Context c, ArrayList<CartItem> ca)
    {
        mContext = c;
        mCart = ca;
    }

    @Override
    public ItemCartHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_cart_layout,parent,false);
        final ItemCartHolder vHolder = new ItemCartHolder(v);
        vHolder.CartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                    if(selected != null)
                    {
                        if(mCart.get(vHolder.getAdapterPosition()).getQuantity()>1)
                        {
                            int current =  mCart.get(vHolder.getAdapterPosition()).getQuantity() - 1;
                            mCart.get(vHolder.getAdapterPosition()).setQuantity(current);
                            notifyDataSetChanged();
                        }
                        else
                        {
                            if(selected != null)
                            {
                                selected = null;
                                vHolder.CartItem.setBackgroundResource(R.color.colorBeige);
                                mCart.remove(vHolder.getAdapterPosition());
                                notifyItemRemoved(vHolder.getAdapterPosition());
                            }
                        }
                    }
                } else {
                    selected = mCart.get(vHolder.getAdapterPosition());
                    notifyItemChanged(selectedPos);
                    selectedPos = vHolder.getLayoutPosition();
                    vHolder.CartItem.setBackgroundResource(R.drawable.highlight);
                    notifyItemChanged(selectedPos);
                }
                lastClickTime = clickTime;
            }
        });



        return vHolder;
    }


    @Override
    public void onBindViewHolder(ItemCartAdapter.ItemCartHolder holder, int position)
    {
        holder.tv_quantity.setText(String.valueOf(mCart.get(position).getQuantity()));
        holder.tv_name.setText(mCart.get(position).getName());
        holder.tv_price.setText(String.format(Locale.getDefault(),"%.2f",mCart.get(position).getPrice()));
        holder.itemView.setSelected(selectedPos == position);
    }

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

        public ItemCartHolder(View itemView) {
            super(itemView);
            CartItem = (LinearLayout) itemView.findViewById(R.id.cart_item);
            tv_quantity = (TextView) itemView.findViewById(R.id.cart_quantity);
            tv_name = (TextView) itemView.findViewById(R.id.cart_name);
            tv_price = (TextView) itemView.findViewById(R.id.cart_price);
        }
    }
}
