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
    Context mContext;
    ArrayList<Employee> mEmp;
    Employee selectedEmployee;

    public listEmployeeAdapter(Context c, ArrayList<Employee> e)
    {
        mContext = c;
        mEmp = e;
    }

    @Override
    public ListEmployeeHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_employee_layout,parent,false);
        final ListEmployeeHolder vHolder = new ListEmployeeHolder(v);

        return vHolder;
    }


    @Override
    public void onBindViewHolder(ListEmployeeHolder holder, int position)
    {
        holder.ve_eid.setText(""+mEmp.get(position).getId());
        holder.ve_name.setText(mEmp.get(position).getName());
        holder.ve_position.setText(mEmp.get(position).getPosition());
        holder.ve_priv.setChecked(mEmp.get(position).getPrivilege());
    }

    public int getItemCount()
    {
        return mEmp.size();
    }

    public static class ListEmployeeHolder extends RecyclerView.ViewHolder
    {
        private TextView ve_eid;
        private TextView ve_name;
        private TextView ve_position;
        private CheckBox ve_priv;

        public ListEmployeeHolder(View itemView) {
            super(itemView);

            ve_eid = (TextView) itemView.findViewById(R.id.ve_tv_eid);
            ve_name = (TextView) itemView.findViewById(R.id.ve_tv_name);
            ve_position = (TextView) itemView.findViewById(R.id.ve_tv_position);
            ve_priv = (CheckBox) itemView.findViewById(R.id.ve_cb_priv);
        }
    }
}
