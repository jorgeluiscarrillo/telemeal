package com.telemeal;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bryan on 3/18/2018.
 */

public class listFoodAdapter extends RecyclerView.Adapter<listFoodAdapter.ListFoodHolder>
{
    Context mContext;
    ArrayList<Food> mfood;
    Food selectedFood;


    public listFoodAdapter(Context c, ArrayList<Food> f)
    {
        mContext = c;
        mfood = f;

    }

    @Override
    public ListFoodHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_food_layout,parent,false);
        final ListFoodHolder vHolder = new ListFoodHolder(v);

        vHolder.foodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*boolean inCart = false;
                for(int i = 0; i < cartItems.size(); i++)
                {
                    if(cartItems.get(i).getName().equals(mfood.get(vHolder.getAdapterPosition()).getName()))
                    {
                        int quantity = cartItems.get(i).getQuantity() + 1;
                        cartItems.get(i).setQuantity(quantity);

                        double price = cartItems.get(i).getPrice() + mfood.get(vHolder.getAdapterPosition()).getPrice();
                        cartItems.get(i).setPrice(price);
                        inCart = true;

                    }
                }
                if(!inCart)
                {
                    CartItem newItem = new CartItem(1,mfood.get(vHolder.getAdapterPosition()).getName(),mfood.get(vHolder.getAdapterPosition()).getPrice());
                    cartItems.add(newItem);
                }*/

                MenuActivity activity = (MenuActivity) mContext;

                selectedFood = mfood.get(vHolder.getAdapterPosition());
                activity.getItemCartFrag().AddItem(selectedFood);
            }
        });

        return vHolder;
    }


    @Override
    public void onBindViewHolder(ListFoodHolder holder, int position)
    {

        // Create a reference with an initial file path and name

        holder.vf_name.setText(mfood.get(position).getName());
        holder.vf_price.setText(String.format(Locale.getDefault(),"%.2f",mfood.get(position).getPrice()));
        holder.vf_cat.setText(mfood.get(position).getCategory().toString());
        holder.vf_desc.setText(mfood.get(position).getDescription());
    }

    public int getItemCount()
    {
        return mfood.size();
    }

    public static class ListFoodHolder extends RecyclerView.ViewHolder
    {
        private RelativeLayout foodItem;
        private ImageView vf_image;
        private TextView vf_name;
        private TextView vf_price;
        private TextView vf_cat;
        private TextView vf_desc;

        public ListFoodHolder(View itemView) {
            super(itemView);

            foodItem = (RelativeLayout) itemView.findViewById(R.id.list_food_layout);
            vf_image = (ImageView) itemView.findViewById(R.id.vf_foodPic);
            vf_name = (TextView) itemView.findViewById(R.id.vf_foodName);
            vf_price = (TextView) itemView.findViewById(R.id.vf_foodPrice);
            vf_cat = (TextView) itemView.findViewById(R.id.vf_category);
            vf_desc = (TextView) itemView.findViewById(R.id.vf_Description);
        }
    }
}
