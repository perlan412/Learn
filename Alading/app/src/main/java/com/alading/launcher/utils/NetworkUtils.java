package com.alading.launcher.utils;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;
//import android.sim.Sim;
//import android.sim.SimManager;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    
    
    public static final int SIGNAL_STRENGTH_NONE_OR_UNKNOWN = 0;
    public static final int SIGNAL_STRENGTH_POOR = 1;
    public static final int SIGNAL_STRENGTH_MODERATE = 2;
    public static final int SIGNAL_STRENGTH_GOOD = 3;
    public static final int SIGNAL_STRENGTH_GREAT = 4;

    public static int signalLevel;
    public static int battery;
    public static boolean isCharging;
    public static int steps;
    public static int mileage;
    public static int calorie;
  
    
    /**
     * Get gsm(2G,2G+) as level 0..4
     *
     */
    public static int getGsmLevel(SignalStrength signalStrength) {
        int level;

        // ASU ranges from 0 to 31 - TS 27.007 Sec 8.5
        // asu = 0 (-113dB or less) is very weak
        // signal, its better to show 0 bars to the user in such cases.
        // asu = 99 is a special case, where the signal strength is unknown.
        int asu = signalStrength.getGsmSignalStrength();
        if (asu <= 2 || asu == 99) level = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
        else if (asu >= 12) level = SIGNAL_STRENGTH_GREAT;
        else if (asu >= 8)  level = SIGNAL_STRENGTH_GOOD;
        else if (asu >= 5)  level = SIGNAL_STRENGTH_MODERATE;
        else level = SIGNAL_STRENGTH_POOR;
        return level;
    }
    
    
    /**
     * Get cdma(3G) as level 0..4  3G
     *
     */
    public static int getCdmaLevel(int signalStrength) {
        int level;

        if (signalStrength >= -75) level = SIGNAL_STRENGTH_GREAT;
        else if (signalStrength >= -85) level = SIGNAL_STRENGTH_GOOD;
        else if (signalStrength >= -95) level = SIGNAL_STRENGTH_MODERATE;
        else if (signalStrength >= -100) level = SIGNAL_STRENGTH_POOR;
        else level = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
        
        return level;
        
    }
    
    /**
     * Get cdma(3G) as level 0..4  3G
     *
     */
    public static int getCdmaLevel(SignalStrength signalStrength) {
        final int cdmaDbm = signalStrength.getCdmaDbm();
       // final int cdmaEcio = signalStrength.getCdmaEcio();
        int levelDbm;
       // int levelEcio;


        if (cdmaDbm >= -75) levelDbm = SIGNAL_STRENGTH_GREAT;
        else if (cdmaDbm >= -85) levelDbm = SIGNAL_STRENGTH_GOOD;
        else if (cdmaDbm >= -95) levelDbm = SIGNAL_STRENGTH_MODERATE;
        else if (cdmaDbm >= -100) levelDbm = SIGNAL_STRENGTH_POOR;
        else levelDbm = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;


//        // Ec/Io are in dB*10
//        if (cdmaEcio >= -90) levelEcio = SIGNAL_STRENGTH_GREAT;
//        else if (cdmaEcio >= -110) levelEcio = SIGNAL_STRENGTH_GOOD;
//        else if (cdmaEcio >= -130) levelEcio = SIGNAL_STRENGTH_MODERATE;
//        else if (cdmaEcio >= -150) levelEcio = SIGNAL_STRENGTH_POOR;
//        else levelEcio = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
//
//
//        int level = (levelDbm < levelEcio) ? levelDbm : levelEcio;
//        return level;
        return levelDbm;
    }
    
    /**
     * Get Evdo(3G) as level 0..4 , Evdo is kind of cdma 
     *
     */
    public static int getEvdoLevel(SignalStrength signalStrength) {
        int evdoDbm = signalStrength.getEvdoDbm();
       // int evdoSnr = signalStrength.getEvdoSnr();
        int levelEvdoDbm;
       // int levelEvdoSnr;


        if (evdoDbm >= -65) levelEvdoDbm = SIGNAL_STRENGTH_GREAT;
        else if (evdoDbm >= -75) levelEvdoDbm = SIGNAL_STRENGTH_GOOD;
        else if (evdoDbm >= -90) levelEvdoDbm = SIGNAL_STRENGTH_MODERATE;
        else if (evdoDbm >= -105) levelEvdoDbm = SIGNAL_STRENGTH_POOR;
        else levelEvdoDbm = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;


//        if (evdoSnr >= 7) levelEvdoSnr = SIGNAL_STRENGTH_GREAT;
//        else if (evdoSnr >= 5) levelEvdoSnr = SIGNAL_STRENGTH_GOOD;
//        else if (evdoSnr >= 3) levelEvdoSnr = SIGNAL_STRENGTH_MODERATE;
//        else if (evdoSnr >= 1) levelEvdoSnr = SIGNAL_STRENGTH_POOR;
//        else levelEvdoSnr = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
//
//
//        int level = (levelEvdoDbm < levelEvdoSnr) ? levelEvdoDbm : levelEvdoSnr;
//        return level;
        return levelEvdoDbm;
    }

    public static String getCallSimNameLabel(Context context,int phoneId){
        String sim_name=null;
        try {
            Class<?> SimManager = Class.forName("android.sim.SimManager");

            Method get = SimManager.getMethod("get" , Context.class);

            Object obj = (Object) get.invoke(SimManager , context);

            Method getSimById = SimManager.getMethod("getSimById",Integer.TYPE);

            Object objs = (Object) getSimById.invoke(obj,phoneId);

            Class<?> Sim = Class.forName("android.sim.Sim");

            //Method getColorIndex = Sim.getMethod("getColorIndex");
            Method getName = Sim.getMethod("getName");
            //Method getPhoneId = Sim.getMethod("getPhoneId");
            if(Sim != null){
                sim_name = (String)getName.invoke(objs);
            } else {
                sim_name = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG,"sim_name = " + sim_name);
        return sim_name;
    }

    public static ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }
  
   
    public static boolean isConnected(Context context) {
        NetworkInfo net = getConnectivityManager(context).getActiveNetworkInfo();
        return net != null && net.isConnected();
    }

    public static boolean isConnectedOrConnecting(Context context) {
        NetworkInfo[] nets = getConnectivityManager(context).getAllNetworkInfo();
        if (nets != null) {
            for (NetworkInfo net : nets) {
                if (net.isConnectedOrConnecting()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static NetType getConnectedType(Context context) {
        NetworkInfo net = getConnectivityManager(context).getActiveNetworkInfo();
        if (net != null) {
            switch (net.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    return NetType.WIFI;
                case ConnectivityManager.TYPE_MOBILE:
                    return NetType.MOBILE;
                default:
                    return NetType.OTHER;
            }
        }
        return NetType.NONE;
    }

    
    public static boolean isWifiConnected(Context context) {
        NetworkInfo net = getConnectivityManager(context).getActiveNetworkInfo();
        return net != null && net.getType() == ConnectivityManager.TYPE_WIFI && net.isConnected();
    }

   
    public static boolean isMobileConnected(Context context) {
        NetworkInfo net = getConnectivityManager(context).getActiveNetworkInfo();
        return net != null && net.getType() == ConnectivityManager.TYPE_MOBILE && net.isConnected();
    }

   
    public static boolean isAvailable(Context context) {
        return isWifiAvailable(context) || (isMobileAvailable(context) && isMobileEnabled(context));
    }

   
    public static boolean isWifiAvailable(Context context) {
        NetworkInfo[] nets = getConnectivityManager(context).getAllNetworkInfo();
        if (nets != null) {
            for (NetworkInfo net : nets) {
                if (net.getType() == ConnectivityManager.TYPE_WIFI) {
                    return net.isAvailable();
                }
            }
        }
        return false;
    }

    public static boolean isMobileAvailable(Context context) {
        NetworkInfo[] nets = getConnectivityManager(context).getAllNetworkInfo();
        if (nets != null) {
            for (NetworkInfo net : nets) {
                if (net.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return net.isAvailable();
                }
            }
        }
        return false;
    }

    
    public static boolean isMobileEnabled(Context context) {
        try {
            Method getMobileDataEnabledMethod = ConnectivityManager.class.getDeclaredMethod("getMobileDataEnabled");
            getMobileDataEnabledMethod.setAccessible(true);
            return (Boolean) getMobileDataEnabledMethod.invoke(getConnectivityManager(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return true;
    }

    public static boolean printNetworkInfo(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo in = connectivity.getActiveNetworkInfo();
            Log.i(TAG, "-------------$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$-------------");
            Log.i(TAG, "getActiveNetworkInfo: " + in);
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    // if (info[i].getType() == ConnectivityManager.TYPE_WIFI) {
                    Log.i(TAG, "NetworkInfo[" + i + "]isAvailable : " + info[i].isAvailable());
                    Log.i(TAG, "NetworkInfo[" + i + "]isConnected : " + info[i].isConnected());
                    Log.i(TAG, "NetworkInfo[" + i + "]isConnectedOrConnecting : " + info[i].isConnectedOrConnecting());
                    Log.i(TAG, "NetworkInfo[" + i + "]: " + info[i]);
                    // }
                }
                Log.i(TAG, "\n");
            } else {
                Log.i(TAG, "getAllNetworkInfo is null");
            }
        }
        return false;
    }

   
    public static int getConnectedTypeINT(Context context) {
        NetworkInfo net = getConnectivityManager(context).getActiveNetworkInfo();
        if (net != null) {
            Log.i(TAG, "NetworkInfo: " + net.toString());
            return net.getType();
        }
        return -1;
    }

   
    public static int getTelNetworkTypeINT(Context context) {
        return getTelephonyManager(context).getNetworkType();
    }

   
    public static NetWorkType getNetworkType(Context context) {
        int type = getConnectedTypeINT(context);
        switch (type) {
            case ConnectivityManager.TYPE_WIFI:
                return NetWorkType.WIFI;
            case ConnectivityManager.TYPE_MOBILE:
            case ConnectivityManager.TYPE_MOBILE_DUN:
            case ConnectivityManager.TYPE_MOBILE_HIPRI:
            case ConnectivityManager.TYPE_MOBILE_MMS:
            case ConnectivityManager.TYPE_MOBILE_SUPL:
                int teleType = getTelephonyManager(context).getNetworkType();
                switch (teleType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return NetWorkType.NET_2_G;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return NetWorkType.NET_3_G;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return NetWorkType.NET_4_G;
                    default:
                        return NetWorkType.UN_KNOWN;
                }
            default:
                return NetWorkType.UN_KNOWN;
        }
    }

    public enum NetType {
        NONE(1),
        MOBILE(2),
        WIFI(4),
        OTHER(8);

        public int value;

        NetType(int value) {
            this.value = value;
        }
    }

    public enum NetWorkType {
        UN_KNOWN(-1),
        WIFI(1),
        NET_2_G(2),
        NET_3_G(3),
        NET_4_G(4);

        public int value;

        NetWorkType(int value) {
            this.value = value;
        }
    }

}
