package com.visualmemory.graphics.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.visualmemory.R;
import com.visualmemory.database.DBManager;
import com.visualmemory.game.DataCurrentUser;
import com.visualmemory.interfaces.IDBWorker;


/**
 * Created by Windows on 12.03.14.
 */
public class SettingsDialog extends AlertDialog.Builder implements SeekBar.OnSeekBarChangeListener,View.OnClickListener {

    private static final String LOG_TAG = " SettingsDialogLogs";
    private Activity activity;
    private IDBWorker dbManager;
    private AlertDialog  alertDialog;


    private SeekBar showPause;
    private SeekBar errorPause;
    private SeekBar durationGame;


    private final int showPauseMax = 10;
    private final int errorPauseMax = 5;
    private final int durationGameMax = 5;

    private TextView showPauseLabel;
    private TextView errorPauseLabel;
    private TextView durationGameLabel;

    private LinearLayout linLayout;

    private int errorValue ;
    private int showValue;
    private int durationValue;

    public SettingsDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        dbManager = new DBManager(activity);

        errorValue = DataCurrentUser.getInstans().errorPause;
        showValue= DataCurrentUser.getInstans().showPause;
        durationValue = DataCurrentUser.getInstans().durationGame;

        String fontPath = "fonts/COMIC.TTF";
        //fonts/From Cartoon Blocks.ttf
        //fonts/PWPerspective.ttf
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), fontPath);

        LayoutInflater ltInflater = activity.getLayoutInflater();
        View item = ltInflater.inflate(R.layout.settings_dialog,null, false);

        TextView header = (TextView) item.findViewById(R.id.header_settings_dailog);
        header.setTypeface(typeface);

        showPauseLabel = (TextView) item.findViewById(R.id.show_pause_label);
        showPauseLabel.setTypeface(typeface);
        showPauseLabel.setText("Show pause: " + DataCurrentUser.getInstans().showPause+" seconds");


        errorPauseLabel =  (TextView) item.findViewById(R.id.error_pause_label);
        errorPauseLabel.setTypeface(typeface);
        errorPauseLabel.setText("Error pause: " + DataCurrentUser.getInstans().errorPause+" seconds");


        durationGameLabel = (TextView) item.findViewById(R.id.duration_game_lable);
        durationGameLabel.setTypeface(typeface);
        durationGameLabel.setText("Duration game: " + DataCurrentUser.getInstans().durationGame+" minute");


        showPause =(SeekBar) item.findViewById(R.id.show_pause_seek);
        showPause.setMax(showPauseMax);
        showPause.setProgress(DataCurrentUser.getInstans().showPause);
        showPause.setOnSeekBarChangeListener(this);



        errorPause = (SeekBar) item.findViewById(R.id.error_pause_seek);
        errorPause.setMax(errorPauseMax);
        errorPause.setProgress(DataCurrentUser.getInstans().errorPause);
        errorPause.setOnSeekBarChangeListener(this);


        durationGame = (SeekBar) item.findViewById(R.id.duration_game_seek );
        durationGame.setMax(durationGameMax);
        durationGame.setProgress(DataCurrentUser.getInstans().durationGame);
        durationGame.setOnSeekBarChangeListener(this);

        Button ok = (Button) item.findViewById(R.id.settings_button_ok );
        ok.setOnClickListener(this);

        setView(item);
        alertDialog = show();

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (seekBar.getId() == showPause.getId()) {
            int show = i+1;
            showPauseLabel.setText("Show pause: " + show+" seconds");
        } else {
            if (seekBar.getId() == errorPause.getId()) {
                int error = i+1;
                errorPauseLabel.setText("Error pause: " + error+" seconds");
            } else {
                int duration = i+1;
                durationGameLabel.setText("Duration game: " +duration+" minute");
            }

        }
    }

        @Override
        public void onStartTrackingTouch (SeekBar seekBar){

        }

        @Override
        public void onStopTrackingTouch (SeekBar seekBar){
            if (seekBar.getId() == showPause.getId()) {
                showValue = seekBar.getProgress()+1;
                Log.d(LOG_TAG,"onStopTrackingTouch showValue="+showValue);
            } else {
                if (seekBar.getId() == errorPause.getId()) {
                    errorValue = seekBar.getProgress()+1;
                    Log.d(LOG_TAG,"onStopTrackingTouch errorValue="+errorValue);
                } else {
                    durationValue = seekBar.getProgress()+1;
                    Log.d(LOG_TAG,"onStopTrackingTouch durationValue="+durationValue);
                }

            }
        }

        @Override
        public void onClick (View e){
            dbManager.updateUserSettingsById(DataCurrentUser.getInstans().userID,errorValue,showValue,durationValue);
            DataCurrentUser.getInstans().setSettingsData(errorValue,showValue,durationValue);
            alertDialog.dismiss();
        }
    }
