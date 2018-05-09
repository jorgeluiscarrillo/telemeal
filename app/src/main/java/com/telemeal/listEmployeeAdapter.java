package com.telemeal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Bryan on 3/18/2018.
 */

public class listEmployeeAdapter extends RecyclerView.Adapter<listEmployeeAdapter.ListEmployeeHolder>
{
    /**The context this adapter is in*/
    Context mContext;

    /**The employees that will be listed*/
    ArrayList<Employee> mEmp;

    /**
     * Initializes the listEmployeeAdapter
     * @param c the context that the adapter will be held in
     * @param e the list of employees that will be shown
     */
    public listEmployeeAdapter(Context c, ArrayList<Employee> e)
    {
        mContext = c;
        mEmp = e;
    }

    /**
     * Creates the view that the employees that will be shown in
     * @param parent the view group that the employees are in
     * @param viewType an integer
     * @return
     */
    @Override
    public ListEmployeeHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_employee_layout,parent,false);
        final ListEmployeeHolder vHolder = new ListEmployeeHolder(v);

        return vHolder;
    }


    /**
     * Binds the employee's data to the parameters inside the view holder
     * @param holder the view holder employee information will be held in
     * @param position the position of the view holder
     */
    @Override
    public void onBindViewHolder(ListEmployeeHolder holder, int position)
    {
        holder.ve_eid.setText(""+mEmp.get(position).getId());
        holder.ve_name.setText(mEmp.get(position).getName());
        holder.ve_position.setText(mEmp.get(position).getPosition());
        holder.ve_priv.setChecked(mEmp.get(position).getPrivilege());
    }

    /**
     * Obtains the size of the employee list
     * @return the size of the employee list
     */
    public int getItemCount()
    {
        return mEmp.size();
    }

    /**
     * View holder to show employee information
     */
    public static class ListEmployeeHolder extends RecyclerView.ViewHolder
    {
        /**Shows employee's id*/
        private TextView ve_eid;

        /**Shows the employee's name*/
        private TextView ve_name;

        /**Shows the employee's postion*/
        private TextView ve_position;

        /**Shows the employee's privilege*/
        private CheckBox ve_priv;

        /**
         * Constructor to initialize the employee view holder
         * @param itemView the view that the components will be held in
         */
        public ListEmployeeHolder(View itemView) {
            super(itemView);

            ve_eid = (TextView) itemView.findViewById(R.id.ve_tv_eid);
            ve_name = (TextView) itemView.findViewById(R.id.ve_tv_name);
            ve_position = (TextView) itemView.findViewById(R.id.ve_tv_position);
            ve_priv = (CheckBox) itemView.findViewById(R.id.ve_cb_priv);
        }
    }
}
