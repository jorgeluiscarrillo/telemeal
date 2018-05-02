package com.telemeal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

        holder.edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setTitle("Confirm Customer Payment");
                builder.setMessage("Confirm that the customer has paid for their order.");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbOrders.orderByChild("orderID")
                                        .equalTo(orders.get(position).getOrderID())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot d : dataSnapshot.getChildren()) {
                                                    Order order = d.getValue(Order.class);
                                                    order.setCashPayment(false);
                                                    dbOrders.child(d.getKey()).setValue(order);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                orders.get(position).setCashPayment(false);
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
        });

        holder.trash_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setTitle("Removing Order");
                builder.setMessage("Are you sure you want to remove this order from the database?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbOrders.orderByChild("orderID")
                                        .equalTo(orders.get(position).getOrderID())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot d : dataSnapshot.getChildren()) {
                                                    Order order = d.getValue(Order.class);
                                                    dbOrders.child(d.getKey()).removeValue();
                                                    Toast.makeText(mContext,
                                                            "Order #" + order.getOrderID() + " was successfully deleted.",
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
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
        });

        if (orders.get(position).getCashPayment()) {
            holder.paymentStatus.setText("Pending ($" + String.format(Locale.getDefault(), "%.2f", orders.get(position).getSubTotal()) +")");
            holder.edit_icon.setVisibility(View.VISIBLE);
        } else {
            holder.paymentStatus.setText("Completed ($" + String.format(Locale.getDefault(), "%.2f", orders.get(position).getSubTotal()) +")");
            holder.edit_icon.setVisibility(View.GONE);
        }
    }

    public int getItemCount() { return orders.size(); }

    public static class OrderHolder extends RecyclerView.ViewHolder
    {
        TextView orderId;
        TextView timeOrdered;
        TextView paymentStatus;
        ImageView edit_icon;
        ImageView trash_icon;
        RecyclerView orderItems;

        public OrderHolder(View itemView) {
            super(itemView);

            orderId = (TextView) itemView.findViewById(R.id.order_id);
            timeOrdered = (TextView) itemView.findViewById(R.id.time_ordered);
            paymentStatus = (TextView) itemView.findViewById(R.id.payment_status);
            orderItems = (RecyclerView) itemView.findViewById(R.id.order_item_names);
            trash_icon = (ImageView) itemView.findViewById(R.id.order_delete);
            edit_icon = (ImageView) itemView.findViewById(R.id.order_edit);
        }
    }
}
