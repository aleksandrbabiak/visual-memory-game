package com.visualmemory.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Windows on 07.03.14.
 */
public class DBConnection extends SQLiteOpenHelper {
    private final String LOG_TAG ="dbConnectionLogs";
    private static String databasesName = "memoryDB";
    private static int databasesVersion = 1;

    public DBConnection(Context context) {
        super(context, databasesName , null, databasesVersion);
        Log.d(LOG_TAG,"DBConnection contractor");
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE users("
                 + "_id integer primary key autoincrement,"
                 +" name text NOT NULL, "
                 +"global_id integer,"
                 + "unique (name)" + ");");


        sqLiteDatabase.execSQL(" CREATE TABLE settings("
                + "_id integer primary key autoincrement,"
                +" error_pause integer NOT NULL,"
                +"show_pause integer NOT NULL,"
                +"duration_game integer NOT NULL,"
                +"user_id integer  NOT NULL,"
                +"unique (user_id),"
                +"CONSTRAINT fkUsersId FOREIGN KEY (user_id)"
                + "REFERENCES users (_id)" + ");");

        sqLiteDatabase.execSQL(" CREATE TABLE statistics	("
                + "_id integer primary key autoincrement,"
                +"  date text  NOT NULL, "
                +"  duration_game integer NOT NULL,"
                +" k_factor real NOT NULL,"
                +"user_id integer  NOT NULL,"
                +"CONSTRAINT fkSTUserId FOREIGN KEY (user_id)"
                + "REFERENCES users (_id) " + ");");

        sqLiteDatabase.execSQL(" CREATE TABLE send_server	("
                + "_id integer primary key autoincrement,"
                +"  date text  NOT NULL, "
                +"  duration_game integer NOT NULL,"
                +" k_factor real NOT NULL,"
                +"user_id integer  NOT NULL,"
                +"CONSTRAINT fkSUserId FOREIGN KEY (user_id)"
                + "REFERENCES users (_id) " + ");");



        Log.d(LOG_TAG,"onCreate database");
    }

    @Override
    public void onOpen(SQLiteDatabase sqLiteDatabase) {
        super.onOpen(sqLiteDatabase);
        if (!sqLiteDatabase.isReadOnly()) {
            sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON;");
            Log.d(LOG_TAG,"PRAGMA foreign_keys=ON");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
