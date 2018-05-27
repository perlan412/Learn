package com.alading.launcher.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.alading.launcher.CommunicationActivity;
import com.alading.launcher.ErweimaActivity;
import com.alading.launcher.utils.ContactsUtils;
import com.alading.launcher.utils.NetworkUtils;
import com.alading.launcher.utils.StepCount;
import com.google.gson.Gson;
import com.toycloud.cmccwatchsdk.CmccWatchSDK;
import com.toycloud.cmccwatchsdk.api.UploadLocationDataAPI;
import com.toycloud.cmccwatchsdk.constant.Config;
import com.toycloud.cmccwatchsdk.listener.IRequestStateListener;
import com.toycloud.cmccwatchsdk.listener.IXpushCmdListener;
import com.toycloud.cmccwatchsdk.model.BaseModel;
import com.toycloud.cmccwatchsdk.model.ContactItemInfo;
import com.toycloud.cmccwatchsdk.model.ContactsClassfiedInfo;
import com.toycloud.cmccwatchsdk.model.RegisterDeviceModel;
import com.toycloud.cmccwatchsdk.model.WeatherInfo;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RegisterService extends Service implements IXpushCmdListener {
    private String TAG = "RegisterService";

    private static final String XPUSH_APPID = "59a92a75";

    private ListView functionListView = null;

    private CmccWatchSDK cmccWatchSDK = null;
    private String pushId = "";
    /**测试程序的guid**/
    private String guid = "4b3169f3ff0a45e8b27dd2dfd7eb2a68";

    //注册的对象回调
    private RegisterDeviceModel registerDeviceModel;
    //维持的是微聊上传小文件的im token
    private String imToken;

    private String registerContent;
    private Timer timer;
    private int retry = 0;
    private Handler delayHandle;

    private Thread thread1, thread2, thread3, thread4, thread5;
    private boolean gpsLocationResult;
    private boolean LBSLocationResult;

    public RegisterService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = Message.obtain();
                    message.what = 0;
                    mHandler.sendEmptyMessage(message.what);
                }
            }, 500, 5000);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.d(TAG, "--------handleMessage------start initcmccWatchSDK");
                    initcmccWatchSDK();
                    delayDo();
                default:
                    break;
            }
        }
    };

    public void delayDo() {
        delayHandle = new Handler();
        delayHandle.postDelayed(new Runnable() {
            public void run() {
                //execute the task
                RegisterState();
            }
        }, 5000);
    }

    private void initcmccWatchSDK() {
        Log.d(TAG, "start initcmccWatchSDK");
        Config.FACTORY_TYPE = "CMCC";
        Config.PRODUCT_TYPE = "W7";
        cmccWatchSDK = CmccWatchSDK.getInstance(getApplicationContext(), guid, XPUSH_APPID);
        cmccWatchSDK.init(this);
    }

    private void RegisterState() {
        Log.d(TAG, "start RegisterState");
        cmccWatchSDK.registerDevice(getApplicationContext(), pushId, guid, new IRequestStateListener<RegisterDeviceModel>() {
            @Override
            public void notifyRequestSuccess(final RegisterDeviceModel registerDeviceModel) {
                Log.d(TAG, "notifyRequestSuccess!");
                registerContent = new Gson().toJson(registerDeviceModel);
                thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String token = registerDeviceModel.getToken();
                        Log.d(TAG, "token = " + token);
                        if (!TextUtils.isEmpty(token)) {
                            Log.d(TAG, "register successfull!");
                            Log.d(TAG, "注册成功,retry = " + retry);
                            SharedPreferences mSharedPreferences = getSharedPreferences("cmccWatchSDK", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.putInt("registerState", 1);
                            editor.commit();
                            retry = 0;
                            timer.cancel();
                            thread1.interrupt();
                        }
                    }
                });
                thread1.start();
            }

            @Override
            public void notifyRequestFail(String s, String s1) {
                //Toast.makeText(getApplicationContext(), "notifyRequestFail", Toast.LENGTH_LONG).show();
                Log.d(TAG, "notifyRequestFail");
                retry++;
                SharedPreferences mSharedPreferences = getSharedPreferences("cmccWatchSDK", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putInt("registerState", 0);
                editor.commit();
            }

            @Override
            public void notifyTokenInvalid() {
                //Toast.makeText(getApplicationContext(), "notifyTokenInvalid", Toast.LENGTH_LONG).show();
                Log.d(TAG, "notifyTokenInvalid");
                SharedPreferences mSharedPreferences = getSharedPreferences("cmccWatchSDK", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putInt("registerState", 0);
                editor.commit();
            }
        });
    }

    private void uploadBaseInfo() {
        //上传设备端的基本的信息
        if (registerDeviceModel == null || TextUtils.isEmpty(registerDeviceModel.getToken())) {
            Toast.makeText(getApplicationContext(), "请先注册", Toast.LENGTH_LONG).show();
            return;
        }
        //对应的是测试数据
        //真实数据 请设备端根据当前的设备的真实的状态的数据上报
        final int battery = 100;
        int walkNum = 10000;
        int distance = 3000;
        boolean isCharging = true;
        int cal = 200;
        cmccWatchSDK.uploadBaseInfo(getApplicationContext(), battery, walkNum,
                distance, cal, isCharging, new IRequestStateListener<BaseModel>() {
                    @Override
                    public void notifyRequestSuccess(final BaseModel baseModel) {
                        thread2 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //Intent gotoDetailContentIntent = new Intent(SDKTestListActivity.this, DetailContentActivity.class);
                                //gotoDetailContentIntent.putExtra("detail", new Gson().toJson(baseModel));
                                //startActivity(gotoDetailContentIntent);
                                Toast.makeText(getApplicationContext(), "上报设备端基本信息成功", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "deviceBaseInfo = " + new Gson().toJson(baseModel));
                                thread2.interrupt();
                            }
                        });
                        thread2.start();
                    }

                    @Override
                    public void notifyRequestFail(String s, String s1) {

                    }

                    @Override
                    public void notifyTokenInvalid() {

                    }
                });
    }


    private void getWeatherInfo() {
        //获取天气的接口
        //获取手机APP端给用户设置的相关的信息
        if (registerDeviceModel == null || TextUtils.isEmpty(registerDeviceModel.getToken())) {
            Toast.makeText(getApplicationContext(), "请先注册", Toast.LENGTH_LONG).show();
            return;
        }
        cmccWatchSDK.getWeatherInfo(getApplicationContext(), new IRequestStateListener<WeatherInfo>() {
            @Override
            public void notifyRequestSuccess(final WeatherInfo weatherInfo) {
                thread3 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //Intent gotoDetailContentIntent = new Intent(SDKTestListActivity.this, DetailContentActivity.class);
                        //gotoDetailContentIntent.putExtra("detail", new Gson().toJson(weatherInfo));
                        //startActivity(gotoDetailContentIntent);
                        Toast.makeText(getApplicationContext(), "获取天气信息成功", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "weatherInfo = " + new Gson().toJson(weatherInfo));
                        thread3.interrupt();
                    }
                });
                thread3.start();
            }

            @Override
            public void notifyRequestFail(String s, String s1) {

            }

            @Override
            public void notifyTokenInvalid() {

            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void notifyUploadLocation() {
        Log.d(TAG, "notifyUploadLocation");
        //下面仅仅是做测试  使用Android原生的LocationManager来进行定位 获取到回调的Location的对象
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGpsEnabled) {
            Toast.makeText(getApplicationContext(), "开始GPS定位", Toast.LENGTH_LONG).show();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LOCATION_SERVICE, 100, 0,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            //在这个地方将当前的Location作为参数
                            if (location != null) {
                                int gsm = -130;
                                int batteryPower = 100;
                                boolean charging = true;
                                int step = 10000;
                                int mile = 3000;
                                int calorie = 200;
                                String gpsData = UploadLocationDataAPI.createGpsLocationInfo(getApplicationContext(), location, gsm, batteryPower, charging,
                                        step, mile, calorie);
                                Log.d(TAG, "当前的gps的数据 = " + gpsData);
                                cmccWatchSDK.uploadLocationData(getApplicationContext(), gpsData, new IRequestStateListener<BaseModel>() {
                                    @Override
                                    public void notifyRequestSuccess(final BaseModel baseModel) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.d(TAG,"GPS定位成功");
                                                gpsLocationResult = true;
                                            }
                                        });
                                    }

                                    @Override
                                    public void notifyRequestFail(String s, String s1) {
                                        gpsLocationResult = false;
                                    }

                                    @Override
                                    public void notifyTokenInvalid() {
                                        gpsLocationResult = false;
                                    }
                                });
                            }
                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {
                        }

                        @Override
                        public void onProviderEnabled(String s) {
                        }

                        @Override
                        public void onProviderDisabled(String s) {
                        }
                    });
            return;
        } else {
            //打开GPS的操作
        }
        //int gsm, int battery, boolean isCharging, int steps, int mileage, int calorie
        //lbs的数据能直接访问
        if(gpsLocationResult == false){
            int gsm = -130;
            int batteryPower = 100;
            boolean charging = true;
            int step = 10000;
            int mile = 3000;
            int calorie = 200;
            String lbsData = UploadLocationDataAPI.createLBSLocationInfo(getApplicationContext(), gsm, batteryPower, charging,
                    step, mile, calorie);
            cmccWatchSDK.uploadLocationData(getApplicationContext(), lbsData, new IRequestStateListener<BaseModel>() {
                @Override
                public void notifyRequestSuccess(final BaseModel baseModel) {
                    thread4 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG,"上报LBS地理位置成功 ");
                            thread4.interrupt();
                            LBSLocationResult = true;
                        }
                    });
                    thread4.start();
                }

                @Override
                public void notifyRequestFail(String s, String s1) {
                    LBSLocationResult = false;
                }

                @Override
                public void notifyTokenInvalid() {
                    LBSLocationResult = false;
                }
            });}
    }

    @Override
    public void notifyNewMsgComeIn(String s) {
        Log.d(TAG,"notifyNewMsgComeIn");
    }

    @Override
    public void notifySingleListenState() {
        Log.d(TAG,"notifySingleListenState");
    }

    @Override
    public void notifyShutDown() {
        //Toast.makeText(getApplicationContext(), "收到推送，需要关机", Toast.LENGTH_LONG).show();
        Log.d(TAG,"收到推送，需要关机");
        cmccWatchSDK.uploadShutDownEvent(this,
                NetworkUtils.signalLevel,//gsm,
                NetworkUtils.battery,//battery,
                NetworkUtils.isCharging,//isCharging,
                StepCount.CURRENT_SETP,//steps,
                10,//mileage,
                100,//calorie,
                new IRequestStateListener<BaseModel>() {
                    @Override
                    public void notifyRequestSuccess(final BaseModel baseModel) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(getApplicationContext(), "上报关机事件成功", Toast.LENGTH_LONG).show();
                                Log.d(TAG,"上报关机事件成功");
                                shutdown();
                            }
                        }).start();
                    }

                    @Override
                    public void notifyRequestFail(String s, String s1) {
                        Log.e(TAG,"notifyRequestFail");
                    }

                    @Override
                    public void notifyTokenInvalid() {
                        Log.e(TAG,"notifyTokenInvalid");
                    }
                });//listener
    }

    private void shutdown(){
        try {
            //获得ServiceManager类
            Class ServiceManager = Class
                    .forName("android.os.ServiceManager");
            //获得ServiceManager的getService方法
            Method getService = ServiceManager.getMethod("getService", java.lang.String.class);
            //调用getService获取RemoteService
            Object oRemoteService = getService.invoke(null,Context.POWER_SERVICE);
            //获得IPowerManager.Stub类
            Class cStub = Class.forName("android.os.IPowerManager$Stub");
            //获得asInterface方法
            Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
            //调用asInterface方法获取IPowerManager对象
            Object oIPowerManager = asInterface.invoke(null, oRemoteService);
            //获得shutdown()方法
            Method shutdown = oIPowerManager.getClass().getMethod("shutdown", boolean.class, boolean.class);
            //调用shutdown()方法
            shutdown.invoke(oIPowerManager, false, false);
        }catch (Exception e) {
            Log.e(TAG, e.toString(), e);
        }
    }

    @Override
    public void notifyResetFactory() {
        Log.d(TAG,"notifyResetFactory");
    }

    @Override
    public void notifyTaskListChange() {
        Log.d(TAG,"notifyTaskListChange");
    }

    @Override
    public void notifyFriendList(List<ContactItemInfo> list) {
        Log.d(TAG,"notifyFriendList");
    }

    @Override
    public void notifyContactListChange() {
        Log.d(TAG,"notifyContactListChange");
        Toast.makeText(getApplicationContext(), "收到推送，手表的通讯录成员发生了变化", Toast.LENGTH_LONG).show();
        cmccWatchSDK.getContact(getApplicationContext(), new IRequestStateListener<ContactsClassfiedInfo>() {
            @Override
            public void notifyRequestSuccess(final ContactsClassfiedInfo contactsClassfiedInfo) {
                thread5 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String json = new Gson().toJson(contactsClassfiedInfo);
                        Log.d(TAG," contactsinfo = " + json);
                        //删除所有旧联系人
                        ContactsUtils.clearOldContatcs(getApplicationContext());
                        Log.d(TAG," 获取联系人信息成功");
                        //同步所有新联系人
                        ContactsUtils.AddContact(getApplicationContext(),contactsClassfiedInfo);
                        thread5.interrupt();
                    }
                });
                thread5.start();
            }

            @Override
            public void notifyRequestFail(String s, String s1) {
                Log.d(TAG," 获取联系人信息Fail");
            }

            @Override
            public void notifyTokenInvalid() {
                Toast.makeText(getApplicationContext(), "获取联系人信息TokenInvalid", Toast.LENGTH_LONG).show();
                Log.d(TAG,"获取联系人信息TokenInvalid");
            }
        });
    }

    @Override
    public void notifySendSmsToServer(String s, String s1) {
        Log.d(TAG,"notifySendSmsToServer");
    }

    @Override
    public void notifyGetUserInfo() {
        Log.d(TAG,"notifyGetUserInfo");
    }

    @Override
    public void notifyUploadBaseInfo() {
        Log.d(TAG,"notifyUploadBaseInfo");
    }

    @Override
    public void notifyTimingBootAndShutDown(boolean b, boolean b1, long l, long l1) {
        Log.d(TAG,"notifyTimingBootAndShutDown");
    }

    @Override
    public void notifyQuickSwitchChange(int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
        Log.d(TAG,"notifyQuickSwitchChange");
    }

    @Override
    public void notifyExtraQuickSwitchChange(int i, int i1, int i2) {
        Log.d(TAG,"notifyExtraQuickSwitchChange");
    }

    @Override
    public void notifyBindXpushSuccess(String s) {
        pushId =s;
        Toast.makeText(getApplicationContext(), "消息推送初始化成功, pushid = " + s, Toast.LENGTH_LONG).show();
        Log.d(TAG,"消息推送初始化成功,pushid = " + s);
    }

    @Override
    public void notifyUnBindXpushSuccess() {
        Toast.makeText(getApplicationContext(), "notifyUnBindXpushSuccess", Toast.LENGTH_LONG).show();
    }

    @Override
    public void notifyXpushConnectionDisconnect() {
        Log.d(TAG,"notifyXpushConnectionDisconnect");
    }

    @Override
    public void notifyXpushConnectionConnect() {
        Log.d(TAG,"notifyXpushConnectionConnect");
    }

    @Override
    public void notifyVolumeSet(int i) {
        Log.d(TAG,"notifyVolumeSet");
    }

    @Override
    public void notifyLocationParamUpdate(int i, int i1, int i2) {
        Log.d(TAG,"notifyLocationParamUpdate");
    }


}
