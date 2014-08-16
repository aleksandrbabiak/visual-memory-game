package com.visualmemory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


import com.google.ads.AdView;
import com.visualmemory.R;
import com.visualmemory.database.DBManager;
import com.visualmemory.game.DataCurrentUser;
import com.visualmemory.graphics.activity.*;
import com.visualmemory.graphics.dialog.InputNameDialog;
import com.visualmemory.graphics.dialog.NotConnectionDialog;
import com.visualmemory.graphics.dialog.SelectionUserDialog;
import com.visualmemory.graphics.dialog.SettingsDialog;
import com.visualmemory.interfaces.IDBWorker;
import com.visualmemory.interfaces.IMainAnimation;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class MainActivity extends Activity implements View.OnClickListener, IMainAnimation {
    private static final String LOG_TAG = "mainActivityLogs";

    private IDBWorker dbManager;
    Animation transLeft1, transLeft2, transLeft3, transRith4, transRith5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        dbManager = new DBManager(this);
        userAuthorization();


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "MainActivity: onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "MainActivity: onResume()");
    }

    private void initViews() {
        transLeft1 = AnimationUtils.loadAnimation(this, R.anim.mytransleft);
        transLeft2 =  AnimationUtils.loadAnimation(this, R.anim.mytransleft);
        transLeft3 =  AnimationUtils.loadAnimation(this, R.anim.mytransleft);
        transRith4 =  AnimationUtils.loadAnimation(this, R.anim.mytransrithe);
        transRith5 =  AnimationUtils.loadAnimation(this,R.anim.mytransrithe);
        String fontPath = "fonts/From Cartoon Blocks.ttf";
        //fonts/From Cartoon Blocks.ttf
        //fonts/PWPerspective.ttf
        Typeface typeface = Typeface.createFromAsset(getAssets(), fontPath);

        int time =800;
        TextView startGame = (TextView) findViewById(R.id.startGame);
        startGame.setTypeface(typeface);
        startGame.setOnClickListener(this);
         transLeft1.setDuration(time);
        startGame.startAnimation(transLeft1);



        TextView globalRecord = (TextView) findViewById(R.id.globalRecord);
        globalRecord.setTypeface(typeface);
        globalRecord.setOnClickListener(this);
        transRith4.setDuration(time) ;
        globalRecord.startAnimation(transRith4);

        TextView statistics = (TextView) findViewById(R.id.statistics);
        statistics.setTypeface(typeface);
        statistics.setOnClickListener(this);
        transLeft2.setDuration(time);
        statistics.startAnimation(transLeft2);

        TextView settings = (TextView) findViewById(R.id.settings);
        settings.setTypeface(typeface);
        settings.setOnClickListener(this);
        transRith4.setDuration(time);
        settings.startAnimation(transRith4);

        TextView exit = (TextView) findViewById(R.id.exit);
        exit.setTypeface(typeface);
        exit.setOnClickListener(this);
        transLeft3.setDuration(time);
        exit.startAnimation(transLeft3);
    }


    private void userAuthorization() {
        if (!DataCurrentUser.getInstans().allDataAreFilled) {
            if (dbManager.getUsersListByUserTable().size()>0) {

                new SelectionUserDialog(this,this);
            } else {
                new InputNameDialog(this,this);
            }
        }
    }


    @Override
    public void onClick(View v) {

        if (DataCurrentUser.getInstans().allDataAreFilled) {
            switch (v.getId()) {

                case R.id.startGame:
                    Log.d(LOG_TAG, "--- startGame action ---");
                    Intent intent = new Intent(this, GameActivity.class);
                    startActivity(intent);
                    break;

                case R.id.globalRecord:
                    Log.d(LOG_TAG, "--- globalRecord action ---");
                    if(internetAvailable()){
                        Intent intentTop100 = new Intent(this, Top100Activity.class);
                        startActivity(intentTop100);
                    }else {
                        new NotConnectionDialog(this);
                    }
                    break;

                case R.id.statistics:
                    Log.d(LOG_TAG, "--- statistics action ---");
                    Intent intentStatistics = new Intent(this, StatisticsActivity.class);
                    startActivity(intentStatistics);
                    break;

                case R.id.settings:
                    Log.d(LOG_TAG, "--- settings action ---");
                   new SettingsDialog(this);

                    break;
            }
        } else {
            userAuthorization();
        }

        if (v.getId() == R.id.exit) {
            Log.d(LOG_TAG, "--- exit action ---");
            System.exit(0);


        }
    }


    @Override
    protected void onDestroy() {
        System.exit(0);
        super.onDestroy();
    }

    @Override
    public void startAnimation() {
        setContentView(R.layout.main);
        initViews();
    }

    @Override
    public void startInputNameDialog() {
        new InputNameDialog(this,this);
    }




    public Boolean internetAvailable() {
        ConnectivityManager connectManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean internetAvailable = (connectManager.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
        return internetAvailable;
    }




}
