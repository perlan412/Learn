package com.alading.launcher;

import com.alading.launcher.dialog.CommunicationDialog;
import com.alading.launcher.dialog.DeviceStateDialog;
import com.alading.launcher.dialog.EntertainmentDialog;
import com.alading.launcher.dialog.GpsAndLearnDialog;
import com.alading.launcher.dialog.MainDialog;
import com.alading.launcher.dialog.MakeFriendsDialog;
import com.alading.launcher.dialog.MessageDialog;
import com.alading.launcher.dialog.SafeStudyDialog;
import com.alading.launcher.dialog.SettingsDialog;
import com.alading.launcher.dialog.SosDialog;
import com.alading.launcher.dialog.StepCountingDialog;
import com.alading.launcher.dialog.ToolDialog;
import com.alading.launcher.dialog.WeatherDialog;
import com.alading.launcher.dialog.WeiChatDialog;
import com.alading.launcher.dialog.XiaodingDialog;
import com.alading.launcher.service.RegisterService;
import com.alading.launcher.utils.ReadSqlUtils;
import com.alading.launcher.utils.ReadSqlUtils.ReadCallBack;
import com.alading.launcher.view.HCircularViewPager;
import com.alading.launcher.view.HViewPager;
import com.alading.launcher.view.VCircularViewPager;
import com.alading.launcher.view.VViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.os.SystemProperties;
import android.util.Log;
import android.view.View;

/**
 * written by huang fang ming
 */
public class MainActivity extends Activity implements ReadCallBack {
	static final String TAG = "MainActivity";
	private HViewPager mHViewPager;
	private VViewPager mVViewPager;
	
	private static final String POWER_CAN_SCREEN_OFF="persist.sys.screenoff";
	private VCircularViewPager mVLeftCircularViewPager;
	private HCircularViewPager mHLeftCircularViewPager;
	private HCircularViewPager mHBottomCircularViewPager;

	MainDialog mMainDialog;

	boolean mViewInit = false;

