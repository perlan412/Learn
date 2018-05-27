package com.alading.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class AppBaseActivity extends Activity{
	
	static final String  TAG = "AppBaseActivity";
	int mWindowWidth = 0;
	
    private int mStartX;
    private int mEndX;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
            		finish();
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
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	
	
}
