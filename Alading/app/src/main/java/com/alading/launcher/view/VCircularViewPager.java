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

public class VCircularViewPager extends VViewPager {  
	static final boolean DEBUG = true;
	static final String  TAG = "VCircularViewPager";
	
    public VCircularViewPager(Context context) {  
        super(context);  
    }  
  
    
    public VCircularViewPager(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public VCircularViewPager(Context context, AttributeSet attrs, int defStyleAttr) {  
        super(context, attrs, defStyleAttr);  
    }  
  
    @Override 
    protected void initView(Context context) {  
        mGestureDetector = new GestureDetector(getContext(), new MySimpleOnGestureListener());  
        mScroller = new Scroller(getContext()); 
    }  
    
   
    private class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {  
        
        @Override  
        public boolean onScroll(MotionEvent e1, MotionEvent e2,  
                                float distanceX, float distanceY) {
        	
        	int dy = (int)distanceY;
        	scrollBy(0, dy);	

        	
            return super.onScroll(e1, e2, distanceX, distanceY);  
        }  
    }  
    
    
    @Override 
    protected void moveToIndex() {
        
        int curScrollY = getScrollY();
        if(DEBUG){
        	Log.d(TAG, "index = " + mIndex + ", height" + getHeight() + ", getScrollY" + curScrollY);	
        }
        
        mScroller.startScroll(getScrollX(), curScrollY, 0, (getHeight() * mIndex - curScrollY));  
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

    
  	int mLastScrollY = -1;
    
    @Override  
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(DEBUG){
			Log.d(TAG, "onLayout");
		}
      
		mLastScrollY = -1;
    	int scrollY = getScrollY();
    	if(mLastScrollY != scrollY){
    		Log.d(TAG, "mLastScrollY = " + mLastScrollY + ",getScrollY = " + scrollY);
    		layoutForCircular();
    		mLastScrollY = scrollY;
    	}
        
    }
  
  private void layoutForCircular(){
    	
      	int width = getWidth();
      	if(DEBUG){
      		Log.d(TAG, "width = " + width);	
      	}
      	
    	int height = getHeight();
    	int childCount = getChildCount();
    	
    	View view = null;
    	int curScroll = getScrollY();
    	
    	
        for(int i=0; i < childCount; i++) {
            view = getChildAt(i);
            view.layout(0, curScroll + (height<<1), width, curScroll + (height<<2));
        }
    	
    	if(childCount == 0){
    		
    	}else if(childCount == 1){
    		view = getChildAt(0);
    		view.layout(0, 0, width, height);
    	}else{

    		
    		if(curScroll == 0){
        		view = getChildAt(0);
        		view.layout(0, 0, width, height);
    		}else{
    			
    			if(curScroll > 0){
    				
    				int multiple = curScroll/height;
        			int index = multiple % childCount;
        			
            		view = getChildAt(index);
            		view.layout(0, multiple * height, width, height + multiple * height);
            		
            		
            		multiple = multiple + 1;
            		if(index == childCount - 1){
            			index = 0;
            		}else{
            			index = index + 1;
            		}

            		
            		view = getChildAt(index);
            		view.layout(0, multiple * height, width, height + multiple * height);
    			}else{
    				
    				int multiple = curScroll/height;
        			int index = multiple % childCount;
        			
        			if(DEBUG){
        				Log.d(TAG, "curScroll = " + curScroll + ",multiple = " + multiple + ", index = " + index);
        			}
        			
        			if(index == 0){
        				
                		view = getChildAt(index);
                		view.layout(0, multiple * height, width, height + multiple * height);
                		
                		
                		multiple = multiple - 1;
                		view = getChildAt(childCount - 1);
                		view.layout(0, multiple * height, width, height + multiple * height);
        			}else{
        				
        				view = getChildAt(index + childCount);
        				view.layout(0, multiple * height, width, height + multiple * height);

        				
        				multiple = multiple - 1;
        				view = getChildAt(index + childCount - 1);
        				view.layout(0, multiple * height, width, height + multiple * height);
        			}
    			}
    			
    		}
    	}
    }
  
  	
    @Override  
    protected void onDraw(Canvas canvas) {
    	
    	int scrollY = getScrollY();
    	if(mLastScrollY != scrollY){
    		Log.d(TAG, "mLastScrollY = " + mLastScrollY + ",getScrollY = " + getScrollY());
    		layoutForCircular();
    		mLastScrollY = scrollY;
    	}
    	
        super.onDraw(canvas);  
    }
}
