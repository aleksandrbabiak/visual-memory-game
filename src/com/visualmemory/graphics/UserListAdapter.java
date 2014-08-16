package com.visualmemory.graphics;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.visualmemory.R;


/**
 * Created by Windows on 07.05.14.
 */
public class UserListAdapter extends BaseAdapter {
  private  Context ctx;
  private  LayoutInflater lInflater;
  private  String [] userNames;

    public UserListAdapter(Context ctx, String[] userNames){
        this.ctx = ctx;
        this.userNames = userNames;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }



    @Override
    public int getCount() {
        return userNames.length;
    }


    @Override
    public Object getItem(int i) {
        return userNames[i];
    }


    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.my_user_list_item, viewGroup, false);
        }

        String fontPath = "fonts/COMIC.TTF";
        Typeface typeface = Typeface.createFromAsset( ctx.getAssets(), fontPath);

        TextView textView = (TextView) view.findViewById(R.id.userListItem);
        textView.setTypeface(typeface);
        textView.setText(userNames[i]);

        return view;
    }
}
