package com.alading.launcher.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

public class AppMenuDialog extends Dialog{

	static final String  TAG = "AppMenuDialog";
	int mWindowWidth = 0;
	
	private int mStartX;
	private int mEndX;
	    
	public AppMenuDialog(Context context) {
		super(context);
		init();
	}
	
	public AppMenuDialog(Context context, int theme) {
		super(context, theme);
		init();
	}
	
	
	void init(){
		mWindowWidth = getWindow().getWindowManager().getDefaultDisplay().getWidth();
		Log.d(TAG,"mWindowWidth = " + mWindowWidth);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
	      
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:  
            	mStartX = (int) event.getX(); 
            	Log.d(TAG,"MotionEvent.ACTION_DOWN");
                break;  
            case MotionEvent.ACTION_MOVE:

            	break;
            case MotionEvent.ACTION_UP:  
       
            	mEndX = (int) event.getX();
            	Log.d(TAG,"MotionEvent.ACTION_UP");
            	if(Math.abs(mEndX - mStartX) > (mWindowWidth/4)){
            		dismiss();
            	}
                break;
            case MotionEvent.ACTION_CANCEL:
            	break;
            default:
                break;  
        }
        
        return super.dispatchTouchEvent(event);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.d(TAG,"onBackPressed = ");
	}
	
}
