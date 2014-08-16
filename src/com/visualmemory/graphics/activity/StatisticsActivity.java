package com.visualmemory.graphics.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.visualmemory.R;
import com.visualmemory.database.DBManager;
import com.visualmemory.database.entity.GameResult;
import com.visualmemory.game.DataCurrentUser;
import com.visualmemory.graphics.dialog.MyRecordDialog;


import java.util.List;

/**
 * Created by Windows on 25.03.14.
 */
public class StatisticsActivity extends Activity implements View.OnClickListener {
    private final String LOG_TAG = "StatisActivityrLogs";
    private DBManager dbManager;
    private Button butRecord;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.statistics);
        dbManager = new DBManager(this);


        String fontPath = "fonts/COMIC.TTF";
        //fonts/floyd.TTF
        //fonts/From Cartoon Blocks.ttf
        //fonts/PWPerspective.ttf
        //fonts/COMIC.TTF
        Typeface typeface = Typeface.createFromAsset(getAssets(), fontPath);

        TextView statisticsHeader= (TextView) findViewById(R.id.statistics_header);
        statisticsHeader.setTypeface(typeface);

        butRecord = (Button) findViewById(R.id.stButtonRecord);
        butRecord.setOnClickListener(this);

        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);
        LayoutInflater ltInflater = getLayoutInflater();

        List<GameResult> statisticsLists = dbManager.getUserStatisticsList(DataCurrentUser.getInstans().userID);
        String date;
        int duration_game;
        double k_factor;
        int color = 0;


        for(GameResult gameResult: statisticsLists ){
            date = gameResult.getDate();
            duration_game = gameResult.getDurationGame();
            k_factor = gameResult.getkFactor();

            View item = ltInflater.inflate(R.layout.item, linLayout, false);

            TextView tvDate = (TextView) item.findViewById(R.id.date);
            tvDate.setTypeface(typeface);
            tvDate.setText(date);

            TextView tvDuration = (TextView) item.findViewById(R.id.durationGame);
            tvDuration.setTypeface(typeface);
            tvDuration.setText("duration game: " + duration_game + " min");

            TextView tvK_factor = (TextView) item.findViewById(R.id.k_factor);
            tvK_factor.setTypeface(typeface);
            tvK_factor.setText("score: " + k_factor);

            item.getLayoutParams().width = LayoutParams.MATCH_PARENT;


            linLayout.addView(item);

            Log.d(LOG_TAG,"Statistics:  date= "+date+" duration= "+duration_game+" k_factor= "+k_factor);
        }


        }

    @Override
    public void onClick(View view) {
       GameResult userRecord = dbManager.getUserRecord(DataCurrentUser.getInstans().userID);

     if(userRecord!= null){
       new MyRecordDialog(this);
     }
    }
}



