package com.telemeal;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by daehe on 3/21/2018.
 */

public class EditEmployeeAdapter extends ArrayAdapter<Employee> {
    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private ArrayList<Employee> values;

    /**
     * Constructor
     * @param context Context which adapter will be set
     * @param textViewResourceId Text format that something will be shown
     * @param values List of object to show in the view
     */
    public EditEmployeeAdapter(Context context, int textViewResourceId,
                               ArrayList<Employee> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    /**
     * Count the number of Employee object in the values
     * @return Integer value representing number of objects in the values
     */
    @Override
    public int getCount(){
        return values.size();
    }

    /**
     * Get the Employee object from the values in the position
     * @param position Integer value representing the index which Employee object is in
     * @return Employee object in the position of values
     */
    @Override
    public Employee getItem(int position){
        return values.get(position);
    }

    /**
     * Get the ID of the Employee object in the position of values
     * @param position Integer value representing the index which Employee object is in
     * @return Integer value representing the ID variable of the Employee
     */
    @Override
    public long getItemId(int position){
        return position;
    }

    /**
     * Default view when the spinner is not clicked.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setText(values.get(position).getName());
        label.setTextSize(30);
        return label;
    }

    /**
     * View when the spinner is clicked. Format of Drop Down List
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setText(values.get(position).getName());
        label.setTextSize(30);
        return label;
    }
}