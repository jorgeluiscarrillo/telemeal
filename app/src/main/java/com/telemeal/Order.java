package com.telemeal;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bryan on 3/15/2018.
 */

public class Order implements Parcelable {
    private int orderID;
    private double total;
    private double subTotal;
    private Date orderDateTime;
    private boolean isTakeOut;
    private ArrayList<Food> foods;
    //private boolean paidwithppl;

    public Order()
    {

    }

    public Order(int oi, double tot, double st, Date odt, boolean take, ArrayList<Food> f)
    {
        orderID = oi;
        total = tot;
        st = subTotal;
        orderDateTime = odt;
        isTakeOut = take;
        foods = f;
    }

    public void setOrderID(int oi)
    {
        orderID = oi;
    }

    public void setTotal(double tot)
    {
        total = tot;
    }

    public void setSubTotal(double st)
    {
        subTotal = st;
    }

    public void setDate(Date date)
    {
        orderDateTime = date;
    }

    public void setTakeOut(boolean takeout)
    {
        isTakeOut = takeout;
    }

    public void setFoods(ArrayList<Food> f)
    {
        foods = f;
    }

    public long getOrderID()
    {
        return orderID;
    }

    public double getTotal()
    {
        return total;
    }

    public double getSubTotal()
    {
        return subTotal;
    }

    public Date getDate()
    {
        return orderDateTime;
    }

    public boolean getTakeOut()
    {
        return isTakeOut;
    }

    public ArrayList<Food> getFoods()
    {
        return foods;
    }

    public Order(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public void readFromParcel(Parcel in) {
        orderID = in.readInt();
        total = in.readDouble();
        subTotal = in.readDouble();
        orderDateTime = new Date(in.readLong());
        boolean[] b = new boolean[1];
        in.readBooleanArray(b);
        isTakeOut = b[0];
        foods = in.readArrayList(Food.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(orderID);
        parcel.writeDouble(total);
        parcel.writeDouble(subTotal);
        parcel.writeLong(orderDateTime.getTime());
        boolean[] b = {isTakeOut};
        parcel.writeBooleanArray(b);
        parcel.writeList(foods);
    }
}
