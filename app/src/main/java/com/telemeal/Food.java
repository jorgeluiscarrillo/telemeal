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
    /**Required to implement paypal*/
    private String sku;

    /**The name of the food*/
    private String name;

    /**The price of the food*/
    private double price;

    /**The description of the food*/
    private String description;

    /**The image link that will show the image of the food*/
    private String image;

    /**The type of the food*/
    private FoodCategory category;

    /**
     * Required empty constructor for the database
     */
    public Food(){}

    /**
     *  Initializes the properties inside an employee object
     * @param s The food's sku
     * @param n The food's name
     * @param p The food's price
     * @param desc The food's description
     * @param img The food's image link
     * @param cat The food's category
     */
    public Food(String s, String n, double p, String desc, String img, FoodCategory cat)
    {
        sku = s;
        name = n;
        price = p;
        description = desc;
        image = img;
        category = cat;
    }


    /**
     * The new value for the sku
     * @param s the sku's new value
     */
    public void setSku(String s) {
        sku = s;
    }

    /**
     * The new value for the name
     * @param n the name's new value
     */
    public void setName(String n) {
        name = n;
    }

    /**
     * The new value for the price
     * @param p the price's new value
     */
    public void setPrice(double p)
    {
        price = p;
    }

    /**
     * The new value for the description
     * @param desc the description's new value
     */
    public void setDescription(String desc)
    {
        description = desc;
    }

    /**
     * The new value for the image
     * @param img the image's new value
     */
    public void setImage(String img)
    {
        image = img;
    }

    /**
     * The new value for the category
     * @param cat the category's new value
     */
    public void setCategory(FoodCategory cat)
    {
        category = cat;
    }

    /**
     * Obtain the foods current sku
     * @return the food's sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * Obtain the food's current name
     * @return the food's name
     */
    public String getName() {return name;}

    /**
     * Obtain the food's current price
     * @return the food's price
     */
    public Double getPrice()
    {
        return price;
    }

    /**
     * Obtain the food's current description
     * @return the food's description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Obtain the food's current image
     * @return the food's image
     */
    public String getImage()
    {
        return image;
    }

    /**
     * Obtain the food's current category
     * @return the food's category
     */
    public FoodCategory getCategory()
    {
        return category;
    }

    /**
     * Used to implement parcelable
     * @param in the parcel items
     */
    public Food(Parcel in) {
        super();
        readFromParcel(in);
    }

    /**
     * Required to implement parcelable
     */
    public static final Parcelable.Creator<Food> CREATOR = new Parcelable.Creator<Food>() {
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    /**
     * Places the properties inside the parcel into a food object
     * @param in
     */
    public void readFromParcel(Parcel in) {
        sku = in.readString();
        name = in.readString();
        price = in.readDouble();
        description = in.readString();
        image = in.readString();
        category = FoodCategory.valueOf(in.readString());
    }

    /**
     * Used for any child classes
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Used for when trying to transfer foods between activities
     * @param parcel the parcel the properties will be held in
     * @param i an iterator
     */
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
