package com.visualmemory.game;


import android.util.Log;
/**
 * Created by Windows on 11.03.14.
 */
public class DataCurrentUser {
    private static final String LOG_TAG = "DataCurrentUserLog";
    private static DataCurrentUser dataUser = new DataCurrentUser();

    public  boolean allDataAreFilled = false;

    public  long userID = -1;
    public  String userName = null;



    public int globalUserID = -1;

    public  int errorPause = -1;
    public  int showPause = -1;
    public int durationGame = -1;

    private DataCurrentUser(){

    }

    public void setGlobalUserID(int globalUserID) {
        this.globalUserID = globalUserID;
        Log.d(LOG_TAG, " DataCurrentUser setGlobalUserID: "+globalUserID);
    }

    public int getGlobalUserID() {
        return globalUserID;
    }

    public static DataCurrentUser getInstans() {
        return dataUser;
    }

    public  void setIdentificationData(long id, String name,int globalID){
       userID = id;
       userName = name;
       globalUserID = globalID;
        Log.d(LOG_TAG, "setIdentificationData id= "+id+" name= "+name+" globalID: "+globalID);
        verificationDataFilled();
    }

    public  void  setSettingsData(int error, int show, int duration){
        errorPause = error;
        showPause = show;
        durationGame = duration;
        Log.d(LOG_TAG, "setSettingsData error = "+error+" show= "+show+" duration= "+duration);
        verificationDataFilled();
    }

    private  void verificationDataFilled(){
        if(userID > 0 && !userName.equals(null) && showPause > 0 && errorPause > 0 && durationGame >0 ){
            allDataAreFilled = true;
        }
        Log.d(LOG_TAG, "AllData id="+userID+" name =  "+userName+" error = "+ errorPause+" show= "+showPause+" duration= "+durationGame+" allDataAreFilled= "+allDataAreFilled);
    }

}
