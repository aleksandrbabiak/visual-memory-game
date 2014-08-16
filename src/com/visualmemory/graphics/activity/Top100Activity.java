package com.visualmemory.graphics.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.visualmemory.R;
import com.visualmemory.VisualMemoryClient;
import com.visualmemory.database.entity.ServerRecord;
import com.visualmemory.game.DataCurrentUser;


import java.util.List;


public class Top100Activity extends Activity {
    private final String LOG_TAG = "Top100ActivityLogs";

    private LinearLayout linLayout;
    private LayoutInflater ltInflater;
    private Typeface typeface;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.loading_top100_data);

        String fontPath = "fonts/COMIC.TTF";
        typeface = Typeface.createFromAsset(getAssets(), fontPath);

        TextView tvTop100LoadingHeader = (TextView) findViewById(R.id.tvTop100LoadingHeader);
        tvTop100LoadingHeader.setTypeface(typeface);



     Handler   clientHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                List<ServerRecord> gameResultList = (List<ServerRecord>)msg.obj;

                String name;
                String date;
                double k_factor;
                int accountValue= 1;
                setContentView(R.layout.top100);
                TextView tvTop100Header = (TextView) findViewById(R.id.tvTop100Header);
                tvTop100Header.setTypeface(typeface);
                linLayout = (LinearLayout) findViewById(R.id.linLayoutTop100);
                ltInflater = getLayoutInflater();

                for(ServerRecord gameResult: gameResultList ){

                    name = gameResult.getName();
                    date = gameResult.getDate();
                    k_factor = gameResult.getK_factor();

                    View item = ltInflater.inflate(R.layout.item, linLayout, false);
                    TextView tvUserName = (TextView) item.findViewById(R.id.date);
                    tvUserName.setTypeface(typeface);
                    tvUserName.setText(name);

                    TextView tvDuration = (TextView) item.findViewById(R.id.durationGame);
                    tvDuration.setTypeface(typeface);
                    tvDuration.setText("date: " + date);

                    TextView tvK_factor = (TextView) item.findViewById(R.id.k_factor);
                    tvK_factor.setTypeface(typeface);
                    tvK_factor.setText("score: " + k_factor);

                    TextView account = (TextView) item.findViewById(R.id.accountTop100);
                    account.setTypeface(typeface);
                    account.setText("№ "+accountValue);
                    Log.d(LOG_TAG, "DataCurrentUser.getGlobalUserID: "+DataCurrentUser.getInstans().getGlobalUserID()+"   gameResult.getGlobalUserID: "+gameResult.getGlobalUserID());
                    if(DataCurrentUser.getInstans().getGlobalUserID() == gameResult.getGlobalUserID() ){
                        tvUserName.setTextColor(Color.RED);
                        tvDuration.setTextColor(Color.RED);
                        tvK_factor.setTextColor(Color.RED);
                        account.setTextColor(Color.RED);
                    }

                    item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    linLayout.addView(item);

                    accountValue++;
               }
            }
        };

        VisualMemoryClient client = new VisualMemoryClient(this,clientHandler);

}
}
