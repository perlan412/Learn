package com.alading.launcher.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alading.launcher.R;

public class SettingsDialog extends BaseDialog implements OnClickListener{

	static final String  TAG = "SettingsDialog";
	public SettingsDialog(Context context) {
		
		super(context);
		mContentParent = (ViewGroup) View.inflate(mContext, R.layout.dialog_setting, null);
		if(mContentParent != null){
			mContentParent.setOnClickListener(this);
			
			//mImageView = (ImageView)mContentParent.findViewById(R.id.item_image1);
		}
		
	}

	ImageView mImageView = null;
	Bitmap mBitmap = null;
	@Override
	public void onClick(View v) {
		Log.d(TAG,"id_settings");
		Intent mIntent=new Intent();
		mIntent.setClassName("com.android.settings", "com.android.settings.Settings");
		try{
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(mIntent);
		}catch(Exception e){
			
		}
	}

}
