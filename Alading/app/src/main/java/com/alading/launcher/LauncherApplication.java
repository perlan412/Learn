package com.alading.launcher;

import android.app.Application;
import android.util.Log;
//import com.taobao.sophix.PatchStatus;
//import com.taobao.sophix.SophixManager;
//import com.taobao.sophix.listener.PatchLoadStatusListener;


public class LauncherApplication extends Application{

	private static final String TAG = "LauncherApplication";

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		super.onCreate();
		//initSophix();
	}

	
	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.d(TAG, "onTerminate");
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Log.d(TAG, "onLowMemory");
	}

	private void initSophix() {
		String appVersion = "1.0.0";
		try {
			appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
			Log.d(TAG,"appversion = " + appVersion);
		} catch (Exception e) {
			Log.d(TAG,"appversion = " + appVersion);
			appVersion = "1.1.1";
		}
		// initialize最好放在attachBaseContext最前面，初始化直接在Application类里面，切勿封装到其他类
//		SophixManager.getInstance().setContext(this)
//				.setAppVersion(appVersion)
//				.setAesKey(null)
//				.setEnableDebug(true)
//				.setPatchLoadStatusStub(new PatchLoadStatusListener() {
//					@Override
//					public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
//						// 补丁加载回调通知
//						if (code == PatchStatus.CODE_LOAD_SUCCESS) {
//							Log.d(TAG,"patch load success");
//							// 表明补丁加载成功
//						} else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
//							Log.d(TAG,"patch need relauncher");
//							// 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
//							// 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
//						} else {
//							Log.d(TAG,"patch load error");
//							// 其它错误信息, 查看PatchStatus类说明
//						}
//					}
//				}).initialize();
//// queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
//		SophixManager.getInstance().queryAndLoadNewPatch();
	}
}
