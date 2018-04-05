package com.telemeal;

import java.io.Serializable;

/**
 * Created by Bryan on 3/15/2018.
 */
public class Food implements Serializable {
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

    public String getName()
    {
        return name;
    }

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
}
