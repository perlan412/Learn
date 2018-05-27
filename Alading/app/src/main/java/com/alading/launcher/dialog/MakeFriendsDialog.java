package com.alading.launcher.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alading.launcher.R;

public class MakeFriendsDialog extends BaseDialog implements OnClickListener{

	static final String  TAG = "MakeFriendsDialog";
	public MakeFriendsDialog(Context context) {
		
		super(context);
		mContentParent = (ViewGroup) View.inflate(mContext, R.layout.dialog_make_friends, null);
		if(mContentParent != null){
			mContentParent.setOnClickListener(this);
			
			//mImageView = (ImageView)mContentParent.findViewById(R.id.item_image1);
		}
		
	}

	ImageView mImageView = null;
	Bitmap mBitmap = null;
	@Override
	public void onClick(View v) {
		Log.d(TAG,"id_make_friends");
		Intent mIntent=new Intent();
		mIntent.setClassName("com.android.calendar", "com.android.calendar.AllInOneActivity");
		try{
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(mIntent);
		}catch(Exception e){
			
		}
	}

}
