package com.alading.launcher.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.alading.launcher.R;

public class ActivitysUtils {
	private static final String TAG = "CommUtils"; 
	
	
	private static final String PACKAGE_NAME = "";
	private static int registerState;
	
	
	public final static String[] ACTIVITYS = {
		"com.android.internal.app.ResolverActivity",
		"com.google.android.voicesearch.RecognitionActivity",
		"com.google.android.voicesearch.VoiceSearchPreferences",
		"com.google.android.voicesearch.IntentApiActivity"
	};
	
	public static boolean isTopActivity(Context context, String strTag){
		boolean result = false;
		if(context != null){
			ActivityManager actManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo>  tasksInfo = actManager.getRunningTasks(1);
			if(tasksInfo.size() > 0){
				String strTopActivity = tasksInfo.get(0).topActivity.getClassName();
				Log.d(TAG + "_"+ strTag, "Robot top Activity is " + strTopActivity);
				for(String strActivityName : ACTIVITYS){
					if(strActivityName.equals(strTopActivity)){
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}
	
	public static boolean isTopActivity(Context context, String strActivityClassName, String strTag){
		if(context == null) return false;
		if(strActivityClassName != null && !strActivityClassName.equals("")){
			ActivityManager actManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo>  tasksInfo = actManager.getRunningTasks(1);
			if(tasksInfo.size() > 0){
				Log.d(TAG + "_"+ strTag, "Robot top Activity is " + tasksInfo.get(0).topActivity.getClassName());
				if(strActivityClassName.equals(tasksInfo.get(0).topActivity.getClassName())){ 
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isTopAppPackagename(Context context, String[] pkgs){
		if(context != null){
			ActivityManager actManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo>  tasksInfo = actManager.getRunningTasks(1);
			if(tasksInfo.size() > 0){
				String topPkgName = tasksInfo.get(0).topActivity.getPackageName();
				Log.d(TAG, "current top activity pkg name is " + topPkgName + ", activity = " + tasksInfo.get(0).topActivity.getClassName());
				for(String pkg : pkgs){
					if(topPkgName.equals(pkg)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean startActivity(String packageName, String className, Context context){
		Intent intent = new Intent();
		Log.d(TAG, "startActivity className = " + className + ",packageName="+packageName);
		try {
			ComponentName c = new ComponentName(packageName,className);
			
			intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			
			intent.setComponent(c);
			int launchFlags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;
			intent.setFlags(launchFlags);		

			context.startActivity(intent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}
	
	public static boolean startActivityEx(Context packageContext, Class<?> cls){
		Intent intent = new Intent();
		
		try {	
			intent.setClass(packageContext, cls);    
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
			//int launchFlags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;
			//intent.setFlags(launchFlags);
			packageContext.startActivity(intent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}

	public static boolean hasSimCard(Context context){
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		int state = tm.getSimState();
		if(state == TelephonyManager.SIM_STATE_ABSENT){
			return false;
		}
		return true;
	}

	public static boolean hasSimCardReady(Context context){
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		int state = tm.getSimState();
		if(state == TelephonyManager.SIM_STATE_READY){
			return true;
		}
		return false;
	}

	public static boolean hasInternet(Context context) {
		ConnectivityManager mConnectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mTelephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//检查网络连接
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();

		if (info == null || !mConnectivity.getBackgroundDataSetting()) {
			Log.d(TAG,"no network");
			return false;
		}
		return true;
	}
}
