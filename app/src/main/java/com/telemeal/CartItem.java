package com.telemeal;

/**
 * Created by Bryan on 3/19/2018.
 */

public class CartItem {
    private int quantity;
    private String name;
    private double price;

    public CartItem() {}

    public CartItem(int q, String n, double p)
    {
        quantity = q;
        name = n;
        price = p;
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
}
