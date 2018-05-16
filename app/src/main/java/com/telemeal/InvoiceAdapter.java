package com.telemeal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Bryan on 3/18/2018.
 */

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ListInvoiceHolder>
{
    Context mContext;
    ArrayList<Order> mOrder;
    Order order;

    /**
     * Constructor
     * @param c Context which adapter will be set
     * @param e List of object to show in the view
     */
    public InvoiceAdapter(Context c, ArrayList<Order> e)
    {
        mContext = c;
        mOrder = e;
    }

    /**
     * Create View Holder when the adapter is created
     * @param parent parent where View group is in
     * @param viewType Integer
     * @return customized view holder
     */
    @Override
    public ListInvoiceHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.invoice_layout,parent,false);
        final ListInvoiceHolder vHolder = new ListInvoiceHolder(v);

        return vHolder;
    }


    /**
     * connects the data to view holder created
     * @param holder created view holder
     * @param position position of item to be selected
     */
    @Override
    public void onBindViewHolder(ListInvoiceHolder holder, int position)
    {
        //set the ID
        holder.invoice_id.setText(String.format ("%04d", mOrder.get(position).getOrderID()));
        //formatting the date
        SimpleDateFormat dt = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        dt.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        String date = dt.format(mOrder.get(position).getDate());
        //set the Date
        holder.invoice_date.setText(date);
        //set the total
        holder.invoice_total.setText(String.format ("%.2f", mOrder.get(position).getSubTotal()));
    }

    /**
     * Count the number of items in the list
     * @return Integer value representing the size of the list
     */
    public int getItemCount()
    {
        return mOrder.size();
    }

    /**
     * Get the item in the list
     * @param position the position of the item to be retrieved
     * @return Object which list hold in the position
     */
    public Order getItem(int position){
        return mOrder.get(position);
    }

    /**
     * Inner class for customized view holder
     */
    public static class ListInvoiceHolder extends RecyclerView.ViewHolder
    {
        private TextView invoice_id;
        private TextView invoice_date;
        private TextView invoice_total;

        public ListInvoiceHolder(View itemView) {
            super(itemView);

            invoice_id = (TextView) itemView.findViewById(R.id.invoice_id);
            invoice_date = (TextView) itemView.findViewById(R.id.invoice_date);
            invoice_total = (TextView) itemView.findViewById(R.id.invoice_total);
        }
    }
}
