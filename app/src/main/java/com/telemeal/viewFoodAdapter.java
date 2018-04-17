package com.telemeal;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bryan on 3/18/2018.
 */

public class viewFoodAdapter extends RecyclerView.Adapter<viewFoodAdapter.ListFoodHolder>
{
    Context mContext;
    ArrayList<Food> mfood;
    Food selectedFood;
    ArrayList<UploadImage> mImage;


    public viewFoodAdapter(Context c, ArrayList<Food> f, ArrayList<UploadImage> i)
    {
        mContext = c;
        mfood = f;
        mImage = i;
    }

    @Override
    public ListFoodHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview_item_food, parent,false);
        final ListFoodHolder vHolder = new ListFoodHolder(v);
        return vHolder;
    }


    @Override
    public void onBindViewHolder(ListFoodHolder holder, int position)
    {
        for(int i = 0; i < mImage.size(); i++)
        {
            if(mfood.get(position).getName().equals(mImage.get(i).getName()))
            {
                Picasso.get()
                        .load(mImage.get(i).getImageUrl())
                        .fit()
                        .centerCrop()
                        .into(holder.vf_image);

                break;

            }
        }
        holder.vf_name.setText(mfood.get(position).getName());
        holder.vf_price.setText(String.format(Locale.getDefault(),"%.2f",mfood.get(position).getPrice()));
    }

    public int getItemCount()
    {
        return mfood.size();
    }

    public static class ListFoodHolder extends RecyclerView.ViewHolder
    {
        private CardView foodItem;
        private ImageView vf_image;
        private TextView vf_name;
        private TextView vf_price;
        private TextView vf_cat;
        private TextView vf_desc;

        public ListFoodHolder(View itemView) {
            super(itemView);

            foodItem = (CardView) itemView.findViewById(R.id.card_view_layout);
            vf_image = (ImageView) itemView.findViewById(R.id.vf_foodPic);
            vf_name = (TextView) itemView.findViewById(R.id.vf_foodName);
            vf_price = (TextView) itemView.findViewById(R.id.vf_foodPrice);
//            vf_cat = (TextView) itemView.findViewById(R.id.vf_category);
//            vf_desc = (TextView) itemView.findViewById(R.id.vf_Description);
        }
    }
}
