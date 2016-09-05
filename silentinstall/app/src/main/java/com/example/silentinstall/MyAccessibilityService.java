package com.example.silentinstall;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;

@SuppressLint("NewApi")
public class MyAccessibilityService extends AccessibilityService {

	Map<Integer, Boolean> hasmap = new HashMap<Integer, Boolean>();
	public MyAccessibilityService(){
		
	}
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		// TODO Auto-generated method stub
		AccessibilityNodeInfo nodeinfo = getRootInActiveWindow();//event.getSource();
		Log.d("pepsl","onAccessibilityEvent");
		if(nodeinfo != null){
			int eventType = event.getEventType();
			Log.d("pepsl","eventType = " + eventType);
			if(eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ||
				eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ||
				eventType == AccessibilityEvent.TYPE_WINDOWS_CHANGED){
				if(hasmap.get(event.getWindowId()) == null){
					boolean handle = iterateNodeAndHandle(nodeinfo);
					if(handle){
						hasmap.put(event.getWindowId(), true);
					}
				} else {
					Log.d("pepsl","hasmap.get(event.getWindowId()) != null");
				}
			}
		} else {
			//acm.sendAccessibilityEvent(event);
			Log.d("pepsl","nodeinfo is null!");
		}
	}

	@Override
	protected void onServiceConnected() {
		// TODO Auto-generated method stub
		super.onServiceConnected();
		Log.d("pepsl", "onServiceConnected");
		AccessibilityServiceInfo as = new AccessibilityServiceInfo();
	}
	@SuppressLint({ "NewApi", "NewApi" })
	private boolean iterateNodeAndHandle(AccessibilityNodeInfo nodeinfo) {
		// TODO Auto-generated method stub
		if(nodeinfo != null){
			Log.d("pepsl","nodeinfo.isFocusable = " + nodeinfo.isFocusable() + " nodeinfo.describeContents() = " + nodeinfo.describeContents());
			int childcount = nodeinfo.getChildCount();

			Log.d("pepsl","nodeinfo.getClassName() = " + nodeinfo.getClassName() + " childcount = " + childcount);
			if("android.widget.Button".equals(nodeinfo.getClassName())){
				String nodecontent = nodeinfo.getText().toString();
				Log.d("pepsl","nodecontent = " + nodecontent);
				if("下一步".equals(nodecontent) ||
				   "安装".equals(nodecontent) ||
				   "完成".equals(nodecontent) ||
				   "确定".equals(nodecontent)){
					nodeinfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
					return true;
				}
			} else if("android.widget.ScrollView".equals(nodeinfo.getClassName())){
				Log.d("pepsl","android.widget.ScrollView");
					nodeinfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
			} else if("android.widget.TextView".equals(nodeinfo.getClassName())){
				Log.d("pepsl","android.widget.TextView" + nodeinfo.getText().toString());
			} 
			for(int i = 0; i < childcount; i++){
				if(iterateNodeAndHandle(nodeinfo.getChild(i))){
					Log.d("pepsl"," i = " + i);
					iterateNodeAndHandle(nodeinfo.getChild(i));
					return true;
				}
				
			}
		} else {
		Log.d("pepsl", "nodeinfo is null");
		}
		return false;
	}


	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub
		Log.d("pepsl","onInterrupt");
	}

}
