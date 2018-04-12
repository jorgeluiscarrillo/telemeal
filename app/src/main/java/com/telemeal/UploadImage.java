package com.telemeal;

/**
 * Created by daehe on 4/11/2018.
 */

public class UploadImage {
    String mName;
    String mImageUrl;

    public UploadImage(){

    }

    public UploadImage(String name, String image){
        if(name.trim().equals("")){
            mName = "No Name";
        }

        mName = name;
        mImageUrl = image;
    }

    public String getName(){return mName;}
    public String getImageUrl() {return mImageUrl;}

    public void setName(String name){mName = name;}
    public void setImageUrl(String image){mImageUrl = image;}
}
