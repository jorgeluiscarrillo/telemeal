package com.telemeal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jcarrillo on 4/25/18.
 */

public class SimpleViewFoodAdapter extends RecyclerView.Adapter<SimpleViewFoodAdapter.SimpleListFoodHolder>
{
    Context mContext;
    ArrayList<Food> mfood;

    public SimpleViewFoodAdapter(Context c, ArrayList<Food> f)
    {
        mContext = c;
        mfood = f;
    }

    @Override
    public SimpleViewFoodAdapter.SimpleListFoodHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.simple_order_view, parent,false);
        return new SimpleListFoodHolder(v);
    }

    @Override
    public void onBindViewHolder(SimpleViewFoodAdapter.SimpleListFoodHolder holder, int position)
    {
        holder.svf_name.setText(mfood.get(position).getName());
    }

    public int getItemCount()
    {
        return mfood.size();
    }

    public static class SimpleListFoodHolder extends RecyclerView.ViewHolder
    {
        private TextView svf_name;

        public SimpleListFoodHolder(View itemView) {
            super(itemView);

            svf_name = (TextView) itemView.findViewById(R.id.svf_foodName);

        }
    }
}
