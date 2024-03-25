package com.example.notebookandroidproject.models;

public class ItemLocation {
    /*
    Variables
    */
    private String locationID;
    private double latitude;
    private double longitude;

    /*
    Default Constructor
    */
    public ItemLocation() {
    }

    /*
    Constructor
    */
    public ItemLocation(String locationID, double latitude, double longitude) {
        this.locationID = locationID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /*
    Setters
    */
    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /*
    Getters
    */
    public String getLocationID() {
        return locationID;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
