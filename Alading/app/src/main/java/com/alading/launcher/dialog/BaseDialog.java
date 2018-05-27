package com.alading.launcher.dialog;

import android.content.Context;
import android.view.ViewGroup;

public abstract class BaseDialog {
	protected Context mContext;
	protected ViewGroup mContentParent;
	
	BaseDialog(Context context){
		mContext = context;
	}
	
	public void onDestroy(){
	}
	
	public ViewGroup getContentView() {
		return mContentParent;
	}
	
	public boolean onKeyBack() {
		return false;
	}
	
	public void pageUp(){}
	public void pageDown(){}
}
