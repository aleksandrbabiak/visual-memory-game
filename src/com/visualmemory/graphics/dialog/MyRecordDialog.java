package com.visualmemory.graphics.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.visualmemory.R;
import com.visualmemory.database.DBManager;
import com.visualmemory.database.entity.GameResult;
import com.visualmemory.game.DataCurrentUser;

/**
 * Created by Windows on 29.04.14.
 */
public class MyRecordDialog  extends AlertDialog.Builder implements View.OnClickListener {
    private  Activity activity;
    private  AlertDialog alertDialog;
    private DBManager dbManager;
    Typeface typeface;

    public MyRecordDialog(Activity activity) {
        super(activity);
        this.activity = activity;

        LayoutInflater ltInflater = this.activity.getLayoutInflater();
        View item = ltInflater.inflate(R.layout.my_record,null, false);

        dbManager = new DBManager(activity);

        String fontPath = "fonts/COMIC.TTF";
        //fonts/Long Clam.ttf
        //fonts/From Cartoon Blocks.ttf
        //fonts/PWPerspective.ttf
        typeface = Typeface.createFromAsset(activity.getAssets(), fontPath);

        GameResult record = dbManager.getUserRecord(DataCurrentUser.getInstans().userID);

        TextView header = (TextView) item.findViewById(R.id.yourRecordDilog);
        header.setTypeface(typeface);
        TextView tvdate = (TextView) item.findViewById(R.id.dateDialog);
        tvdate.setTypeface(typeface);
        TextView tvduration = (TextView) item.findViewById(R.id.durationGameDialog);
        tvduration.setTypeface(typeface);
        TextView tvk_factor = (TextView) item.findViewById(R.id.k_factorDialog);
        tvk_factor.setTypeface(typeface);

        Button okButton = (Button) item.findViewById(R.id.buttonRecordDialog);
        okButton.setOnClickListener(this);

        tvdate.setText("Date: " + record.getDate());
        tvduration.setText("Duration game: " + record.getDurationGame() + " min");
        tvk_factor.setText("score: " + record.getkFactor());

       setView(item);
        alertDialog=show();
    }

    @Override
    public void onClick(View view) {
        alertDialog.dismiss();
    }
}
