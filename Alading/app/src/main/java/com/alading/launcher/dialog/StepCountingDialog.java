package com.alading.launcher.dialog;

import com.alading.launcher.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.view.ViewGroup;

public class StepCountingDialog extends BaseDialog implements OnClickListener{

	static final String  TAG = "StepCountingDialog";
	ImageView mImageView = null;
	public StepCountingDialog(Context context) {
		
		super(context);
		mContentParent = (ViewGroup) View.inflate(mContext, R.layout.dialog_step_counting, null); 
		if(mContentParent != null){
			mContentParent.setOnClickListener(this);
//			mImageView = (ImageView)mContentParent.findViewById(R.id.item_image1);
//			Glide.with(mContext)
//	        .load(R.drawable.count_step_bg)
//	        .diskCacheStrategy(DiskCacheStrategy.NONE)
//	        .listener(new RequestListener<Integer, GlideDrawable>() {
//
//	          @Override
//	          public boolean onException(Exception arg0, Integer arg1,
//	              Target<GlideDrawable> arg2, boolean arg3) {
//	            return false;
//	          }
//
//	          @Override
//	          public boolean onResourceReady(GlideDrawable resource,
//	              Integer model, Target<GlideDrawable> target,
//	              boolean isFromMemoryCache, boolean isFirstResource) {
//
//	            GifDrawable drawable = (GifDrawable) resource;
//	            GifDecoder decoder = drawable.getDecoder();
//	            int duration = 0;
//	            for (int i = 0; i < drawable.getFrameCount(); i++) {
//	              duration += decoder.getDelay(i);
//	            }
//	            Log.d(TAG, "duration = " + duration);
//
//	            return false;
//	          }
//	        })
//	        .into(mImageView);
		}

	
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.id_step:
			Log.d(TAG, "id_step");
			break;
		default:
			break;
		}
	}

}
