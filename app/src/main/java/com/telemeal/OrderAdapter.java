package com.telemeal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
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

    /**
     * Constructor for Order adapter.
     * @param c Application context
     * @param o List of recent orders
     * @param db Reference to Firestore Database
     */
    public OrderAdapter(Context c, ArrayList<Order> o, DatabaseReference db) {
        mContext = c;
        orders = o;
        dbOrders = db;
    }

    /**
     * Initialize the View holders when the adapter is created.
     * @param parent Parent view
     * @param viewType View type
     * @return Created holder object
     */
    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.order_items_layout, parent, false);
        return new OrderHolder(view);
    }

    /**
     * View components to be displayed for each recycler view item
     * @param holder Stores each of the component views
     * @param position Position of item in recycler view
     */
    @Override
    public void onBindViewHolder(final OrderHolder holder, final int position)
    {
        final SimpleViewFoodAdapter adapter = new SimpleViewFoodAdapter(mContext, orders.get(position).getFoods());

        holder.orderId.setText(String.format(Locale.getDefault(), "%d", orders.get(position).getOrderID()));
        holder.timeOrdered.setText(": " + orders.get(position).getDate().toString());
        holder.orderItems.setLayoutManager(new LinearLayoutManager(mContext));
        holder.orderItems.setAdapter(adapter);


        holder.dropdown_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, holder.dropdown_menu);
                popup.inflate(R.menu.order_options_menu);

                if (orders.get(position).getCashPayment()) {
                    popup.getMenu().findItem(R.id.confirm_order_menu).setVisible(true);
                } else {
                    popup.getMenu().findItem(R.id.confirm_order_menu).setVisible(false);
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.confirm_order_menu:
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                                builder1.setCancelable(true);
                                builder1.setTitle("Confirm Customer Payment");
                                builder1.setMessage("Confirm that the customer has paid for their order.");
                                builder1.setPositiveButton("Confirm",
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
                                builder1.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                AlertDialog dialog1 = builder1.create();
                                dialog1.show();
                                break;
                            case R.id.delete_order_menu:
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
                                builder2.setCancelable(true);
                                builder2.setTitle("Removing Order");
                                builder2.setMessage("Are you sure you want to remove this order from the database?");
                                builder2.setPositiveButton("Confirm",
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
                                builder2.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                AlertDialog dialog2 = builder2.create();
                                dialog2.show();

                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

        if (orders.get(position).getCashPayment()) {
            holder.paymentStatus.setText("Pending ($" + String.format(Locale.getDefault(), "%.2f", orders.get(position).getSubTotal()) +")");
        } else {
            holder.paymentStatus.setText("Completed ($" + String.format(Locale.getDefault(), "%.2f", orders.get(position).getSubTotal()) +")");
        }

        if (orders.get(position).getTakeOut()) {
            holder.orderType.setText("Takeout");
        } else {
            holder.orderType.setText("Dine-in");
        }
    }

    /**
     * Getter for order list size
     * @return Size of orders list
     */
    public int getItemCount() { return orders.size(); }

    /**
     * Holder class for constructing and initializing recycler view components
     */
    public static class OrderHolder extends RecyclerView.ViewHolder
    {
        TextView orderId;
        TextView timeOrdered;
        TextView paymentStatus;
        TextView orderType;
        ImageView dropdown_menu;
        RecyclerView orderItems;

        public OrderHolder(View itemView) {
            super(itemView);

            orderId = (TextView) itemView.findViewById(R.id.order_id);
            timeOrdered = (TextView) itemView.findViewById(R.id.time_ordered);
            paymentStatus = (TextView) itemView.findViewById(R.id.payment_status);
            orderType = (TextView) itemView.findViewById(R.id.order_type);
            orderItems = (RecyclerView) itemView.findViewById(R.id.order_item_names);
            dropdown_menu = (ImageView) itemView.findViewById(R.id.order_dropdown);

        }
    }
}