	private ReadSqlUtils mReadSqlUtils;
	private MessageDialog mMessageDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		//注册过程中crash后再次启动注册服务
//		retryStartRegisterService();
	}

	private void retryStartRegisterService() {
		Log.d(TAG,"retryStartRegisterService");
		Intent intent = new Intent(this, RegisterService.class);
		startService(intent);
	}

	void initView() {
		boolean useHLeftCircularViewPager = true;

		mReadSqlUtils = ReadSqlUtils.getInstance(this);
		mReadSqlUtils.setCallBack(this);
		mVLeftCircularViewPager = (VCircularViewPager) View.inflate(getApplicationContext(), R.layout.v_circular_pager,null);
		mHLeftCircularViewPager = (HCircularViewPager) View.inflate(getApplicationContext(), R.layout.h_circular_pager,null);

		SosDialog sosDialog = new SosDialog(getApplicationContext());
		GpsAndLearnDialog gpsAndLearnDialog = new GpsAndLearnDialog(getApplicationContext());
		StepCountingDialog stepCountingialog = new StepCountingDialog(this);
		MakeFriendsDialog makeFriendsDialog = new MakeFriendsDialog(this);
		SettingsDialog settingsDialog = new SettingsDialog(this);
		WeiChatDialog weiChatDialog = new WeiChatDialog(this);
		XiaodingDialog robertDialog = new XiaodingDialog(this);
		if (useHLeftCircularViewPager) {
			mHLeftCircularViewPager.addView(sosDialog.getContentView());
			mHLeftCircularViewPager.addView(gpsAndLearnDialog.getContentView());
			mHLeftCircularViewPager.addView(stepCountingialog.getContentView());
			mHLeftCircularViewPager.addView(makeFriendsDialog.getContentView());
			mHLeftCircularViewPager.addView(settingsDialog.getContentView());
			mHLeftCircularViewPager.addView(weiChatDialog.getContentView());
			mHLeftCircularViewPager.addView(robertDialog.getContentView());
		} else {
			mVLeftCircularViewPager.addView(sosDialog.getContentView());
			mVLeftCircularViewPager.addView(gpsAndLearnDialog.getContentView());
			mVLeftCircularViewPager.addView(stepCountingialog.getContentView());
			mVLeftCircularViewPager.addView(makeFriendsDialog.getContentView());
			mVLeftCircularViewPager.addView(settingsDialog.getContentView());
			mVLeftCircularViewPager.addView(weiChatDialog.getContentView());
			mVLeftCircularViewPager.addView(robertDialog.getContentView());
		}

		mHBottomCircularViewPager = (HCircularViewPager) View.inflate(getApplicationContext(),
				R.layout.h_circular_pager, null);

		StepCountingDialog stepCountingDialog = new StepCountingDialog(getApplicationContext());
		WeatherDialog weatherDialog = new WeatherDialog(getApplicationContext());
		// HealthDialog healthDialog = new
		// HealthDialog(getApplicationContext());

		mHBottomCircularViewPager.addView(stepCountingDialog.getContentView());
		mHBottomCircularViewPager.addView(weatherDialog.getContentView());
		// mHBottomCircularViewPager.addView(healthDialog.getContentView());

		setContentView(R.layout.activity_main);
		mHViewPager = (HViewPager) findViewById(R.id.id_h_view_pager);

		mMessageDialog = new MessageDialog(getApplicationContext());
		mHViewPager.addView(mMessageDialog.getContentView());

		mVViewPager = (VViewPager) View.inflate(getApplicationContext(), R.layout.v_view_pager, null);

		DeviceStateDialog deviceStateDialog = new DeviceStateDialog(getApplicationContext());
		mVViewPager.addView(deviceStateDialog.getContentView());

		mMainDialog = new MainDialog(getApplicationContext());
		mVViewPager.addView(mMainDialog.getContentView());

		mVViewPager.addView(mHBottomCircularViewPager);

		mHViewPager.addView(mVViewPager);

		if (useHLeftCircularViewPager) {
			mHViewPager.addView(mHLeftCircularViewPager);

			mHViewPager.setOnPageChangedListener(new HViewPager.OnPageChangedListener() {

				@Override
				public void onChange(int index) {
//					if(index==1){
//						SystemProperties.set(POWER_CAN_SCREEN_OFF, "true");
//					}else{
//						SystemProperties.set(POWER_CAN_SCREEN_OFF, "false");
//					}
					if (index == 2) {
						mHViewPager.setMovable(false);
					} else {
						mHViewPager.setMovable(true);
					}
				}
			});
		} else {
			mHViewPager.addView(mVLeftCircularViewPager);
		}

		mVViewPager.setOnPageChangedListener(new VViewPager.OnPageChangedListener() {

			@Override
			public void onChange(int index) {
//				if(index==1){
//					SystemProperties.set(POWER_CAN_SCREEN_OFF, "true");
//				}else{
//					SystemProperties.set(POWER_CAN_SCREEN_OFF, "false");
//				}
				
				if (index != 1) {
					mHViewPager.setMovable(false);
				} else {
					mHViewPager.setMovable(true);
				}
			}
		});

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		if (!mViewInit) {
			mHViewPager.scrollToIndex(1);
			mVViewPager.scrollToIndex(1);
			mViewInit = true;
		}
//		if(mHViewPager.getCurrentPage() == 1 && mVViewPager.getCurrentPage() == 1) {
//			SystemProperties.set(POWER_CAN_SCREEN_OFF, "true");
//		}
	}

	@Override
	public void onBackPressed() {

		Log.d(TAG, "onBackPressed");
		if (mHViewPager.getCurrentPage() == 1 && mVViewPager.getCurrentPage() == 1) {
			// super.onBackPressed();
			return;
		}

		if (mHViewPager.getCurrentPage() != 1) {
			mHViewPager.scrollToIndex(1);
		}

		if (mVViewPager.getCurrentPage() != 1) {
			mVViewPager.scrollToIndex(1);
		}

	}

//	public void onPause(){
//		super.onPause();
//		SystemProperties.set(POWER_CAN_SCREEN_OFF, "false");
//	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mViewInit = false;
		if (mMainDialog != null) {
			mMainDialog.onDestroy();
		}
		mReadSqlUtils.onDestory();
	}

	@Override
	public void unReadCallsCallBack(int count) {
		mMessageDialog.updateUnReadCallCount(count);
	}

	@Override
	public void unReadMessageCallBack(int count) {
		mMessageDialog.updateUnReadMessageCount(count);
	}

}
