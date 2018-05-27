package com.alading.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity {
    WebView mWebView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mWebView = (WebView)findViewById(R.id.id_webview);
        loadWeb();
    }
    
    public void loadWeb(){
        //String url = "http://www.baidu.com";
        String url = "http://192.168.0.129:8081/download";
        
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.loadUrl(url);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {    
        
        if(keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()){
        	mWebView.goBack();
            return true;
        } 
        return super.onKeyDown(keyCode, event);
    }
    
}
