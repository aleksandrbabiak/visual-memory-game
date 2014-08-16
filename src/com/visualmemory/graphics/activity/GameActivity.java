package com.visualmemory.graphics.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.visualmemory.R;
import com.visualmemory.database.DBManager;
import com.visualmemory.database.entity.GameResult;
import com.visualmemory.game.DataCurrentUser;
import com.visualmemory.game.LogicGame;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Windows on 17.03.14.
 */
public class GameActivity  extends Activity {
    private final String LOG_TAG = "GameActivityLogs";
    private	ProgressBar progressTime;
    private LogicGame logicGame;
    private List<TextView> mainTextViewLis;
    private ProgressBarTask barTask;
    private DBManager dbManager;

    private   String date;
    private Integer durationGame;
    private  double k_factor;
    private Typeface typefaceComic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.game);
        String fontPath = "fonts/COMIC.TTF";

        typefaceComic = Typeface.createFromAsset(getAssets(), fontPath);


        mainTextViewLis = new ArrayList<TextView>();
        initViews();
        durationGame = DataCurrentUser.getInstans().durationGame;
        progressTime = (ProgressBar) findViewById(R.id.progressBar);
        logicGame = new LogicGame(mainTextViewLis);
        dbManager = new DBManager(this);

        barTask = new ProgressBarTask();
        barTask.execute();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG,"onDestroy");
        logicGame.setIsStoped(true);
        barTask.cancel(false);
        super.onDestroy();

    }

    private void initViews() {

        TextView time = (TextView) findViewById(R.id.tvTime);
        time.setTypeface(typefaceComic);

        TextView textView1 = (TextView) findViewById(R.id.textView5);
        textView1.setTypeface(typefaceComic);
        mainTextViewLis.add(textView1);

        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setTypeface(typefaceComic);
        mainTextViewLis.add(textView2);

        TextView textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setTypeface(typefaceComic);
        mainTextViewLis.add(textView3);

        TextView textView4 = (TextView) findViewById(R.id.textView4);
        textView4.setTypeface(typefaceComic);
        mainTextViewLis.add(textView4);



        TextView textView5 = (TextView) findViewById(R.id.textView7);
        textView5.setTypeface(typefaceComic);
        mainTextViewLis.add(textView5);

        TextView textView6 = (TextView) findViewById(R.id.textView8);
        textView6.setTypeface(typefaceComic);
        mainTextViewLis.add(textView6);

        TextView textView7 = (TextView) findViewById(R.id.textView9);
        textView7.setTypeface(typefaceComic);
        mainTextViewLis.add(textView7);

        TextView textView8 = (TextView) findViewById(R.id.textView10);
        textView8.setTypeface(typefaceComic);
        mainTextViewLis.add(textView8);



        TextView textView9 = (TextView) findViewById(R.id.textView12);
        textView9.setTypeface(typefaceComic);
        mainTextViewLis.add(textView9);

        TextView textView10 = (TextView) findViewById(R.id.textView13);
        textView10.setTypeface(typefaceComic);
        mainTextViewLis.add(textView10);

        TextView textView11 = (TextView) findViewById(R.id.textView14);
        textView11.setTypeface(typefaceComic);
        mainTextViewLis.add(textView11);

        TextView textView12 = (TextView) findViewById(R.id.textView15);
        textView12.setTypeface(typefaceComic);
        mainTextViewLis.add(textView12);



        TextView textView13 = (TextView) findViewById(R.id.textView17);
        textView13.setTypeface(typefaceComic);
        mainTextViewLis.add(textView13);

        TextView textView14 = (TextView) findViewById(R.id.textView18);
        textView14.setTypeface(typefaceComic);
        mainTextViewLis.add(textView14);

        TextView textView15 = (TextView) findViewById(R.id.textView19);
        textView15.setTypeface(typefaceComic);
        mainTextViewLis.add(textView15);

        TextView textView16 = (TextView) findViewById(R.id.textView20);
        textView16.setTypeface(typefaceComic);
        mainTextViewLis.add(textView16);



        TextView textView17 = (TextView) findViewById(R.id.textView22);
        textView17.setTypeface(typefaceComic);
        mainTextViewLis.add(textView17);

        TextView textView18 = (TextView) findViewById(R.id.textView23);
        textView18.setTypeface(typefaceComic);
        mainTextViewLis.add(textView18);

        TextView textView19 = (TextView) findViewById(R.id.textView24);
        textView19.setTypeface(typefaceComic);
        mainTextViewLis.add(textView19);

        TextView textView20 = (TextView) findViewById(R.id.textView25);
        textView20.setTypeface(typefaceComic);
        mainTextViewLis.add(textView20);
    }




    private void startResultActivity() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("date",date);
        intent.putExtra("durationGame",durationGame);
        intent.putExtra("k_factor",k_factor);
        startActivity(intent);
        finish();
    }

    private void startNewRecordActivity(){
        Intent intent = new Intent(this, NewRecordActivity.class);
        intent.putExtra("date",date);
        intent.putExtra("durationGame",durationGame);
        intent.putExtra("k_factor",k_factor);
        startActivity(intent);
        finish();
    }



    private void cancelTask() {
        if ( barTask == null) return;
        barTask.cancel(false);
    }

    private void saveStatistics(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy/HH:mm");

         date = sdf.format(new Date(System.currentTimeMillis()));
         int correct_namber = logicGame.getCorrectAnswers();
         k_factor=((double)correct_namber)/(double) durationGame;
         long user_id=DataCurrentUser.getInstans().userID ;

         dbManager.insertStatistics(date, durationGame,k_factor,user_id);
        Log.d(LOG_TAG,"saveStatistics date = "+date+" duration_game= "+durationGame+" correct_namber= "+correct_namber+"  k_factor ="+ k_factor+" user_id="+user_id);
    }


    private class ProgressBarTask extends AsyncTask<Void, Integer, Void> {
        private int max;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            max = 60 * durationGame;
            progressTime.setMax(max);
            Log.d(LOG_TAG,"ProgressBarTask onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i <= max; i++ ){
                try {
                    if (isCancelled()) return null;
                    TimeUnit.SECONDS.sleep(1);
                    Log.d(LOG_TAG,"ProgressBarTask doInBackground i="+i);
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressTime.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            saveStatistics();
            GameResult userRecord = dbManager.getUserRecord(DataCurrentUser.getInstans().userID);
            if(userRecord != null && userRecord.getkFactor()>k_factor){
                startResultActivity();
            }else {
                startNewRecordActivity();
            }

            finish();
            Log.d(LOG_TAG,"ProgressBarTask onPostExecute");
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d(LOG_TAG,"onCancelled()");
        }

    }






}
