package com.telemeal;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CartItem implements Parcelable {
    private int quantity;
    private String name;
    private double price;
    private String sku;

    public CartItem(int q, String n, double p, String s)
    {
        quantity = q;
        name = n;
        price = p;
        sku = s;
    }

    public void setQuantity(int q)
    {
        quantity = q;
    }

    public void setName(String n)
    {
        name = n;
    }

    public void setPrice(double p)
    {
        price = p;
    }

    public void setSku(String s) {
        sku = s;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public String getName()
    {
        return name;
    }

    public Double getPrice()
    {
        return price;
    }

    public String getSku() {
        return sku;
    }

    public CartItem(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<CartItem> CREATOR = new Parcelable.Creator<CartItem>() {
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    public void readFromParcel(Parcel in) {
        quantity = in.readInt();
        name = in.readString();
        price = in.readDouble();
        sku = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(quantity);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeString(sku);
    }
}
