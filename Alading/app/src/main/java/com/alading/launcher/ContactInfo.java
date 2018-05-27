package com.alading.launcher;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * Created by chongming on 18-1-11.
 */
public class ContactInfo implements View.OnClickListener{

    private Context mContext;
    private String name;
    private String number;

    public ContactInfo(Context context, String name, String number) {
        this.mContext = context;
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }


    @Override
    public void onClick(View view) {
        Toast.makeText(mContext,"here", Toast.LENGTH_LONG).show();
    }
}
