package com.visualmemory.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.visualmemory.database.entity.GameResult;
import com.visualmemory.database.entity.Settings;
import com.visualmemory.database.entity.User;
import com.visualmemory.interfaces.IDBWorker;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Windows on 09.03.14.
 */
public class DBManager implements IDBWorker {
    private final String USERS_TABLE = "users";
    private final String STATISTICS_TABLE = "statistics";
    private final String SETTINGS_TABLE = "settings";
    private final String SEND_SERVER = "send_server";
    private final String LOG_TAG = "dbManagerLogs";
    private DBConnection dbConnection;

    public DBManager(Context context) {
        Log.d(LOG_TAG, "DBManager contractor");
        dbConnection = new DBConnection(context);
    }


    @Override
    public long insertUser(String userName) {
        long rowID = -1;
        if (!userName.equals(null)) {
            SQLiteDatabase db = dbConnection.getWritableDatabase();
            ContentValues cvUsers = new ContentValues();
            cvUsers.put("name", userName);
            rowID = db.insert(USERS_TABLE, null, cvUsers);
            db.close();
            Log.d(LOG_TAG, "row insertUser , ID = " + rowID);
        }
        return rowID;
    }

    @Override
    public long insertSettings(int error_pause, int show_pause, int duration_game, long user_id) {
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        ContentValues cvSettings = new ContentValues();
        cvSettings.put("error_pause", error_pause);
        cvSettings.put("show_pause", show_pause);
        cvSettings.put("duration_game", duration_game);
        cvSettings.put("user_id", user_id);
        long rowID = db.insert(SETTINGS_TABLE, null, cvSettings);
        db.close();
        Log.d(LOG_TAG, "row insertSettings , ID = " + rowID);
        return rowID;
    }

    @Override
    public long insertStatistics(String date, int duration_game, double k_factor, long user_id) {
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        ContentValues cvStatistics = new ContentValues();
        cvStatistics.put("date", date);
        cvStatistics.put("duration_game", duration_game);
        cvStatistics.put("k_factor", k_factor);
        cvStatistics.put("user_id", user_id);
        long rowID = db.insert(STATISTICS_TABLE, null, cvStatistics);
        db.close();
        Log.d(LOG_TAG, "row insertStatistics , ID = " + rowID);
        return rowID;
    }

