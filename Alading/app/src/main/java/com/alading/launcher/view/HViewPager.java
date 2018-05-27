package com.alading.launcher.view;  
  
import android.content.Context;  
import android.graphics.Canvas;  
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;  
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;  
import android.widget.Scroller;  

/**
 * written by huang fang ming
 */
public class HViewPager extends ViewGroup {
	static final boolean DEBUG = true;
	static final String  TAG = "HViewPager";
	
	protected GestureDetector mGestureDetector;
	protected Scroller mScroller; 
	protected boolean mMovable = true;
    
    
    private VelocityTracker mVelocityTracker;
    
    int mMinFlipingVelocity = 600;
    
    static int TOUCH_SLOP = 0;
  
    public HViewPager(Context context) {  
        super(context);  
        initView(context);  
    }  
  
     
    public HViewPager(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        initView(context);  
    }  
  
    public HViewPager(Context context, AttributeSet attrs, int defStyleAttr) {  
        super(context, attrs, defStyleAttr);  
        initView(context);  
    }  
  
    protected void initView(Context context) {  
    	TOUCH_SLOP = ViewConfiguration.get(context).getScaledTouchSlop();
        mGestureDetector = new GestureDetector(getContext(), new MySimpleOnGestureListener());  
        mScroller = new Scroller(getContext()); 
    } 
    
    
    private class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {  
        
        @Override  
        public boolean onScroll(MotionEvent e1, MotionEvent e2,  
                                float distanceX, float distanceY) {
          	if(!mMovable){
        		return super.onScroll(e1, e2, distanceX, distanceY); 
        	}
          	
        	int dx = (int)distanceX;
        	int width = getWidth();
        	int childCount = getChildCount();
        	int curScrollX = getScrollX();
        	
        	Log.d(TAG,"curScrollX = " + curScrollX + ",dx = " + dx+ ",width = " + width + ",childCount = " + childCount);
        	if(curScrollX + dx < 0 ){
        		scrollTo(0,getScrollY());
        	}else if(curScrollX + dx > width * (childCount - 1)){
        		scrollTo(width * (childCount - 1),getScrollY());
        	}else{
        		scrollBy(dx, 0); 	
        	}
        	
            
            return super.onScroll(e1, e2, distanceX, distanceY);  
        }  
    }  
    
  
   
    public interface OnPageChangedListener {  
        void onChange(int index);  
    }  
  
    protected OnPageChangedListener mOnPageChangedListener;  
  
    
    public void setOnPageChangedListener(OnPageChangedListener listener) {  
    	mOnPageChangedListener = listener;  
    }
    
    public void setMovable(boolean movable){
    	mMovable = movable;
    }
  
    private int mStartX2;  
    private int mStartY2;  
   
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	if(!mMovable){
    		return super.onInterceptTouchEvent(ev);
    	}
    	
        switch(ev.getAction()) {  
            case MotionEvent.ACTION_DOWN:  
                mGestureDetector.onTouchEvent(ev); 
                mStartX2 = (int) ev.getX();  
                mStartY2 = (int) ev.getY();
                mStartX = mStartX2;
                break;  
            case MotionEvent.ACTION_MOVE:
                int endX2 = (int) ev.getX();  
                int endY2 = (int) ev.getY();  
                int dx = endX2 - mStartX2;  
                int dy = endY2 - mStartY2;  
  
                if(Math.abs(dx) > Math.abs(dy) &&  Math.abs(dx) > TOUCH_SLOP) { 
                    return true;  
                }  
                break;
            default:  
                break;  
        }  
        
        return false;  
    }  
    
    
    protected int mIndex = 0; 
    protected int mStartX;
    protected int mEndX;
    
   
    @Override  
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event); 
      	if(!mMovable){
    		return super.onTouchEvent(event);
    	}
      	
        obtainVelocity(event);
        
        switch(event.getAction()) {  
            case MotionEvent.ACTION_DOWN:  
                
            	mStartX = (int) event.getX();  
                break;  
            case MotionEvent.ACTION_MOVE:
    
            	break;
            case MotionEvent.ACTION_UP: 
            case MotionEvent.ACTION_CANCEL:
                
                int mEndX = (int) event.getX();  
  
                int xVelocity = Math.abs(getVelocity());
                
            	if(DEBUG){
            		Log.d(TAG, "mEndX = " + mEndX + "mStartX = " +  mStartX + ", xVelocity = " + xVelocity);
            	}
            	
            	
                if((mEndX < mStartX) && ((mStartX - mEndX) > getWidth()/2 || xVelocity > mMinFlipingVelocity)) {  
                    
                	mIndex++;  
                } else if((mEndX > mStartX) && ((mEndX - mStartX) > getWidth()/2 || xVelocity > mMinFlipingVelocity)) {  
                    
                	mIndex--;  
                }  
                moveToIndex();
                recycleVelocity();
                break;      
            default:  
                break;  
        }  
       
        return true;  
  
    }
    
    
    public int getCurrentPage(){
    	return mIndex;
    }
    
    
    public void scrollToIndex(int index) {  
    	mIndex = index;
    	scrollTo(mIndex * getWidth(),getScrollY());
        if(mOnPageChangedListener != null) {  
        	mOnPageChangedListener.onChange(mIndex);  
        }  
    }
    
    
    public void moveToIndex(int index) {  
    	mIndex = index;  
        moveToIndex();
    }  
  
    protected void moveToIndex() {  
        if(mIndex < 0) {  
        	mIndex = 0;  
        }  
        if(mIndex == getChildCount()) {  
        	mIndex = getChildCount() -1;  
        }  
        if(mOnPageChangedListener != null) {  
        	mOnPageChangedListener.onChange(mIndex);  
        }  
        
        int curScrollX = getScrollX();
        
        if(DEBUG){
        	Log.d(TAG, "index = " + mIndex + ", getScrollX" + getWidth() + ", getScrollX" + curScrollX);	
        }
        
        mScroller.startScroll(curScrollX, getScrollY(), (getWidth() * mIndex - curScrollX), 0);  
        invalidate();  
    }

    
    
    @Override  
    public void computeScroll() {  
        if(mScroller != null) {
            if(mScroller.computeScrollOffset()) {  
                scrollTo(mScroller.getCurrX(), 0);  
                invalidate();  
            }  
        }  
        super.computeScroll();  
    }  
  
    
    private int getVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        return (int) mVelocityTracker.getXVelocity();
    }

    private void obtainVelocity(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

   
    private void recycleVelocity() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
    
    
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
        MeasureSpec.getMode(widthMeasureSpec); 
        MeasureSpec.getSize(widthMeasureSpec); 
        int childCount = getChildCount();
        
        for(int i=0; i< childCount; i++) {  
            View view = getChildAt(i);  
            view.measure(widthMeasureSpec, heightMeasureSpec);
        }  
    }  
  
   
    @Override  
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
  
    	int width = getWidth();
    	int height = getHeight();
    	int childCount = getChildCount();
        for(int i=0; i < childCount; i++) {
            View view = getChildAt(i);
            view.layout(0 + width * i, 0, width + width * i, height);
        }
    }
  
    @Override  
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
