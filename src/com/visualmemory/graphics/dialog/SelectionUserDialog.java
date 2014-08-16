package com.visualmemory.graphics.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.visualmemory.R;
import com.visualmemory.database.DBManager;
import com.visualmemory.database.entity.Settings;
import com.visualmemory.database.entity.User;
import com.visualmemory.game.DataCurrentUser;
import com.visualmemory.graphics.UserListAdapter;
import com.visualmemory.interfaces.IDBWorker;
import com.visualmemory.interfaces.IMainAnimation;


/**
 * Created by Windows on 10.03.14.
 */
public class SelectionUserDialog extends AlertDialog.Builder implements AdapterView.OnItemClickListener , View.OnClickListener {
    private final String LOG_TAG = "SelectUserDialogLog";
    private Activity activity;
    private IDBWorker dbManager;
    private String[] userNameList;
    private IMainAnimation animation;
    private AlertDialog alertDialog;
    private Button exitButton;
    private Button addButton;


    public SelectionUserDialog(final Activity activity, final IMainAnimation animation) {
        super(activity);
        this.activity = activity;
        this.animation = animation;
        dbManager = new DBManager(activity);
        userNameList = dbManager.getNamesByUsersTable();

        String fontPath = "fonts/COMIC.TTF";
        Typeface typeface = Typeface.createFromAsset( activity.getAssets(), fontPath);

        LayoutInflater ltInflater = activity.getLayoutInflater();
        View item = ltInflater.inflate(R.layout.select_dialog_layout,null, false);

        TextView selectDialogTitle = (TextView) item.findViewById(R.id.selectDialogTitle);
        selectDialogTitle.setTypeface(typeface);

        ListView userListView = (ListView) item.findViewById(R.id.selectUserList);

        exitButton = (Button)  item.findViewById(R.id.selectDialogExit);
        exitButton.setOnClickListener(this);

        addButton  = (Button)  item.findViewById(R.id.selectDialogAdd);
        addButton.setOnClickListener(this);


        UserListAdapter userListAdapter = new UserListAdapter(activity,userNameList);
               userListView.setAdapter(userListAdapter);


        userListView.setOnItemClickListener(this);

        setCancelable(false);
        setView(item);

        alertDialog = show();
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String userName = userNameList[i];

        User user =  dbManager.getUserByUserName(userName);

        long userID = user.getUserID();
        Log.d(LOG_TAG, "id = " + userID + " name= " + userName);

        int error_pause = -1;
        int show_pause = -1;
        int duration_game = -1;

        Settings settings = dbManager.getSettingsBySettingTable(userID);
        error_pause= settings.getErrorPause();
        show_pause = settings.getShowPause();
        duration_game= settings.getDurationGame();

        Log.d(LOG_TAG, "id = " + userID + " name= " + userName + " error_pause= " + error_pause + " show_pause =" + show_pause + " duration_game=" + duration_game);

        if (userID > 0 && !userName.equals(null) && show_pause > 0 && error_pause > 0 && duration_game > 0) {
            Log.d(LOG_TAG, "id = " + userID + " name= " + userName + " error_pause=" + error_pause + " show_pause =" + show_pause + " duration_game=" + duration_game);
            DataCurrentUser.getInstans().setIdentificationData(userID, userName, user.getGlobalUserID());
            DataCurrentUser.getInstans().setSettingsData(error_pause, show_pause, duration_game);
            alertDialog.dismiss();
            animation.startAnimation();
        }
    }

    @Override
    public void onClick(View view) {
       if (view.getId() == addButton.getId() ){
           new InputNameDialog(activity,animation);
           alertDialog.dismiss();
       }else {
           System.exit(0);
       }

    }
}
