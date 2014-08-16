package com.visualmemory.graphics.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.visualmemory.R;
import com.visualmemory.database.DBManager;
import com.visualmemory.game.DataCurrentUser;
import com.visualmemory.game.DefaultSettingsGame;
import com.visualmemory.interfaces.IDBWorker;
import com.visualmemory.interfaces.IMainAnimation;


/**
 * Created by Windows on 07.03.14.
 */
public class InputNameDialog extends AlertDialog.Builder implements View.OnClickListener {
    private final String LOG_TAG = "InputNameDialogLogs";
    private  Activity activity;
    private  EditText inputName;
    private AlertDialog alertDialog;
    private IMainAnimation animation;
    private  Button exitButton;
    private  Button okButton;


    public InputNameDialog(Activity activity,IMainAnimation animation ) {
        super(activity);
        this.activity = activity;
        this.animation = animation;

        LayoutInflater ltInflater = activity.getLayoutInflater();
        View item = ltInflater.inflate(R.layout.input_dialog_layout,null, false);
        String fontPath = "fonts/COMIC.TTF";
        //fonts/Long Clam.ttf
        //fonts/From Cartoon Blocks.ttf
        //fonts/PWPerspective.ttf
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), fontPath);

        TextView title = (TextView) item.findViewById(R.id.inputDialogTitle);
        title.setTypeface(typeface);

        TextView message = (TextView) item.findViewById(R.id.inputDialogMessage);
        message.setTypeface(typeface);

         exitButton = (Button) item.findViewById(R.id.inputDialogExit);
         exitButton.setOnClickListener(this);

         okButton = (Button) item.findViewById(R.id.inputDialogOk);
         okButton.setOnClickListener(this);

         inputName = (EditText) item.findViewById(R.id.inputDialogEditText);
         inputName.setTypeface(typeface);


        setCancelable(false);
        setView(item);
        alertDialog = show();

    }



    @Override
    public void onClick(View view) {
       if(view.getId()==okButton.getId()){
        IDBWorker dbManager;
        String inputUserName = inputName.getText().toString();

        int error = DefaultSettingsGame.ERROR_PAUSE;
        int show = DefaultSettingsGame.SHOW_PAUSE;
        int duration =  DefaultSettingsGame.DURATION_GAME;

        if (!inputUserName.equals("")) {
            dbManager = new DBManager(activity);
            Log.d(LOG_TAG, "User input name: " + inputUserName);
            if (dbManager.getUserByUserName(inputUserName) == null) {

                long rowUserID = dbManager.insertUser(inputUserName);
                if (rowUserID > 0) {
                  dbManager.insertSettings(error,show,duration,rowUserID);
                    DataCurrentUser.getInstans().setIdentificationData(rowUserID, inputUserName, 0);
                    DataCurrentUser.getInstans().setSettingsData(error, show, duration);
                    alertDialog.dismiss();
                    animation.startAnimation();
                }

            } else {
                Toast.makeText(activity, "this name is used", Toast.LENGTH_LONG).show();

                new InputNameDialog(activity,animation);
            }
        }

       }else {
           System.exit(0);
       }

    }
}
