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

    public UploadImage() {
    }

    public UploadImage(String name, String image){
        if(name.trim().equals("")){
            name = "No Name";
        }

        this.name = name;
        imageUrl = image;
    }

    public String getName(){return name;}
    public String getImageUrl() {return imageUrl;}

    public void setName(String name){this.name = name;}
    public void setImageUrl(String image){imageUrl = image;}

    @Exclude
    public String getKey(){return mkey;}
    @Exclude
    public void setKey(String key){mkey = key;}

    public UploadImage(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<UploadImage> CREATOR = new Parcelable.Creator<UploadImage>() {
        public UploadImage createFromParcel(Parcel in) {
            return new UploadImage(in);
        }

        public UploadImage[] newArray(int size) {
            return new UploadImage[size];
        }
    };

    public void readFromParcel(Parcel in) {
        name = in.readString();
        imageUrl = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageUrl);
    }
}
