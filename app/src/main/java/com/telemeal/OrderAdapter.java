package com.telemeal;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Bryan on 4/18/2018.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    private Context mContext;
    private ArrayList<Order> orders;
    private ArrayList<UploadImage> images;

    public OrderAdapter(Context c, ArrayList<Order> o, ArrayList<UploadImage> i) {
        mContext = c;
        orders = o;
        images = i;
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.order_items_layout, parent, false);
        final OrderHolder vHolder = new OrderHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, int position)
    {
        holder.timeOrdered.setText(orders.get(position).getDate().toString());
        listFoodAdapter adapter = new listFoodAdapter(mContext, orders.get(position).getFoods(), images);
        holder.orderedFoods.setLayoutManager(new GridLayoutManager(mContext, 4));
        holder.orderedFoods.setAdapter(adapter);
    }

    public int getItemCount() { return orders.size(); }

    public static class OrderHolder extends RecyclerView.ViewHolder
    {
        private TextView timeOrdered;
        private RecyclerView orderedFoods;

        public OrderHolder(View itemView) {
            super(itemView);

            timeOrdered = (TextView) itemView.findViewById(R.id.time_ordered);
            orderedFoods = (RecyclerView) itemView.findViewById(R.id.orders);

        }
    }
}
