package com.alading.launcher.dialog;

import com.alading.launcher.R;
import com.alading.launcher.utils.HttpUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class ToolDialog extends BaseDialog implements OnClickListener{

	static final String  TAG = "ToolDialog";
	public ToolDialog(Context context) {
		
		super(context);
		mContentParent = (ViewGroup) View.inflate(mContext, R.layout.dialog_tool, null); 
		if(mContentParent != null){
			mContentParent.setOnClickListener(this);
			
			mImageView = (ImageView)mContentParent.findViewById(R.id.item_image1);
		}
		
	}

	ImageView mImageView = null;
	Bitmap mBitmap = null;
	@Override
	public void onClick(View v) {
		Intent mIntent=new Intent();
		switch(v.getId()){
		case R.id.item_1:
		{
			

			
//			new Thread(new Runnable() {
//		        public void run() {
//					try {
//						mBitmap = HttpUtils.getImageBitmap("http://192.168.0.134:8081/image");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					mImageView.post(new Runnable() {
//		                public void run() {
//		                	if(mImageView != null){
//		                		mImageView.setImageBitmap(mBitmap);
//		                	}
//		                	
//		                }
//		            });
//		        }
//		    }).start();

			Log.d(TAG, "item_1");
			
			
			final Dialog dialog = new AppMenuDialog(mContext,R.style.Dialog_Fullscreen);  
			ViewGroup viewGroup = (ViewGroup)View.inflate(mContext, R.layout.layout_tool, null);
			dialog.setContentView(viewGroup);  
			dialog.findViewById(R.id.id_tool_setting).setOnClickListener(this);
			dialog.findViewById(R.id.id_tool_file).setOnClickListener(this);
			dialog.findViewById(R.id.id_tool_calendar).setOnClickListener(this);
			dialog.findViewById(R.id.id_tool_alarm).setOnClickListener(this);
				
			dialog.show();
			
			return ;
		}
		case R.id.id_tool_setting:
			//Toast.makeText(mContext, "id_tool_setting", Toast.LENGTH_SHORT).show();
			mIntent.setClassName("com.android.settings", "com.android.settings.Settings");
			break;
		case R.id.id_tool_file:
			//Toast.makeText(mContext, "id_tool_file", Toast.LENGTH_SHORT).show();
			mIntent.setClassName("com.sprd.fileexplorer", "com.sprd.fileexplorer.activities.FileExploreActivity");
			break;
		case R.id.id_tool_calendar:
			//Toast.makeText(mContext, "id_tool_calendar", Toast.LENGTH_SHORT).show();
			mIntent.setClassName("com.android.calendar", "com.android.calendar.AllInOneActivity");
			break;
		case R.id.id_tool_alarm:
			//Toast.makeText(mContext, "id_tool_alarm", Toast.LENGTH_SHORT).show();
			mIntent.setClassName("com.android.deskclock", "com.android.deskclock.DeskClock");
			break;
		default:
			break;
		}
		try{
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(mIntent);
		}catch(Exception e){
			
		}
	}

}
