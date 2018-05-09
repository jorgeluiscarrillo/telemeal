package com.telemeal;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class CartItem implements Parcelable {
    /**The number of food items inside this cart item*/
    private int quantity;

    /**The name of the cart item - uses a food item's name*/
    private String name;

    /**The price of the cart item*/
    private double price;

    /**Used for paypal - uses a food item's sku*/
    private String sku;

    /**
     * Constructor to initalize the properties of a cart item
     * @param q cart item's quantity
     * @param n cart item's name
     * @param p cart item's price
     * @param s cart item's sku
     */
    public CartItem(int q, String n, double p, String s)
    {
        quantity = q;
        name = n;
        price = p;
        sku = s;
    }

    /**
     * Sets the quantity to q
     * @param q the new quantity
     */
    public void setQuantity(int q)
    {
        quantity = q;
    }

    /**
     * Sets the name to n
     * @param n the new name
     */
    public void setName(String n)
    {
        name = n;
    }

    /**
     * Sets the price to p
     * @param p the new price
     */
    public void setPrice(double p)
    {
        price = p;
    }

    /**
     * Sets the sku to s
     * @param s the new sku
     */
    public void setSku(String s) {
        sku = s;
    }

    /**
     * Obtain the current cart item quantity
     * @return the quantity
     */
    public int getQuantity()
    {
        return quantity;
    }

    /**
     * Obtain the current cart item name
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Obtain the current cart item price
     * @return the price
     */
    public Double getPrice()
    {
        return price;
    }

    /**
     * Obtain the current cart item sku sku
     * @return the sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * Required when implementing parcelable to transfer data between activities
     * @param in the parcel that holds a cart item data
     */
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

    /**
     * Reads data from parcel to create and initialize a new cart item object
     * @param in the parcel that holds a cart item data
     */
    public void readFromParcel(Parcel in) {
        quantity = in.readInt();
        name = in.readString();
        price = in.readDouble();
        sku = in.readString();
    }

    /**
     * Required to implement parcelable
     * @return 0
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Writes data to a parcel
     * @param dest the parcel that is written to
     * @param flags an integer
     */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(quantity);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeString(sku);
    }
}
