package com.visualmemory.graphics.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Windows on 01.05.14.
 */
public class NotConnectionDialog extends   AlertDialog.Builder {
    private AlertDialog alertDialog;

    public NotConnectionDialog(Context context) {
        super(context);
        setTitle("Connection");
        setMessage("No internet connection");
        setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog=show();
    }
}
