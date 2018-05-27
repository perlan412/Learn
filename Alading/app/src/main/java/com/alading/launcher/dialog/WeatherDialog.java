package com.alading.launcher.dialog;

import com.alading.launcher.R;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class WeatherDialog extends BaseDialog implements OnClickListener{

	static final String  TAG = "WeatherDialog";
	public WeatherDialog(Context context) {
		
		super(context);
		mContentParent = (ViewGroup) View.inflate(mContext, R.layout.dialog_weather, null); 
		if(mContentParent != null){
			mContentParent.setOnClickListener(this);
		}
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.item_1:
			Log.d(TAG, "item_1");
			break;
		default:
			break;
		}
	}

}
