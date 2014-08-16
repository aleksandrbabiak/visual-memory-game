package com.visualmemory.database.entity;

/**
 * Created by Windows on 02.04.14.
 */
public class User {

    private int globalUserID=-1;
    private long userID=-1;
    private String userName;

    public void setGlobalUserID(int globalUserID) {
        this.globalUserID = globalUserID;
    }


    public int getGlobalUserID() {
        return globalUserID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }


}