    @Override
    public int getIDByUserName(String userName) {
        int userID = -1;
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        String sqlQuery = "SELECT _id FROM " + USERS_TABLE + " WHERE name='" + userName + "';";
        Cursor c = db.rawQuery(sqlQuery, null);
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("_id");
            userID = c.getInt(idColIndex);
            Log.d(LOG_TAG, "row id by user name moveToFirst , ID = " + userID);
            c.close();
            db.close();
            return userID;
        }
        Log.d(LOG_TAG, "row id by user name , ID = " + userID);
        c.close();
        db.close();
        return userID;
    }


    @Override
    public void printContentUserTable() {
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        Cursor c = db.query(USERS_TABLE, null, null, null, null, null, null);

        if (c.moveToFirst()) {

            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int globalIDIndex = c.getColumnIndex("global_id");
            do {
                Log.d(LOG_TAG, "ID = " + c.getInt(idColIndex) + ", name = " + c.getString(nameColIndex));
            } while (c.moveToNext());
        } else {
            Log.d(LOG_TAG, "0 rows");
        }
        c.close();
        db.close();
    }

    @Override
    public String[] getNamesByUsersTable() {

        List<String> userNameList = new ArrayList<String>();
        SQLiteDatabase db = dbConnection.getWritableDatabase();

        Cursor c = db.query(USERS_TABLE, null, null, null, null, null, null);

        if (c.moveToFirst()) {

            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");

            do {
                userNameList.add(c.getString(nameColIndex));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        String[] nameMas = new String[userNameList.size()];
        userNameList.toArray(nameMas);

        return nameMas;
    }

    @Override
    public List<User> getUsersListByUserTable() {
        List<User> usersList = new ArrayList<User>();
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        Cursor c = db.query(USERS_TABLE, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("_id");
            int nameColIndex = c.getColumnIndex("name");

            do {
                User user = new User();
                user.setUserID(c.getLong(idColIndex));
                user.setUserName(c.getString(nameColIndex));
                usersList.add(user);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return usersList;
    }

    @Override
    public Settings getSettingsBySettingTable(long userID) {
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        String sqlQuery = "SELECT error_pause, show_pause, duration_game FROM " + SETTINGS_TABLE + " WHERE user_id=" + userID + ";";
        Cursor c = db.rawQuery(sqlQuery, null);
        Settings settings = new Settings();
        if (c.moveToFirst()) {

            int errorColIndex = c.getColumnIndex("error_pause");
            int showColIndex = c.getColumnIndex("show_pause");
            int durationColIndex = c.getColumnIndex("duration_game");

            do {

                settings.setErrorPause(c.getInt(errorColIndex));
                settings.setShowPause(c.getInt(showColIndex));
                settings.setDurationGame(c.getInt(durationColIndex));
                Log.d(LOG_TAG, "setErrorPause: " + c.getInt(errorColIndex));
                Log.d(LOG_TAG, "setShowPause: " + c.getInt(showColIndex));
                Log.d(LOG_TAG, "DurationGame: " + c.getInt(durationColIndex));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return settings;
    }

    @Override
    public void updateUserSettingsById(long id, int error_pause, int show_pause, int duration_game) {
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        String sqlQuery = "UPDATE settings SET error_pause=" + error_pause + ", " +
                "show_pause=" + show_pause + ", duration_game=" + duration_game + " WHERE user_id=" + id + ";";
        db.execSQL(sqlQuery);
        db.close();
    }

    @Override
    public List<GameResult> getUserStatisticsList(long userID) {
        List<GameResult> gameResultsList = new ArrayList<GameResult>();
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        String sqlQuery = "SELECT date, duration_game, k_factor FROM " + STATISTICS_TABLE + " WHERE user_id=" + userID + ";";
        Cursor c = db.rawQuery(sqlQuery, null);

        if (c.moveToFirst()) {

            int dateColIndex = c.getColumnIndex("date");
            int durationColIndex = c.getColumnIndex("duration_game");
            int k_ColIndex = c.getColumnIndex("k_factor");

            do {
                GameResult gameResult = new GameResult();

                String date = c.getString(dateColIndex);
                int duration_game = c.getInt(durationColIndex);
                double k_factor = c.getDouble(k_ColIndex);

                gameResult.setDate(date);
                gameResult.setDurationGame(duration_game);
                gameResult.setkFactor(k_factor);
                Log.d(LOG_TAG, "Statistics:  date= " + date + " duration= " + duration_game + " k_factor= " + k_factor);
                gameResultsList.add(gameResult);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return gameResultsList;
    }

    @Override
    public GameResult getUserRecord(long userID) {
        GameResult gameRecord = new GameResult();
        SQLiteDatabase db = dbConnection.getWritableDatabase();

        String sqlQuery3 = "SELECT date, duration_game, k_factor FROM " + STATISTICS_TABLE + " WHERE user_id=" + userID +
                " ORDER BY k_factor DESC" +
                "  LIMIT 1;";

        Cursor c = db.rawQuery(sqlQuery3, null);

        String recDate = "";
        int recDuration_game = 0;
        double rec_k_factor = 0;

        if (c.moveToFirst()) {

            int dateColIndex = c.getColumnIndex("date");
            Log.d(LOG_TAG, "dateColIndex = " + dateColIndex);

            int duration_gameColIndex = c.getColumnIndex("duration_game");
            Log.d(LOG_TAG, "duration_gameColIndex = " + duration_gameColIndex);

            int k_factorColIndex = c.getColumnIndex("k_factor");
            Log.d(LOG_TAG, "_factorColIndex = " + k_factorColIndex);

            do {
                recDate = c.getString(dateColIndex);
                recDuration_game = c.getInt(duration_gameColIndex);
                rec_k_factor = c.getDouble(k_factorColIndex);
                gameRecord.setDate(recDate);
                gameRecord.setDurationGame(recDuration_game);
                gameRecord.setkFactor(rec_k_factor);

                Log.d(LOG_TAG, "recDate: " + recDate + "  recDuration_game: " + recDuration_game + "  rec_k_factor: " + rec_k_factor);
            } while (c.moveToNext());
            c.close();
            db.close();
            return gameRecord;

        }
        c.close();
        db.close();

        return null;
    }



    @Override
    public User getUserByUserName(String userName) {
        User user = new User();
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        String sqlQuery = "select _id, name, global_id from users WHERE name='" + userName + "';";
        Cursor c = db.rawQuery(sqlQuery, null);

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("_id");
            int nameColIndex = c.getColumnIndex("name");
            int globalIDColIndex = c.getColumnIndex("global_id");


            do {
                user.setUserID(c.getInt(idColIndex));
                user.setUserName(c.getString(nameColIndex));
                user.setGlobalUserID(c.getInt(globalIDColIndex));
                Log.d(LOG_TAG, "id: "+c.getInt(idColIndex)+" mame:"+c.getString(nameColIndex)+"  globalID: "+c.getInt(globalIDColIndex));
            } while (c.moveToNext());
            return user;
        }
        return null;
    }


    @Override
    public void insertSendServerRecord(String date, int duration_game, double k_factor, long user_id) {
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        ContentValues cvStatistics = new ContentValues();
        cvStatistics.put("date", date);
        cvStatistics.put("duration_game", duration_game);
        cvStatistics.put("k_factor", k_factor);
        cvStatistics.put("user_id", user_id);
        long rowID = db.insert(SEND_SERVER, null, cvStatistics);
        db.close();
        Log.d(LOG_TAG, "row insertSendServerRecord , ID = " + rowID);
    }





    @Override
    public GameResult getSendServerUserRecord(long userID) {
        GameResult gameRecord = new GameResult();
        SQLiteDatabase db = dbConnection.getWritableDatabase();

        String sqlQuery = "SELECT date, duration_game, k_factor FROM " + SEND_SERVER + " WHERE user_id=" + userID +";";

        Cursor c = db.rawQuery(sqlQuery, null);

        String recDate = "";
        int recDuration_game = 0;
        double rec_k_factor = 0;

        if (c.moveToFirst()) {

            int dateColIndex = c.getColumnIndex("date");
            Log.d(LOG_TAG, "dateColIndex = " + dateColIndex);

            int duration_gameColIndex = c.getColumnIndex("duration_game");
            Log.d(LOG_TAG, "duration_gameColIndex = " + duration_gameColIndex);

            int k_factorColIndex = c.getColumnIndex("k_factor");
            Log.d(LOG_TAG, "_factorColIndex = " + k_factorColIndex);

            do {
                recDate = c.getString(dateColIndex);
                recDuration_game = c.getInt(duration_gameColIndex);
                rec_k_factor = c.getDouble(k_factorColIndex);
                gameRecord.setDate(recDate);
                gameRecord.setDurationGame(recDuration_game);
                gameRecord.setkFactor(rec_k_factor);

                Log.d(LOG_TAG, "ServerDate: " + recDate + "  ServerDuration_game: " + recDuration_game + "  Server_k_factor: " + rec_k_factor);
            } while (c.moveToNext());
            return gameRecord;
        }
        c.close();
        db.close();
        return null;
    }

    @Override
    public void inserGlobalId(long userID, int globalID) {
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        String sqlQuery = "UPDATE users set global_id="+globalID+" WHERE _id="+userID+";";
        Log.d(LOG_TAG, "DBManager inserGlobalId: "+globalID);
        db.execSQL(sqlQuery);
        db.close();
    }

    @Override
    public void updateSendServerRecord(long userID, double k_factor) {
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        String sqlQuery = "UPDATE send_server set k_factor="+k_factor+" WHERE _id="+userID+";";
        db.execSQL(sqlQuery);
        db.close();
    }


}