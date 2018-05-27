package com.alading.launcher.dialog;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.alading.launcher.CommunicationActivity;
import com.alading.launcher.R;
import com.alading.launcher.utils.ActivitysUtils;

public class SosDialog extends BaseDialog implements OnClickListener{

	static final String  TAG = "SosDialog";
	ViewGroup mMenu;
	public SosDialog(Context context) {
		super(context);
		mContentParent = (ViewGroup) View.inflate(mContext, R.layout.dialog_sos, null);
		if(mContentParent != null){
			mContentParent.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.id_sos:
			Log.d(TAG, "id_sos");
			ActivitysUtils.startActivityEx(mContext,CommunicationActivity.class);
			break;
		default:
			break;
		}
	}

}
