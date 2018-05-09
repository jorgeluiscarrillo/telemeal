package com.telemeal;

import java.util.ArrayList;
import java.util.Date;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by Bryan on 3/15/2018.
 */

public class Order implements Parcelable, Comparable<Order> {
    private int orderID;
    private double total;
    private double subTotal;
    private Date orderDateTime;
    private boolean isTakeOut;
    private ArrayList<Food> foods;
    private boolean cashPayment;

    // Required empty constructor
    public Order()
    {

    }

    /**
     * Constructor for Order object
     * @param oi Order id
     * @param tot Order total
     * @param st Order subtotal
     * @param odt Order time and date
     * @param take Whether order is a takeout order or not
     * @param f Food items in an Order
     * @param cp Whether order was paid with cash
     */
    public Order(int oi, double tot, double st, Date odt, boolean take, ArrayList<Food> f, boolean cp)
    {
        orderID = oi;
        total = tot;
        subTotal = st;
        orderDateTime = odt;
        isTakeOut = take;
        foods = f;
        cashPayment = cp;
    }

    /**
     * Set a unique order ID
     * @param oi Order id
     */
    public void setOrderID(int oi)
    {
        orderID = oi;
    }

    /**
     * Set the order total
     * @param tot Order total
     */
    public void setTotal(double tot)
    {
        total = tot;
    }

    /**
     * Set the order subtotal
     * @param st Order subtotal
     */
    public void setSubTotal(double st)
    {
        subTotal = st;
    }

    /**
     * Set the order data and time
     * @param date Order data and time
     */
    public void setDate(Date date)
    {
        orderDateTime = date;
    }

    /**
     * Set whether the order is a takeout
     * @param takeout Whether order is takeout or not
     */
    public void setTakeOut(boolean takeout)
    {
        isTakeOut = takeout;
    }

    /**
     * Set the foods in an order
     * @param f Food items in an order
     */
    public void setFoods(ArrayList<Food> f)
    {
        foods = f;
    }

    /**
     * Set whether an order is a cash payment
     * @param cp Whether an order was paid with cash
     */
    public void setCashPayment(Boolean cp) { cashPayment = cp; }

    /**
     * Order ID getter
     * @return Order ID
     */
    public int getOrderID()
    {
        return orderID;
    }

    /**
     * Order total getter
     * @return Order total
     */
    public double getTotal()
    {
        return total;
    }

    /**
     * Order subtotal getter
     * @return Order subtotal
     */
    public double getSubTotal()
    {
        return subTotal;
    }

    /**
     * Order date and time getter
     * @return Order date and time
     */
    public Date getDate()
    {
        return orderDateTime;
    }

    /**
     * Order takeout getter
     * @return Whether order is a takeout order
     */
    public boolean getTakeOut()
    {
        return isTakeOut;
    }

    /**
     * Getter for list of foods in an order
     * @return List of food in an order
     */
    public ArrayList<Food> getFoods()
    {
        return foods;
    }

    /**
     * Constructor for implementing Parcelable
     * @param in New Parcel for Order
     */
    public Order(Parcel in) {
        super();
        readFromParcel(in);
    }

    /**
     * Create a new Parcel for order
     */
    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    /**
     * Function to read Order data from a parcel
     * @param in Input Parcel data member
     */
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

    /**
     * If we plan to have child classes
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Function for writing Order data to a Parcel
     * @param parcel Parcel to write to
     * @param i Iterator
     */
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

    /**
     * Getter for cash payment
     * @return Whther order was paid with cash
     */
    public boolean getCashPayment() { return cashPayment; }

    /**
     * Override the compareTo to compare order date
     * @param order Order to compare
     * @return Whether an orers have equal date and time
     */
    @Override
    public int compareTo(@NonNull Order order) {
        return this.orderDateTime.compareTo(order.getDate());
    }
}
