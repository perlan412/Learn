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

public class VViewPager extends ViewGroup {  
	static final boolean DEBUG = true;
	static final String  TAG = "VViewPager";
	
	protected GestureDetector mGestureDetector;  
	protected Scroller mScroller;
    
    boolean mCanMove = true;	
   
    private VelocityTracker mVelocityTracker;
    
    int mMinFlipingVelocity = 600;
    
    static int TOUCH_SLOP = 8;
  
    public VViewPager(Context context) {  
        super(context);  
        initView(context);  
    }  
  
    
    public VViewPager(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        initView(context);  
    }  
  
    public VViewPager(Context context, AttributeSet attrs, int defStyleAttr) {  
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
        	
        	int dy = (int)distanceY;
        	int height = getHeight();
        	int childCount = getChildCount();
        	int curScrollY = getScrollY();
        	
        	
        	if(DEBUG){
        		Log.d(TAG,"curScrollY = " + curScrollY + ",dy = " + dy+ ",height = " + height + ",childCount = " + childCount);	
        	}
        	
        	if(curScrollY + dy < 0 ){
        		scrollTo(getScrollX(),0);
        	}else if(curScrollY + dy > height * (childCount - 1)){
        		
        		scrollTo(getScrollX(),height * (childCount - 1));
        		
        	}else{
        		scrollBy(0, dy);
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
  
   
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent ev) {  
        switch(ev.getAction()) {  
            case MotionEvent.ACTION_DOWN:  
                mGestureDetector.onTouchEvent(ev); 
                mStartX2 = (int) ev.getX();  
                mStartY2 = (int) ev.getY();
                mStartY = mStartY2;
                break;  
            case MotionEvent.ACTION_MOVE:
                int endX2 = (int) ev.getX();  
                int endY2 = (int) ev.getY();  
                int dx = endX2 - mStartX2;  
                int dy = endY2 - mStartY2;  
  
                if(Math.abs(dx) < Math.abs(dy) &&  Math.abs(dy) > TOUCH_SLOP) { 
                    return true;  
                }  
                break;  
            default:  
                break;  
        }  
        
        return false;  
    }  
    
    
    private int mStartX2;  
    private int mStartY2;  
  
    protected int mIndex = 0; 
    private int mStartY;
    private int mEndY;
    
    
    @Override  
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event); 
        
        obtainVelocity(event);
        
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:  
                
            	mStartY = (int) event.getY();  
                break;  
            case MotionEvent.ACTION_MOVE:
            	
            	break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
               
                mEndY = (int) event.getY();  
                int velocity = Math.abs(getVelocity());
                
            	
            	
            	int height = getHeight();
                if((mEndY < mStartY) && ((mStartY - mEndY) > height/2 || velocity > mMinFlipingVelocity)) {  
                     
                	mIndex++;  
                } else if((mEndY > mStartY) && ((mEndY - mStartY) > height/2 || velocity > mMinFlipingVelocity)) {  
                   
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
    	scrollTo(getScrollX(),mIndex * getHeight());
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
        
        int curScrollY = getScrollY();
        if(DEBUG){
        	Log.d(TAG, "index = " + mIndex + ", height" + getHeight() + ", getScrollY" + curScrollY);	
        }
        
        mScroller.startScroll(getScrollX(), curScrollY, 0, (getHeight() * mIndex - curScrollY));  
        invalidate();
    }

    
   
    @Override  
    public void computeScroll() {
        if(mScroller != null) {
            if(mScroller.computeScrollOffset()) {
                scrollTo(0, mScroller.getCurrY()); 
                invalidate();  
            }
        }
        super.computeScroll();  
    }

    protected int getVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        return (int) mVelocityTracker.getXVelocity();
    }

    protected void obtainVelocity(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    
    protected void recycleVelocity() {
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
        for(int i=0; i < childCount; i++) {
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
            view.layout(0, 0 + height * i, width, height + height * i);  
        }
    }
  
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
    }
}
