package com.visualmemory.graphics.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.visualmemory.R;


/**
 * Created by Windows on 08.05.14.
 */
public class NewRecordActivity extends Activity implements View.OnClickListener {
    private Typeface typeface;
    private TextView newDate;
    private TextView newDuration;
    private TextView newKfaktor;
    private Button newRecordBtnMenu;
    private Button newRecordBtnAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.new_record);

        String fontPath = "fonts/Long Clam.ttf";
        //fonts/Long Clam.ttf
        //fonts/From Cartoon Blocks.ttf
        //fonts/PWPerspective.ttf
        typeface = Typeface.createFromAsset(getAssets(), fontPath);


      initView();
      setNewRecordDataGame();
    }

    private void initView(){
        TextView newRecordHeader = (TextView)findViewById(R.id.newRecordHeader);
        newRecordHeader.setTypeface(typeface);

        newDate = (TextView)findViewById(R.id.newDate);
        newDate.setTypeface(typeface);

        newDuration = (TextView)findViewById(R.id.newDuration);
        newDuration.setTypeface(typeface);

        newKfaktor = (TextView)findViewById(R.id.newKfaktor);
        newKfaktor.setTypeface(typeface);

        newRecordBtnMenu = (Button) findViewById(R.id.newRecordBtnMenu);
        newRecordBtnMenu.setOnClickListener(this);

        newRecordBtnAgain = (Button) findViewById(R.id.newRecordBtnAgain);
        newRecordBtnAgain.setOnClickListener(this);
    }

    private void setNewRecordDataGame() {
        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        int durationGame = (Integer) intent.getSerializableExtra("durationGame");
        double k_factor = (Double) intent.getSerializableExtra("k_factor");

        newDate.setText("Date: " + date);
        newDuration.setText("Duration game: " + durationGame + " min");
        newKfaktor.setText("score: " + k_factor);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.newRecordBtnAgain:
                Intent intent = new Intent(this, GameActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.newRecordBtnMenu:
                finish();
                break;

        }
    }
}
