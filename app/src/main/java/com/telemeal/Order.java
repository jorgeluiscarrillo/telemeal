package com.telemeal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bryan on 3/15/2018.
 */

public class Order {
    private long orderID;
    private double total;
    private double salesTax;
    private Date orderDateTime;
    private boolean isTakeOut;
    private ArrayList<Food> foods;
    //private boolean paidwithppl;

    public Order()
    {

    }

    public Order(long oi, double tot, double st, Date odt, boolean take, ArrayList<Food> f)
    {
        orderID = oi;
        total = tot;
        st = salesTax;
        orderDateTime = odt;
        isTakeOut = take;
        foods = f;
    }

    public void setOrderID(long oi)
    {
        orderID = oi;
    }

    public void setTotal(double tot)
    {
        total = tot;
    }

    public void setSalesTax(double st)
    {
        salesTax = st;
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

    public double getSalesTax()
    {
        return salesTax;
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

    public double SubTotal()
    {
        return total+total*salesTax;
    }
}
