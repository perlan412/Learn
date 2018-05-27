package com.alading.launcher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alading.launcher.utils.ContactsUtils;
import com.alading.launcher.utils.NetworkUtils;
import com.alading.launcher.utils.StepCount;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chongming on 18-3-23.
 */

public class ErweimaActivity extends Activity{
    private final String TAG = "ErweimaActivity";
    public ImageView imageView;
    private static final String XPUSH_APPID = "59a92a75";

    private ListView functionListView = null;

//    private CmccWatchSDK cmccWatchSDK = null;
    private String pushId = "";
    /**测试程序的guid**/
    private String guid = "4b3169f3ff0a45e8b27dd2dfd7eb2a68";

    //注册的对象回调
//    private RegisterDeviceModel registerDeviceModel;
    //维持的是微聊上传小文件的im token
    private String imToken;

    private String registerContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erweima);
        imageView = (ImageView) findViewById(R.id.image_view);
        creatErweima();
    }
    public void delayDo(){
        new Handler().postDelayed(new Runnable(){
            public void run() {
                //execute the task
                creatErweima();
            }
        }, 5000);
    }

    public void creatErweima(){
        Bitmap bm = generateBitmap(guid,240,240);
        if(bm != null){
           imageView.setImageBitmap(bm);
        }
    }
    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
