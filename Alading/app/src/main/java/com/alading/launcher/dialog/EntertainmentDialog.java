package com.alading.launcher.dialog;

import com.alading.launcher.EntertainmentActivity;
import com.alading.launcher.R;
import com.alading.launcher.utils.ActivitysUtils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class EntertainmentDialog extends BaseDialog implements OnClickListener{

	static final String  TAG = "EntertainmentDialog";
	public EntertainmentDialog(Context context) {
		
		super(context);
		mContentParent = (ViewGroup) View.inflate(mContext, R.layout.dialog_entertrainment, null); 
		if(mContentParent != null){
			mContentParent.setOnClickListener(this);
		}
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.item_1:
			Log.d(TAG, "item_1");
			ActivitysUtils.startActivityEx(mContext,EntertainmentActivity.class);
			break;
		default:
			break;
		}
	}

}
