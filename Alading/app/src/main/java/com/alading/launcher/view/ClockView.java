package com.alading.launcher.view;

import java.util.Calendar;

import com.alading.launcher.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;


/**
 * written by huang fang ming
 */

public class ClockView extends View implements Runnable{

	private Bitmap mBitmapBg,mBitmapHour,mBitmapMinute,mBitmapSecond,mBitmapPoint;
	private int mBgWidth,mBgHeight,mHourWidth,mHourHeight,mMinuteWidth,mMinuteHeight,mPointWidth,mPointHeight
	,mSecondWidth,mSecondHeight;
	private Matrix mHourMatrix,mMinuteMatrix,mSecondMatrix;
	private int mMinuteDeviation = 19,mSecondDeviation = 39,mHourValue = 100,mMinuteValue = 170,mSecondValue;
	private PaintFlagsDrawFilter mFlagsDrawFilter;
	private Paint mPaint;
	private boolean mIsRun = false;
	private boolean mAnchorCenter = true;
	public ClockView(Context context) {
		this(context,null);
	}
	
	public ClockView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	
	public ClockView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context,attrs,defStyle);
	}

	void init(Context context,AttributeSet attrs, int defStyle) {
		TypedArray typeArray = context.obtainStyledAttributes(attrs,R.styleable.ClockView, defStyle, 0);
		mAnchorCenter = typeArray.getBoolean(R.styleable.ClockView_anchor_center, true);
		mBitmapBg     = getBitmap(typeArray,R.styleable.ClockView_clock_bg);
		mBitmapHour   = getBitmap(typeArray,R.styleable.ClockView_clock_hour);
		mBitmapMinute = getBitmap(typeArray,R.styleable.ClockView_clock_minute);
		mBitmapSecond = getBitmap(typeArray,R.styleable.ClockView_clock_second);
		mBitmapPoint  = getBitmap(typeArray,R.styleable.ClockView_clock_time_point);
		
		if(mBitmapBg != null){
			mBgWidth      = mBitmapBg.getWidth();
			mBgHeight     = mBitmapBg.getHeight();
		}
		
		if(mBitmapHour != null){
			mHourWidth    = mBitmapHour.getWidth();
			mHourHeight   = mBitmapHour.getHeight();
		}
		
		if(mBitmapMinute != null){
			mMinuteWidth  = mBitmapMinute.getWidth();
			mMinuteHeight = mBitmapMinute.getHeight();
		}
		
		if(mBitmapSecond != null){
			mSecondMatrix = new Matrix();
			mSecondWidth   = mBitmapSecond.getWidth();
			mSecondHeight  = mBitmapSecond.getHeight();
		}
		
		mFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true); 
		mPaint.setFilterBitmap(true);
		mHourMatrix   = new Matrix();
		mMinuteMatrix = new Matrix();
		
		update();
	}
	
	public Bitmap getBitmap(TypedArray typeArray,int index){
		BitmapDrawable drawable = (BitmapDrawable) typeArray.getDrawable(index);
		if(drawable != null){
			return drawable.getBitmap();
		}
		return null;
	}
	
	public void setTime(int hour,int minute){
		mHourValue   = ((hour * 60 + minute) >> 1) % 360;
		mMinuteValue = minute * 6;
		update();
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		update();
		mIsRun = true;
		if(mIsRun)
			new Thread(this).start();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mIsRun = false;
	}
	
	@Override   
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   
	  setMeasuredDimension(this.getMeasuredSize(widthMeasureSpec, true), this.getMeasuredSize(heightMeasureSpec, false));  
	} 
	
	private int getMeasuredSize(int length, boolean isWidth){  
	    int specMode = MeasureSpec.getMode(length);  
	    int specSize = MeasureSpec.getSize(length);  
	    int retSize = 0;          
	    int padding = (isWidth? getPaddingLeft()+getPaddingRight():getPaddingTop()+getPaddingBottom());  
	    if(specMode==MeasureSpec.EXACTLY){ 
	        retSize = specSize;  
	    }else{
	        retSize = (isWidth? mBitmapBg.getWidth()+padding : mBitmapBg.getHeight()+padding);  
	        if(specMode==MeasureSpec.UNSPECIFIED){  
	        	retSize = Math.min(retSize, specSize);  
	        }  
	    }          
	    return retSize;  
	} 
	
	private void update(){
		
		mHourMatrix.reset();
		if(mAnchorCenter){
			mHourMatrix.setRotate(mHourValue,mHourWidth>>1,mHourHeight>>1);
			mHourMatrix.postTranslate((mBgWidth - mHourWidth)>>1, (mBgHeight - mHourHeight)>>1);
		}else {
			mHourMatrix.setRotate(mHourValue,mHourWidth>>1,mHourHeight - mMinuteDeviation);
			mHourMatrix.postTranslate((mBgWidth - mHourWidth)>>1, (mBgHeight >>1) - mHourHeight + mMinuteDeviation);	
		}
		
		mMinuteMatrix.reset();
		if(mAnchorCenter){
			mMinuteMatrix.setRotate(mMinuteValue,mMinuteWidth>>1,mMinuteHeight>>1);
			mMinuteMatrix.postTranslate((mBgWidth - mMinuteWidth)>>1, (mBgHeight - mMinuteHeight)>>1);	
		}else {
			mMinuteMatrix.setRotate(mMinuteValue,mMinuteWidth>>1,mMinuteHeight - mMinuteDeviation);
			mMinuteMatrix.postTranslate((mBgWidth - mMinuteWidth)>>1, (mBgHeight >>1) - mMinuteHeight + mMinuteDeviation);		
		}

		if(mBitmapSecond != null){
			mSecondMatrix.reset();
			if(mAnchorCenter){
				mSecondMatrix.setRotate(mSecondValue,mSecondWidth>>1,mSecondHeight>>1);
				mSecondMatrix.postTranslate((mBgWidth - mSecondWidth)>>1, (mBgHeight  - mSecondHeight)>>1);	
			}else {
				mSecondMatrix.setRotate(mSecondValue,mSecondWidth>>1,mSecondHeight - mSecondDeviation);
				mSecondMatrix.postTranslate((mBgWidth - mSecondWidth)>>1, (mBgHeight >>1) - mSecondHeight + mSecondDeviation);
			}
		}
		
		postInvalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.setDrawFilter(mFlagsDrawFilter);
//		canvas.drawBitmap(mBitmapBg, 0, 0, null);
		if(mBitmapHour != null)
			canvas.drawBitmap(mBitmapHour, mHourMatrix, mPaint);
		if(mBitmapMinute != null)
			canvas.drawBitmap(mBitmapMinute, mMinuteMatrix, mPaint);
		if(mBitmapSecond != null)
			canvas.drawBitmap(mBitmapSecond, mSecondMatrix, mPaint);
		if(mBitmapPoint != null)
			canvas.drawBitmap(mBitmapPoint, (mBgWidth - mPointWidth) >> 1, (mBgHeight - mPointHeight) >> 1, null);
	}

	@Override
	public void run() {
		while (mIsRun) {
			Calendar calendar = Calendar.getInstance();
			
			int hour = calendar.get(Calendar.HOUR);
			int minute = calendar.get(Calendar.MINUTE);
			int second = calendar.get(Calendar.SECOND);
			
			mHourValue = (hour * 60 + minute) >> 1;
			mMinuteValue = minute * 6; 
			mSecondValue = second * 6;
			
			update();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
