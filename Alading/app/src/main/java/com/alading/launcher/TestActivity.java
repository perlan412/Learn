package com.alading.launcher;

import com.alading.launcher.dialog.CommunicationDialog;
import com.alading.launcher.view.HCircularViewPager;
import com.alading.launcher.view.HViewPager;
import com.alading.launcher.view.VCircularViewPager;
import com.alading.launcher.view.VViewPager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

public class TestActivity extends Activity {
	private HViewPager mHViewPager;
	private VViewPager mVViewPager;
    private RadioGroup mRadioGroup;  
    private HCircularViewPager mHCircularViewPager;
    private VCircularViewPager mVCircularViewPager;
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }
    
    void initView(){
    	
    	mVCircularViewPager = (VCircularViewPager)View.inflate(getApplicationContext(), R.layout.v_circular_pager, null);
        int color = 0xFF0000FF;
        
    	for(int i=0; i < 4; i++) {
            ImageView imageView = new ImageView(getApplicationContext());  
            imageView.setImageResource(R.drawable.animation1);
            color = color + i * 6;
            imageView.setBackgroundColor(color);
            mVCircularViewPager.addView(imageView);  
        } 
        
        setContentView(R.layout.test_activity_main);
        mHCircularViewPager = (HCircularViewPager) findViewById(R.id.id_h_view_pager);
        
        CommunicationDialog mCommunicationDialog = new CommunicationDialog(getApplicationContext());
        
        mHCircularViewPager.addView(mCommunicationDialog.getContentView());
        
        for(int i=0; i < 4; i++) {
            ImageView imageView = new ImageView(getApplicationContext());  
            imageView.setImageResource(R.drawable.animation1);
            mHCircularViewPager.addView(imageView);  
        }
        
        mHCircularViewPager.addView(mVCircularViewPager, 1); 
        
        View view = View.inflate(getApplicationContext(), R.layout.scroll_layout, null);  
        mHCircularViewPager.addView(view, 2); 
        
  
    
    }
}
