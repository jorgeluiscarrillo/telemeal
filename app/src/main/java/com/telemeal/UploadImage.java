package com.telemeal;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * Created by daehe on 4/11/2018.
 */

public class UploadImage implements Parcelable {
    private String name;
    private String imageUrl;
    private String mkey;

    /**
     * Default empty constructor required for Firebase
     */
    public UploadImage() {
    }

    /**
     * Create Image constructor
     * @param name Image file name
     * @param image Image URL
     */
    public UploadImage(String name, String image){
        if(name.trim().equals("")){
            name = "No Name";
        }

        this.name = name;
        imageUrl = image;
    }

    /**
     * Getter for Image Filename
     * @return Image filename
     */
    public String getName(){return name;}

    /**
     * Getting for Image URL
     * @return Image URL
     */
    public String getImageUrl() {return imageUrl;}

    /**
     * Setter for Image Filename
     * @param name Image filename
     */
    public void setName(String name){this.name = name;}

    /**
     * Setter for Image URL
     * @param image Image URL
     */
    public void setImageUrl(String image){imageUrl = image;}

    /**
     * Exclude Firebase key from image to avoid duplicate entries.
     * @return Image Firebase key
     */
    @Exclude
    public String getKey(){return mkey;}

    /**
     * Setter for Image Firebase key
     * @param key Image Firebase key
     */
    @Exclude
    public void setKey(String key){mkey = key;}

    /**
     * Constructor for implementing Parcelable
     * @param in New Parcel for Image
     */
    public UploadImage(Parcel in) {
        super();
        readFromParcel(in);
    }

    /**
     * Create a new Parcel for Image
     */
    public static final Parcelable.Creator<UploadImage> CREATOR = new Parcelable.Creator<UploadImage>() {
        public UploadImage createFromParcel(Parcel in) {
            return new UploadImage(in);
        }

        public UploadImage[] newArray(int size) {
            return new UploadImage[size];
        }
    };

    /**
     * Function to read Image data from a parcel
     * @param in Input Parcel data member
     */
    public void readFromParcel(Parcel in) {
        name = in.readString();
        imageUrl = in.readString();
    }

    /**
     * If we plan to have child classes
     * @return 0
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Function for writing Image data to a Parcel
     * @param dest Parcel to write to
     * @param flags Iterator
     */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageUrl);
    }
}
