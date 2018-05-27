package com.alading.launcher.dialog;

import com.alading.launcher.R;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.ViewGroup;

public class DeviceStateDialog extends BaseDialog implements OnClickListener{

	static final String  TAG = "DeviceStateDialog";
	public static final String EDIT_ID_KEY = "currentId";
	private ImageView wifi_settings,dial_settings,sound_settings,settings;
	private int ringerMode;

	public DeviceStateDialog(Context context) {
		
		super(context);
		mContentParent = (ViewGroup) View.inflate(mContext, R.layout.dialog_device_state, null); 
		if(mContentParent != null){
			mContentParent.setOnClickListener(this);
		}
		
		init();
	}
	private void init(){
		wifi_settings=(ImageView) mContentParent.findViewById(R.id.wifi_settings);
		dial_settings=(ImageView) mContentParent.findViewById(R.id.dial_settings);
		sound_settings=(ImageView) mContentParent.findViewById(R.id.sound_settings);
		settings=(ImageView) mContentParent.findViewById(R.id.settings);
		
		wifi_settings.setOnClickListener(this);
		dial_settings.setOnClickListener(this);
		sound_settings.setOnClickListener(this);
		settings.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent mIntent=new Intent();
		
		switch(v.getId()){
		case R.id.item_1:
			Log.d(TAG, "item_1");
			break;
		case R.id.wifi_settings:
			mIntent.setAction("android.settings.WIFI_SETTINGS");
			break;
		case R.id.dial_settings:
			mIntent.setAction("android.intent.action.DIAL");
			break;
		case R.id.sound_settings:
//			mIntent.setClassName("com.sprd.audioprofile", "com.sprd.audioprofile.AudioProfileSettings");
			ringerMode = Settings.System.getInt(mContext.getContentResolver(), "currentAudioProfileId", 1);
			Log.d("pepsl"," ringerMode = " + ringerMode);
			Intent intent = new Intent("com.sprd.action.AUDIO_PROFILE_SOUND_SETTINGS");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra(EDIT_ID_KEY, ringerMode);
			mContext.startActivity(intent);
			break;
		case R.id.settings:
			mIntent.setClassName("com.android.settings", "com.android.settings.Settings");
			break;
		default:
			break;
		}
		if(v.getId() != R.id.sound_settings) {
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			try {
				mContext.startActivity(mIntent);
			} catch (Exception e) {
				Toast.makeText(mContext, R.string.app_not_install, Toast.LENGTH_SHORT).show();
			}
		}
	}

}
