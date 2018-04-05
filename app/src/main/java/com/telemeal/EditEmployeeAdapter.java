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

    public EditEmployeeAdapter(Context context, int textViewResourceId,
                               ArrayList<Employee> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public Employee getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setText(values.get(position).getName());
        label.setTextSize(30);
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setText(values.get(position).getName());
        label.setTextSize(30);
        return label;
    }
}
