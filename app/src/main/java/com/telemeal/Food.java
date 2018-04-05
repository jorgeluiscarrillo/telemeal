package com.telemeal;

/**
 * Created by Bryan on 3/15/2018.
 */

public class Food {
    //private String id;
    private String name;
    private double price;
    private String description;
    private String image;
    private FoodCategory category;

    public Food(){}

    public Food(String n, double p, String desc, String img, FoodCategory cat)
    {
        //id = s;
        name = n;
        price = p;
        description = desc;
        image = img;
        category = cat;
    }

    //public void setId(String s) {id = s;}

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

    //public String getId(){return id;}

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
