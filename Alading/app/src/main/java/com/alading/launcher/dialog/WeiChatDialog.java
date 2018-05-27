package com.alading.launcher.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.alading.launcher.R;
import com.alading.launcher.utils.ActivitysUtils;

public class WeiChatDialog extends BaseDialog implements OnClickListener{

	static final String  TAG = "WeiChatDialog";
	private Context mContext;
	public WeiChatDialog(Context context) {
		
		super(context);
		mContext = context;
		mContentParent = (ViewGroup) View.inflate(mContext, R.layout.dialog_weichat, null);
		if(mContentParent != null){
			mContentParent.setOnClickListener(this);
			
			//mImageView = (ImageView)mContentParent.findViewById(R.id.item_image1);
		}
		
	}

	ImageView mImageView = null;
	Bitmap mBitmap = null;
	@Override
	public void onClick(View v) {
		Log.d(TAG,"id_weichat");
		Intent mIntent=new Intent();

		try{
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if(!ActivitysUtils.hasSimCard(mContext)){
				Toast.makeText(mContext,"no sim card",Toast.LENGTH_SHORT).show();
			}else {
				if(true){
					Toast.makeText(mContext,"unregister",Toast.LENGTH_SHORT).show();
					mIntent.setClassName(mContext, "com.alading.launcher.ErweimaActivity");
					mContext.startActivity(mIntent);
				}else {
					mIntent.setClassName("com.android.weichat", "com.android.settings.Settings");
					mContext.startActivity(mIntent);
				}
			}
		}catch(Exception e){
			Toast.makeText(mContext, "App not install", Toast.LENGTH_SHORT).show();
		}
	}

}
