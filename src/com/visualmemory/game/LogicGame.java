package com.visualmemory.game;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class LogicGame implements OnClickListener {

    public static final String TAG = "myLogs";
    private int currentlyQuantityNumeric = 2;
    private long timePause;
    private long errorPause;
    private List<TextView> mainTextViewList;
    private boolean isStoped = false;
    private int correctAnswers = 0;
    private int correctCircle = 0;
    private int errorCircle = 0;
    // _____________________________________________
    private List<TextView> workTextViewList;
    private int currentlyButtonPres = 0;
    private int NumderButton = 1;
    private OnClickListener clickListener;

    public LogicGame(List<TextView> mainTextViewList) {
        this.mainTextViewList = mainTextViewList;
        clickListener = this;
        timePause = DataCurrentUser.getInstans().showPause;
        errorPause = DataCurrentUser.getInstans().errorPause;
        start();
    }

    public int getCorrectAnswers(){
        return correctAnswers;
    }

    public void setIsStoped(boolean isStoped){
        this.isStoped = isStoped;
    }

    private List<TextView> selectWorkTextViewList(int workListSize, int mainListSize) {
        List<TextView> workTextViewList = new ArrayList<TextView>();
        Set<Integer> OriginalNumberList = new HashSet<Integer>();
        Random rand = new Random();

        while (OriginalNumberList.size() < workListSize) {
            Integer number = rand.nextInt(mainListSize);
            OriginalNumberList.add(number);
        }

        for (Integer numb : OriginalNumberList) {
            workTextViewList.add(mainTextViewList.get(numb));
        }
        return workTextViewList;
    }

    private void start() {

        if (correctCircle == 2) {
            if (currentlyQuantityNumeric <= mainTextViewList.size()) {
                currentlyQuantityNumeric++;
            }
            correctCircle = 0;
        }
        if (errorCircle == 2) {
            if (currentlyQuantityNumeric > 1) {
                currentlyQuantityNumeric--;
            }
            errorCircle = 0;
        }
        workTextViewList = selectWorkTextViewList(currentlyQuantityNumeric, mainTextViewList.size());

        int i = 1;
        for (TextView textView : workTextViewList) {
            textView.setText("" + i);
            i++;
        }
        new Thread(hideThread).start();

    }

    private Runnable hideThread = new Runnable() {
        @Override
        public void run() {

            try {
                TimeUnit.SECONDS.sleep(timePause);
                if(!isStoped){
                    myHandle.sendMessage(myHandle.obtainMessage());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Handler myHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(!isStoped){
                for (TextView textView : workTextViewList) {
                    textView.setText("?");
                    textView.setOnClickListener(clickListener);
                    }
                }
            }
        };
    };

    private void setErrorStatus() {
        errorCircle++;
        if (correctCircle > 0) {
            correctCircle--;
        }
        int i = 1;
        for (TextView textView : workTextViewList) {
            textView.setText("" + i);
            textView.setTextColor(Color.RED);
            textView.setOnClickListener(null);
            i++;
        }
        new Thread(errorPauseThread).start();
    }

    private Runnable errorPauseThread = new Runnable() {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(errorPause);
                if(!isStoped){
                myHandle.sendMessage(myHandle.obtainMessage());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Handler myHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(!isStoped){
                    for (TextView textView : workTextViewList) {
                    textView.setTextColor(Color.parseColor("#674D1A"));
                }
                if (correctCircle > 0) {
                    correctCircle--;
                }
                cleanAll();
                start();
            }
          }
        };
    };

    private void cleanAll() {
        for (TextView textView : workTextViewList) {
            textView.setText("");
            textView.setOnClickListener(null);
        }
        currentlyButtonPres = 0;
        NumderButton = 1;
        workTextViewList.clear();
    }

    private int returnButtonIndex(View e) {
        int k = -1;
        for (int i = 0; i < workTextViewList.size(); i++) {
            if (workTextViewList.get(i).equals(e)) {
                k = i;
                break;
            }
        }
        return k;
    }

    @Override
    public void onClick(View e) {
        Log.d(TAG, "onClick");
            if (currentlyButtonPres == returnButtonIndex(e)) {
                TextView textView = (TextView) e;
                textView.setText("" + NumderButton);
                textView.setOnClickListener(null);
                correctAnswers++;
               //  Log.d(TAG, "correctAnswers " + correctAnswers);
                //	Log.d(TAG, "Size " + workTextViewList.size());
                //	Log.d(TAG, "currentlyNumderButton " + NumderButton);
                if (NumderButton == workTextViewList.size()) {
                    Log.d(TAG, "start");
                    correctCircle++;
                    if (errorCircle > 0) {
                        errorCircle--;
                    }
                    cleanAll();
                    start();
                } else {
                    NumderButton++;
                    currentlyButtonPres++;
                }
            } else {
              /*  Log.d(TAG,
                        "Size else Error " + workTextViewList.size());
                Log.d(TAG, " else Error returnButtonIndex "
                        + returnButtonIndex(e));
                Log.d(TAG, "currentlyButtonPres else Error "
                        + currentlyButtonPres);*/
                setErrorStatus();
            }
    }
}


