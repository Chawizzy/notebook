package com.example.notebookandroidproject.models;

public class User {
    /*
    Variables
    */
    private String userID;
    private String fullName;
    private String gmailAddress;
    private String password;

    /*
    Default Constructor
    */
    public User() {
    }

    /*
    Constructor
    */
    public User(String userID, String fullName, String gmailAddress, String password) {
        this.userID = userID;
        this.fullName = fullName;
        this.gmailAddress = gmailAddress;
        this.password = password;
    }

    /*
    Setters
    */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setGmailAddress(String gmailAddress) {
        this.gmailAddress = gmailAddress;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*
    Getters
    */
    public String getUserID() {
        return userID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGmailAddress() {
        return gmailAddress;
    }

    public String getPassword() {
        return password;
    }
}
