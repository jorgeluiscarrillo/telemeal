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

    public InvoiceAdapter(Context c, ArrayList<Order> e)
    {
        mContext = c;
        mOrder = e;
    }

    @Override
    public ListInvoiceHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.invoice_layout,parent,false);
        final ListInvoiceHolder vHolder = new ListInvoiceHolder(v);

        return vHolder;
    }


    @Override
    public void onBindViewHolder(ListInvoiceHolder holder, int position)
    {
        holder.invoice_id.setText(""+mOrder.get(position).getOrderID());
        SimpleDateFormat dt = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        dt.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        String date = dt.format(mOrder.get(position).getDate());
        holder.invoice_date.setText(date);
        holder.invoice_total.setText(""+mOrder.get(position).getSubTotal());
    }

    public int getItemCount()
    {
        return mOrder.size();
    }

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
