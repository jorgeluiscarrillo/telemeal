package com.telemeal;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by Bryan on 3/15/2018.
 */
public class Food implements Parcelable {
    private String sku;
    private String name;
    private double price;
    private String description;
    private String image;
    private FoodCategory category;

    public Food(){}

    public Food(String s, String n, double p, String desc, String img, FoodCategory cat)
    {
        sku = s;
        name = n;
        price = p;
        description = desc;
        image = img;
        category = cat;
    }

    public void setSku(String s) {
        sku = s;
    }

    public void setName(String n) {
        name = n;
    }

    public void setPrice(double p)
    {
        price = p;
    }

    public void setDescription(String desc)
    {
        description = desc;
    }

    public void setImage(String img)
    {
        image = img;
    }

    public void setCategory(FoodCategory cat)
    {
        category = cat;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {return name;}

    public Double getPrice()
    {
        return price;
    }

    public String getDescription()
    {
        return description;
    }

    public String getImage()
    {
        return image;
    }

    public FoodCategory getCategory()
    {
        return category;
    }

    public Food(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<Food> CREATOR = new Parcelable.Creator<Food>() {
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public void readFromParcel(Parcel in) {
        sku = in.readString();
        name = in.readString();
        price = in.readDouble();
        description = in.readString();
        image = in.readString();
        category = FoodCategory.valueOf(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sku);
        parcel.writeString(name);
        parcel.writeDouble(price);
        parcel.writeString(description);
        parcel.writeString(image);
        parcel.writeString(category.toString());
    }
}
