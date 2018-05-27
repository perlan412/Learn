package com.alading.launcher.dialog;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.alading.launcher.EntertainmentActivity;
import com.alading.launcher.R;
import com.alading.launcher.utils.ActivitysUtils;

public class GpsAndLearnDialog extends BaseDialog implements OnClickListener{

	static final String  TAG = "GpsAndLearnDialog";
	public GpsAndLearnDialog(Context context) {
		
		super(context);
		mContentParent = (ViewGroup) View.inflate(mContext, R.layout.dialog_gps_learn, null);
		if(mContentParent != null){
			mContentParent.setOnClickListener(this);
		}
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.id_gps_learn:
			Log.d(TAG, "id_gps_learn");
			ActivitysUtils.startActivityEx(mContext,EntertainmentActivity.class);
			break;
		default:
			break;
		}
	}

}
