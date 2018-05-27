package com.alading.launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

import com.alading.launcher.service.RegisterService;
import com.alading.launcher.utils.ActivitysUtils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

/**
 * Created by chongming on 18-5-22.
 */

public class BootReceiver extends BroadcastReceiver {
    private String TAG = "BootReceiver";
    private boolean hasSimCard;
    private boolean hasSimCardReady;
    private boolean networkIsOk;
    private Timer timer;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            mContext = context;
            if (timer == null) {
                Log.d(TAG,"ACTION_BOOT_COMPLETED");
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message message = Message.obtain();
                        message.what = 0;
                        mHandler.sendEmptyMessage(message.what);
                    }
                }, 500, 3000);
            }
        }
    }

    android.os.Handler mHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    hasSimCard = ActivitysUtils.hasSimCard(mContext);
                    hasSimCardReady = ActivitysUtils.hasSimCardReady(mContext);
                    networkIsOk = ActivitysUtils.hasInternet(mContext);
                    Log.d(TAG," hasSimCard = " + hasSimCard + " hasSimCardReady = " + hasSimCardReady + " networkIsOk = " + networkIsOk);
                    if(hasSimCard && hasSimCardReady && networkIsOk){
                        Intent intent = new Intent(mContext, RegisterService.class);
                        mContext.startService(intent);
                        timer.cancel();
                        Log.d(TAG,"start cmcc watch register service!");
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
