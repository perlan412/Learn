package com.alading.launcher.dialog;

import com.alading.launcher.EditNumberActivity;
import com.alading.launcher.R;
import com.alading.launcher.utils.DateUtilsEx;
import com.alading.launcher.utils.EditNumberUtils;
import com.alading.launcher.utils.NetworkUtils;
import com.alading.launcher.utils.StepCount;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

/**
 * written by huang fang ming
 */
@SuppressLint("ClickableViewAccessibility")
public class MainDialog extends BaseDialog implements OnClickListener {

	static final boolean DEBUG = false;
	static final String TAG = "MainDialog";
	public static final String SCHEME_TEL = "tel";
	public static final String SCHEME_SIP = "sip";
	public static final String EXTRA_PHONE_ID = "phone_id";
	public static final String NOT_NEED_SIMCARD_SELECTION = "NOT_NEED_SIMCARD_SELECTION";
	public static final String ACTION_CALL_PRIVILEGED = "android.intent.action.CALL_PRIVILEGED";
	public static final ComponentName CALL_INTENT_DESTINATION = new ComponentName("com.android.phone",
			"com.android.phone.PrivilegedOutgoingCallBroadcaster");
	
	private WifiManager mWifiManager;
	TelephonyManager mTelephonyManager;
	TimeBroadcastReceiver mTimeBroadcastReceiver = null;
	BatteryReceiver mBatteryReceiver = null;
	WifiStateReceiver mWifiStateReceiver = null;
	private TextView mDateText;
	private TextView mMncNameText;

	private ImageView mImageHourHigh, mImageHourLow, mImageMinuteHigh, mImageMinuteLow;
	private ImageView mImageWifiSignal;
	private ImageView mImageMobileSignal;

	private ImageView mImageMoblieLable;

	private BitmapDrawable mWarningDrawable = null;
	private ClipDrawable mBatteryClipDrawable = null;
	private BitmapDrawable mEmptyDrawable = null;

	private int mCurrentBattery = 0;
	private int mBatteryChargeIndex = 25;
	private ImageView mImageBattery;

	private int[] mTimeImageIndexs = new int[] { R.drawable.time_0, R.drawable.time_1, R.drawable.time_2,
			R.drawable.time_3, R.drawable.time_4, R.drawable.time_5, R.drawable.time_6, R.drawable.time_7,
			R.drawable.time_8, R.drawable.time_9 };

	final int MESSAGE_WIFI_CHECK = 1;
	final int MESSAGE_BATTERY = 2;
	final int MESSAGE_MOBILE_SIGNAL = 3;
	private int mClickIndex = 0;
	private int mCurrentId = 0;

