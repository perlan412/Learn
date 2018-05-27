package com.alading.launcher.dialog;

import com.alading.launcher.CommunicationActivity;
import com.alading.launcher.R;
import com.alading.launcher.utils.ActivitysUtils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class CommunicationDialog extends BaseDialog implements OnClickListener{

	static final String  TAG = "CommunicationDialog";
	ViewGroup mMenu;
	public CommunicationDialog(Context context) {
		super(context);
		mContentParent = (ViewGroup) View.inflate(mContext, R.layout.dialog_communication, null); 
		if(mContentParent != null){
			mContentParent.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.id_comm:
			Log.d(TAG, "id_comm");
			ActivitysUtils.startActivityEx(mContext,CommunicationActivity.class);
			break;
		default:
			break;
		}
	}

}
