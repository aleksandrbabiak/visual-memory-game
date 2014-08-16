package com.visualmemory.graphics.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.visualmemory.R;
import com.visualmemory.database.DBManager;
import com.visualmemory.database.entity.GameResult;
import com.visualmemory.game.DataCurrentUser;


/**
 * Created by Windows on 25.03.14.
 */


public class ResultActivity extends Activity implements View.OnClickListener {
    private final String LOG_TAG = "ResultActivityLogs";
    private DBManager dbManager;

    private TextView yourRecord;
    private TextView recordDate;
    private TextView recordDurationGame;
    private TextView record_k_factor;

    private TextView yourResult;
    private TextView currentDate;
    private TextView currentDurationGame;
    private TextView current_k_factor;

    private Button tryAgain;
    private Button menu;
    Typeface typeface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.gameresult);

        String fontPath = "fonts/Long Clam.ttf";
        //fonts/Long Clam.ttf
        //fonts/From Cartoon Blocks.ttf
        //fonts/PWPerspective.ttf
         typeface = Typeface.createFromAsset(getAssets(), fontPath);

        dbManager = new DBManager(this);
        initView();
        setUserRecord();
        setResultDataGame();


    }

    private void initView() {
        yourRecord = (TextView) findViewById(R.id.yourRecord);
        yourRecord.setTypeface(typeface);
        recordDate = (TextView) findViewById(R.id.date);
        recordDate.setTypeface(typeface);
        recordDurationGame = (TextView) findViewById(R.id.durationGame);
        recordDurationGame.setTypeface(typeface);
        record_k_factor = (TextView) findViewById(R.id.k_factor);
        record_k_factor.setTypeface(typeface);

        yourResult = (TextView) findViewById(R.id.yourResult);
        yourResult.setTypeface(typeface);
        currentDate = (TextView) findViewById(R.id.currentDate);
        currentDate.setTypeface(typeface);
        currentDurationGame = (TextView) findViewById(R.id.currentDurationGame);
        currentDurationGame.setTypeface(typeface);
        current_k_factor = (TextView) findViewById(R.id.current_k_factor);
        current_k_factor.setTypeface(typeface);

        tryAgain = (Button) findViewById(R.id.buttonTry_again);
        tryAgain.setOnClickListener(this);
        menu = (Button) findViewById(R.id.menuButton);
        menu.setOnClickListener(this);
    }

    private void setResultDataGame() {
        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        int durationGame = (Integer) intent.getSerializableExtra("durationGame");
        double k_factor = (Double) intent.getSerializableExtra("k_factor");
        Log.d(LOG_TAG, "setResultDataGame  date: " + date + " duration game: " + durationGame + " k_factor: " + k_factor);

        currentDate.setText("Date: " + date);
        currentDurationGame.setText("Duration game: " + durationGame + " min");
        current_k_factor.setText("score: " + k_factor);
    }

    private void setUserRecord() {
        GameResult gameRecord = dbManager.getUserRecord(DataCurrentUser.getInstans().userID);

        String recDate = "";
        int recDuration_game = 0;
        double rec_k_factor = 0;

        recDate = gameRecord.getDate();
        Log.d(LOG_TAG, "recDate = " + recDate);

        recDuration_game = gameRecord.getDurationGame();
        Log.d(LOG_TAG, "recDuration_game = " + recDuration_game);

        rec_k_factor = gameRecord.getkFactor();
        Log.d(LOG_TAG, "rec_k_factor = " + rec_k_factor);

        recordDate.setText("Date: " + recDate);
        recordDurationGame.setText("Duration game: " + recDuration_game + " min");
        record_k_factor.setText("score: " + rec_k_factor);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonTry_again:
                Intent intent = new Intent(this, GameActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menuButton:
                finish();
                break;

        }
    }
}

