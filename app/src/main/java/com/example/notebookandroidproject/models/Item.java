package com.example.notebookandroidproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Item implements Parcelable {
    /*
    Variables
    */
    private String itemID;
    private String userID;
    private String locationID;
    private String name;
    private String description;
    private double price;
    private String imageURL;
    private boolean isPurchased;

    /*
    Default Constructor
    */
    public Item() {
    }

    /*
    Constructor
    */
    public Item(String itemID, String userID, String locationID, String name, String description,
                double price, String imageURL, boolean isPurchased) {
        this.itemID = itemID;
        this.userID = userID;
        this.locationID = locationID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageURL = imageURL;
        this.isPurchased = isPurchased;
    }

    /*
    Constructor (Parcel).
    */
    protected Item(Parcel in) {
        itemID = in.readString();
        userID = in.readString();
        locationID = in.readString();
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
        imageURL = in.readString();
        isPurchased = in.readByte() != 0;
    }

    /*
    Tells android application how to create an object, and array of objects, of type item object
    from a Parcel
    */
    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    /*
    Setters
    */
    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

    /*
    Getters
    */
    public String getItemID() {
        return itemID;
    }

    public String getUserID() {
        return userID;
    }

    public String getLocationID() {
        return locationID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    /*
    Used to put FileDescriptor inside the parcel.
    */
    @Override
    public int describeContents() {
        return 0;
    }

    /*
    Used to create the Parcel object with the Item object.
    */
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(itemID);
        dest.writeString(userID);
        dest.writeString(locationID);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeString(imageURL);
        dest.writeByte((byte) (isPurchased ? 1 : 0));
    }
}
