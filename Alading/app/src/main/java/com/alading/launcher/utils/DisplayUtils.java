package com.alading.launcher.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplayUtils {

	private static DisplayUtils mDisplayUtils;
	
	private int mW;
	private int mH;
	public static DisplayUtils getInstance(Context context){
		if(mDisplayUtils==null){
			mDisplayUtils=new DisplayUtils(context);
		}
		return mDisplayUtils;
	}
	private DisplayUtils(Context context){
		
		DisplayMetrics mDisplayMetrics=new DisplayMetrics();
		WindowManager mWm=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		mWm.getDefaultDisplay().getMetrics(mDisplayMetrics);
		mW=mDisplayMetrics.widthPixels;
		mH=mDisplayMetrics.heightPixels;
	}
	public int getScreenW(){
		return mW;
	}
	public int getScreenH(){
		return mH;
	}
}