	private SensorManager manager;
	private Sensor sensor = null;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mHandler.removeMessages(msg.what);
			switch (msg.what) {
			case MESSAGE_WIFI_CHECK:
				int rssi = mWifiManager.getConnectionInfo().getRssi();
				int level = Math.abs(rssi);
				Log.i(TAG, "getRssi:" + rssi);
				mImageWifiSignal.setImageResource(R.drawable.wifi_selector);
				mImageWifiSignal.setImageLevel(level);
				mHandler.removeMessages(MESSAGE_WIFI_CHECK);
				mHandler.sendEmptyMessageDelayed(MESSAGE_WIFI_CHECK, 5000);
				break;
			case MESSAGE_BATTERY:
				if (mBatteryChargeIndex + 25 <= 100) {
					mBatteryChargeIndex += 25;
				} else {
					mBatteryChargeIndex = 25;
				}
				mBatteryClipDrawable.setLevel(mBatteryChargeIndex * 100);
				mHandler.sendEmptyMessageDelayed(MESSAGE_BATTERY, 500);
				break;
			case MESSAGE_MOBILE_SIGNAL:
				break;
			default:
				break;
			}
		}
	};

	public MainDialog(Context context) {

		super(context);
		mContentParent = (ViewGroup) View.inflate(mContext, R.layout.dialog_main, null);
		if (mContentParent != null) {
			// mContentParent.setOnClickListener(this);
			mContentParent.findViewById(R.id.id_call_dialer).setOnClickListener(this);
			mContentParent.findViewById(R.id.id_call_contacts).setOnClickListener(this);
			//mContentParent.findViewById(R.id.id_call_dad).setOnClickListener(this);
		}

		init(context);
	}

	void init(Context context) {

		// --------------------time init

		mImageHourHigh = (ImageView) mContentParent.findViewById(R.id.id_time_hour_0);
		mImageHourLow = (ImageView) mContentParent.findViewById(R.id.id_time_hour_1);
		mImageMinuteHigh = (ImageView) mContentParent.findViewById(R.id.id_time_minute_0);
		mImageMinuteLow = (ImageView) mContentParent.findViewById(R.id.id_time_minute_1);
		mDateText = (TextView) mContentParent.findViewById(R.id.id_date_week);

		refreshTime();

		// ====================== register timer broadcast receiver
		mTimeBroadcastReceiver = new TimeBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_TICK);
		filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
		filter.addAction(Intent.ACTION_TIME_CHANGED);
		filter.addAction(Intent.ACTION_LOCALE_CHANGED);
		context.registerReceiver(mTimeBroadcastReceiver, filter);

		// --------------------------------init MNC
		mMncNameText = (TextView) mContentParent.findViewById(R.id.id_mnc_name);
		String mncName = NetworkUtils.getCallSimNameLabel(mContext, 0);
		if (mncName == null) {
			mncName = mContext.getString(R.string.insert_sim_card);
		}
		mMncNameText.setText(mncName);

		// ----------------------battery init
		mImageBattery = (ImageView) mContentParent.findViewById(R.id.id_image_battery);
		LayerDrawable mLayerDrawable = (LayerDrawable) mImageBattery.getDrawable();
		mBatteryClipDrawable = (ClipDrawable) mLayerDrawable.findDrawableByLayerId(R.id.id_clip_drawable);
		mWarningDrawable = (BitmapDrawable) mLayerDrawable.findDrawableByLayerId(R.id.id_drawalbe_warming);
		mEmptyDrawable = (BitmapDrawable) mLayerDrawable.findDrawableByLayerId(R.id.id_drawable_empty);
		mBatteryReceiver = new BatteryReceiver();
		filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent intent = context.registerReceiver(mBatteryReceiver, filter);
		if (intent != null) {
			refreshBattery(intent);
		}

		// ---------------------wifi signal init
		mImageWifiSignal = (ImageView) mContentParent.findViewById(R.id.id_wifi_signal);
		mWifiStateReceiver = new WifiStateReceiver();
		filter = new IntentFilter();
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		context.registerReceiver(mWifiStateReceiver, filter);

		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		int rssi = mWifiManager.getConnectionInfo().getRssi();
		int level = Math.abs(rssi);
		mImageWifiSignal.setImageResource(R.drawable.wifi_selector);
		mImageWifiSignal.setImageLevel(level);

		// ------------------Sim change listen
		mImageMobileSignal = (ImageView) mContentParent.findViewById(R.id.id_mobile_signal);
		mImageMoblieLable = (ImageView) mContentParent.findViewById(R.id.id_mobile_lable);
		mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		mTelephonyManager.listen(mPhoneStateListener,
				PhoneStateListener.LISTEN_SIGNAL_STRENGTHS | PhoneStateListener.LISTEN_SERVICE_STATE
				| PhoneStateListener.LISTEN_CALL_STATE | PhoneStateListener.LISTEN_DATA_ACTIVITY | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
		StepCount stepCount = new StepCount(context);
		manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		manager.registerListener(stepCount.sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public void onDestroy() {
		if (mTimeBroadcastReceiver != null && mContext != null) {
			mContext.unregisterReceiver(mTimeBroadcastReceiver);
		}

		if (mBatteryReceiver != null && mContext != null) {
			mContext.unregisterReceiver(mBatteryReceiver);
		}

		if (mWifiStateReceiver != null && mContext != null) {
			mContext.unregisterReceiver(mWifiStateReceiver);
		}

		mHandler.removeMessages(MESSAGE_BATTERY);
		mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		mHandler.removeMessages(MESSAGE_WIFI_CHECK);
		
	}

	public class WifiStateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			int rssi = mWifiManager.getConnectionInfo().getRssi();
			int level = Math.abs(rssi);
			Log.i(TAG, "getRssi:" + rssi);
			mImageWifiSignal.setImageResource(R.drawable.wifi_selector);
			mImageWifiSignal.setImageLevel(level);

		}
	}


	PhoneStateListener mPhoneStateListener = new PhoneStateListener() {

		private int oldLevel = 0;
		private boolean isSimAvail=true;
		@Override
		public void onServiceStateChanged(ServiceState serviceState) {
			super.onServiceStateChanged(serviceState);

			int mCurrentDataNetWorkType = mTelephonyManager.getVoiceNetworkType();
			switch (mCurrentDataNetWorkType) {
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
			case TelephonyManager.NETWORK_TYPE_EHRPD:
			case TelephonyManager.NETWORK_TYPE_UMTS:
				// 3G
				//break;
			case TelephonyManager.NETWORK_TYPE_HSDPA:
			case TelephonyManager.NETWORK_TYPE_HSUPA:
			case TelephonyManager.NETWORK_TYPE_HSPA:
			case TelephonyManager.NETWORK_TYPE_HSPAP:
				// h+
				//break;
			case TelephonyManager.NETWORK_TYPE_EDGE:
				// 2g
				mImageMoblieLable.setImageResource(R.drawable.sim_e);
				break;
			case TelephonyManager.NETWORK_TYPE_LTE:
				mImageMoblieLable.setImageResource(R.drawable.sim_4g);
				// 4G
				break;
			}
			switch (serviceState.getState()) {

			case ServiceState.STATE_IN_SERVICE:
				isSimAvail=true;
				mMncNameText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,mContext.getResources().getDimension(R.dimen.main_dialog_mnc_text_size));
				mMncNameText.setText(NetworkUtils.getCallSimNameLabel(mContext, 0));
				mImageMoblieLable.setVisibility(View.VISIBLE);
				break;
			case ServiceState.STATE_EMERGENCY_ONLY:
				isSimAvail=false;
				mImageMobileSignal.setImageLevel(0);
				mMncNameText.setText(R.string.sim_emergency);
				mMncNameText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,mContext.getResources().getDimension(R.dimen.main_dialog_mnc_text_size_small));
				mImageMoblieLable.setVisibility(View.GONE);
				break;
			case ServiceState.STATE_OUT_OF_SERVICE:
				isSimAvail=false;
				mImageMobileSignal.setImageLevel(0);
				mMncNameText.setText(R.string.sim_not_avaliab);
				mMncNameText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,mContext.getResources().getDimension(R.dimen.main_dialog_mnc_text_size_small));
				mImageMoblieLable.setVisibility(View.GONE);
				break;
			case ServiceState.STATE_POWER_OFF:
				isSimAvail=false;
				mImageMobileSignal.setImageLevel(0);
				mMncNameText.setText(R.string.insert_sim_card);
				mMncNameText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,mContext.getResources().getDimension(R.dimen.main_dialog_mnc_text_size_small));
				mImageMoblieLable.setVisibility(View.GONE);
				break;
			}
		}

		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {

			super.onSignalStrengthsChanged(signalStrength);
			if (signalStrength == null) {
				Log.e(TAG, "signalStrength==null");
				return;
			}
			//int level = 0;
			if(isSimAvail){
				NetworkUtils.signalLevel = signalStrength.getLevel();
			}
			Log.d(TAG," oldLevel = " + oldLevel + " level = " + NetworkUtils.signalLevel + " isSimAvail = " + isSimAvail);
			if (oldLevel != NetworkUtils.signalLevel) {
				mImageMobileSignal.setImageLevel(NetworkUtils.signalLevel);
				oldLevel = NetworkUtils.signalLevel;
			}
			if(!isSimAvail){
				mImageMobileSignal.setImageLevel(0);
			}

		}
	};

	public class BatteryReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			refreshBattery(intent);
		}
	}

	void refreshBattery(Intent intent) {

		int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		NetworkUtils.isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
		boolean isFull = status == BatteryManager.BATTERY_STATUS_FULL;
		mBatteryChargeIndex = NetworkUtils.battery = intent.getExtras().getInt(BatteryManager.EXTRA_LEVEL);
		mHandler.removeMessages(MESSAGE_BATTERY);
		mBatteryClipDrawable.setLevel(0);
		mEmptyDrawable.setAlpha(0);
		mWarningDrawable.setAlpha(0);
		if (isFull) {
			mBatteryChargeIndex = 25;
			mBatteryClipDrawable.setLevel(10000);
		} else if (NetworkUtils.isCharging) {
			mHandler.sendEmptyMessage(MESSAGE_BATTERY);
		} else {
			mBatteryChargeIndex = 25;
			if (NetworkUtils.battery > 25) {
				mBatteryClipDrawable.setLevel(NetworkUtils.battery * 100);
			} else if (NetworkUtils.battery <= 25 && NetworkUtils.battery >= 10) {
				mWarningDrawable.setAlpha(0xff);
			} else if (NetworkUtils.battery <= 10) {
				mEmptyDrawable.setAlpha(0xff);
			}
		}
	}

	class TimeBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			refreshTime();
		}
	}

	void refreshTime() {

		if (mContext == null) {
			return;
		}

		int[] indexs = DateUtilsEx.getTimeIndexEx(mContext);
		mImageHourHigh.setImageResource(mTimeImageIndexs[indexs[4]]);
		mImageHourLow.setImageResource(mTimeImageIndexs[indexs[5]]);
		mImageMinuteHigh.setImageResource(mTimeImageIndexs[indexs[6]]);
		mImageMinuteLow.setImageResource(mTimeImageIndexs[indexs[7]]);

		int[] datas = DateUtilsEx.getTimeIndex(mContext);
		String weekText = DateUtils.getDayOfWeekString(datas[1], DateUtils.LENGTH_LONG);
		String dateText = DateUtilsEx.getDate(mContext);
		mDateText.setText(dateText + " " + weekText);
	}

	private Runnable mResetRunnable = new Runnable() {

		@Override
		public void run() {
			mClickIndex = 0;
		}
	};

	@Override
	public void onClick(View v) {
		Intent mIntent=new Intent();
		switch (v.getId()){
			case R.id.id_call_dialer:
				mIntent.setAction("android.intent.action.DIAL");
				break;
			case R.id.id_call_contacts:
				mIntent.setClassName("com.android.contacts", "com.android.contacts.activities.PeopleActivity");
				break;
		}
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try{
			mContext.startActivity(mIntent);
		}catch(Exception e){
			Toast.makeText(mContext, "App not install", Toast.LENGTH_SHORT).show();
		}
		/*switch (v.getId()) {
		case R.id.id_call_mom:
			if (mCurrentId != R.id.id_call_mom) {
				mCurrentId = R.id.id_call_mom;
				mClickIndex = 0;
			}
			break;
		case R.id.id_call_dad:
			if (mCurrentId != R.id.id_call_dad) {
				mCurrentId = R.id.id_call_dad;
				mClickIndex = 0;
			}
			break;
		case R.id.id_call_sos:
			if (mCurrentId != R.id.id_call_sos) {
				mCurrentId = R.id.id_call_sos;
				mClickIndex = 0;
			}
			break;
		}
		mClickIndex++;
		if (mClickIndex == 2) {
			mClickIndex = 0;
			String number = null;
			if (v.getId() == R.id.id_call_mom) {
				number = EditNumberUtils.getInstance(mContext).getNumber(EditNumberUtils.PRE_EDIT_NUMBER_MOM);
				if (number == null || number.equals("")) {
					startEditActivity();
				} else {
					dial(number);
				}
			} else if (v.getId() == R.id.id_call_dad) {
				number = EditNumberUtils.getInstance(mContext).getNumber(EditNumberUtils.PRE_EDIT_NUMBER_DAD);
				if (number == null || number.equals("")) {
					startEditActivity();
				} else {
					dial(number);
				}
			} else if (v.getId() == R.id.id_call_sos) {
				number = EditNumberUtils.getInstance(mContext).getNumber(EditNumberUtils.PRE_EDIT_NUMBER_SOS);
				if (number == null || number.equals("")) {
					startEditActivity();
				} else {
					dial(number);
				}
			}

		}
		mHandler.removeCallbacks(mResetRunnable);
		mHandler.postDelayed(mResetRunnable, 400);*/

	}

	private void startEditActivity() {
		Intent mIntent = new Intent();
		mIntent.setClass(mContext, EditNumberActivity.class);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mIntent.putExtra("from_launcher",true);
		mContext.startActivity(mIntent);
	}
	private  boolean isUriNumber(String number) {
        return number != null && (number.contains("@") || number.contains("%40"));
    }
	private void dial(String number) {
		Uri mUri = null;
		if (isUriNumber(number)) {
			mUri = Uri.fromParts(SCHEME_SIP, number, null);
		} else {
			mUri = Uri.fromParts(SCHEME_TEL, number, null);
		}
		Intent intent = new Intent(ACTION_CALL_PRIVILEGED, mUri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (PhoneNumberUtils.isEmergencyNumber(number)) {
			intent.putExtra(NOT_NEED_SIMCARD_SELECTION, true);
			intent.putExtra(EXTRA_PHONE_ID, 0);
			mContext.startActivity(intent);
		}else{
			mContext.startActivity(intent);
		}
	}
}
