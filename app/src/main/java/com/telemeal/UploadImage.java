package com.telemeal;

/**
 * Created by daehe on 4/11/2018.
 */

public class UploadImage {
    String name;
    String imageUrl;

    public UploadImage(){

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
}
