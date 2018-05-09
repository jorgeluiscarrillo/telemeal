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

    /**
     * Constructor for a Recycler view on the Employee Order page
     * @param c Application context
     * @param f Food item in an order
     */
    public SimpleViewFoodAdapter(Context c, ArrayList<Food> f)
    {
        mContext = c;
        mfood = f;
    }

    /**
     * Initialize the View holders when the adapter is created.
     * @param parent Parent view
     * @param viewType View type
     * @return Created holder object
     */
    @Override
    public SimpleViewFoodAdapter.SimpleListFoodHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.simple_order_view, parent,false);
        return new SimpleListFoodHolder(v);
    }

    /**
     * View components to be displayed for each recycler view item
     * @param holder Stores each of the component views
     * @param position Position of item in recycler view
     */
    @Override
    public void onBindViewHolder(SimpleViewFoodAdapter.SimpleListFoodHolder holder, int position)
    {
        holder.svf_name.setText(mfood.get(position).getName());
    }

    /**
     * Getter for Items in an order
     * @return Size of food list
     */
    public int getItemCount()
    {
        return mfood.size();
    }

    /**
     * Holder class for constructing and initializing recycler view components
     */
    public static class SimpleListFoodHolder extends RecyclerView.ViewHolder
    {
        private TextView svf_name;

        public SimpleListFoodHolder(View itemView) {
            super(itemView);

            svf_name = (TextView) itemView.findViewById(R.id.svf_foodName);

        }
    }
}
