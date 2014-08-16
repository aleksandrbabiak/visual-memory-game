package com.visualmemory.interfaces;

import android.database.Cursor;
import com.visualmemory.database.entity.GameResult;
import com.visualmemory.database.entity.Settings;
import com.visualmemory.database.entity.User;


import java.util.List;


public interface IDBWorker {
    public long  insertUser(String userName);
    public long insertSettings(int error_pause,int show_pause, int duration_game, long user_id );
    public long insertStatistics(String date,int duration_game,  double k_factor, long user_id);
    public int getIDByUserName(String userName);
    public User getUserByUserName(String userName);
    public void printContentUserTable();
    public String[] getNamesByUsersTable();
    public List<User> getUsersListByUserTable();
    public Settings getSettingsBySettingTable(long userID);
    public void  updateUserSettingsById(long id,int error_pause,int show_pause, int duration_game);
    public  List<GameResult> getUserStatisticsList(long userID);
    public GameResult getUserRecord(long uerID);
    public void insertSendServerRecord(String date, int duration_game, double k_factor, long user_id);
    public GameResult getSendServerUserRecord(long userID);
    public void updateSendServerRecord(long userID, double k_factor);
    public void inserGlobalId(long userID, int globalID);

}
