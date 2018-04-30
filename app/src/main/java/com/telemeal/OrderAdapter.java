package com.telemeal;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Bryan on 4/18/2018.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    private Context mContext;
    private ArrayList<Order> orders;
    private DatabaseReference dbOrders;

    public OrderAdapter(Context c, ArrayList<Order> o, DatabaseReference db) {
        mContext = c;
        orders = o;
        dbOrders = db;
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.order_items_layout, parent, false);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, final int position)
    {
        final SimpleViewFoodAdapter adapter = new SimpleViewFoodAdapter(mContext, orders.get(position).getFoods());
        holder.orderId.setText(String.format(Locale.getDefault(), "%d", orders.get(position).getOrderID()));
        holder.timeOrdered.setText(": " + orders.get(position).getDate().toString());
        holder.orderItems.setLayoutManager(new LinearLayoutManager(mContext));
        holder.orderItems.setAdapter(adapter);

        holder.trash_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dbOrders.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            String dbOrderID = d.child("orderID").getValue().toString();
                            if (d.child("orderID") != null) {
                                if (dbOrderID.equals(Integer.toString(orders.get(position).getOrderID()))) {
                                    dbOrders.child(d.getKey()).removeValue();
                                    Toast.makeText(v.getContext(),
                                            "Order #" + dbOrderID + " was successfully deleted.",
                                            Toast.LENGTH_LONG).show();
                                    notifyDataSetChanged();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        if (orders.get(position).getCashPayment()) {
            holder.paymentStatus.setText("Pending ($" + String.format(Locale.getDefault(), "%.2f", orders.get(position).getSubTotal()) +")");
        } else {
            holder.paymentStatus.setText("Completed ($" + String.format(Locale.getDefault(), "%.2f", orders.get(position).getSubTotal()) +")");
        }
    }

    public int getItemCount() { return orders.size(); }

    public static class OrderHolder extends RecyclerView.ViewHolder
    {
        TextView orderId;
        TextView timeOrdered;
        TextView paymentStatus;
        ImageView trash_icon;
        RecyclerView orderItems;

        public OrderHolder(View itemView) {
            super(itemView);

            orderId = (TextView) itemView.findViewById(R.id.order_id);
            timeOrdered = (TextView) itemView.findViewById(R.id.time_ordered);
            paymentStatus = (TextView) itemView.findViewById(R.id.payment_status);
            orderItems = (RecyclerView) itemView.findViewById(R.id.order_item_names);
            trash_icon = (ImageView) itemView.findViewById(R.id.order_delete);
        }
    }
}
