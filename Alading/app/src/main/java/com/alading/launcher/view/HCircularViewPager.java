package com.alading.launcher.view;  
  
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;  


/**
 * written by huang fang ming
 */

public class HCircularViewPager extends HViewPager {
	static final boolean DEBUG = true;
	static final String  TAG = "HCircularViewPager";
	
    public HCircularViewPager(Context context) {  
        super(context);
        setWillNotDraw(false);
    }  
  
    
    public HCircularViewPager(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        setWillNotDraw(false);
    }  
  
    public HCircularViewPager(Context context, AttributeSet attrs, int defStyleAttr) {  
        super(context, attrs, defStyleAttr);  
        setWillNotDraw(false);
    }  
  
    
    private class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {  
        
        @Override  
        public boolean onScroll(MotionEvent e1, MotionEvent e2,  
                                float distanceX, float distanceY) {
        	int dx = (int)distanceX;
        	
        	scrollBy(dx, 0);
            
            return super.onScroll(e1, e2, distanceX, distanceY);  
        }  
    }  
    
    @Override 
    protected void initView(Context context) {  
        mGestureDetector = new GestureDetector(getContext(), new MySimpleOnGestureListener());  
        mScroller = new Scroller(getContext()); 
    }  
  

    @Override 
    protected void moveToIndex() {  
        
        int curScrollX = getScrollX();
        
        if(DEBUG){
        	Log.d(TAG, "index = " + mIndex + ", getWidth = " + getWidth() + ", getScrollX = " + curScrollX);	
        }
        
        mScroller.startScroll(curScrollX, getScrollY(), (getWidth() * mIndex - curScrollX), 0);  
        invalidate(); 
        
    	
        if(mOnPageChangedListener != null) {  
            int childCount = getChildCount();
            int index = mIndex % childCount;
        	if(index < 0){
        		index = index + childCount;
        	}
        	Log.d(TAG, "index = " + index + ", childCount = " + childCount);
            mOnPageChangedListener.onChange(index);  
        }  
    }

  
    int mLastScrollX = -1;
    
    
    @Override  
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(DEBUG){
			Log.d(TAG, "onLayout");
		}
		
    	int scrollX = getScrollX();
    	mLastScrollX = -1;
    	if(mLastScrollX != scrollX){
    		//Log.d(TAG, "mLastScrollX = " + mLastScrollX + ",getScrollX = " + getScrollX());
    		layoutForCircular();
    		mLastScrollX = scrollX;
    	}
    	
    }
  
    void layoutForCircular(){
    	
      	int width = getWidth();
    	int height = getHeight();
    	int childCount = getChildCount();
    	
    	View view = null;
    	int curScroll = getScrollX();
    	
    	
        for(int i=0; i < childCount; i++) {
            view = getChildAt(i);
            view.layout(curScroll + (width<<1), 0, curScroll + (width<<2), height);
        }
    	
    	if(childCount == 0){
    		
    	}else if(childCount == 1){
    		view = getChildAt(0);
    		view.layout(0, 0, width, height);
    	}else{

    		//============================
    		if(curScroll == 0){
        		view = getChildAt(0);
        		view.layout(0, 0, width, height);
    		}else{
    			
    			if(curScroll > 0){
    				
    				int multiple = curScroll/width;
        			int index = multiple % childCount;
        			
            		view = getChildAt(index);
            		view.layout(multiple * width, 0, width + multiple * width, height);
            		
            		
            		multiple = multiple + 1;
            		if(index == childCount - 1){
            			index = 0;
            		}else{
            			index = index + 1;
            		}

            		
            		view = getChildAt(index);
            		view.layout(multiple * width, 0, width + multiple * width, height);
    			}else{
    				
    				int multiple = curScroll/width;
        			int index = multiple % childCount;
        			if(DEBUG){
        			//	Log.d(TAG, "curScroll = " + curScroll + ",multiple = " + multiple + ", index = " + index);
        			}
        			if(index == 0){
        				
                		view = getChildAt(index);
                		view.layout(multiple * width, 0, width + multiple * width, height);
                		
                		
                		multiple = multiple - 1;
                		view = getChildAt(childCount - 1);
                		view.layout(multiple * width, 0, width + multiple * width, height);
        			}else{
        				
        				view = getChildAt(index + childCount);
        				view.layout(multiple * width, 0, width + multiple * width, height);

        				
        				multiple = multiple - 1;
        				view = getChildAt(index + childCount - 1);
        				view.layout(multiple * width, 0, width + multiple * width, height);
        			}
    			}
    			
    		}
    	}
    }

    
    @Override  
    protected void onDraw(Canvas canvas) {
    	int scrollX = getScrollX();
    	if(mLastScrollX != scrollX){
    		//Log.d(TAG, "mLastScrollX = " + mLastScrollX + ",getScrollX = " + getScrollX());
    		layoutForCircular();
    		mLastScrollX = scrollX;
    	}
    	
        super.onDraw(canvas);
    }
}
