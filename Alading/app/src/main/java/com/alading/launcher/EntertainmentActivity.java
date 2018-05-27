package com.alading.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class EntertainmentActivity extends AppBaseActivity implements OnClickListener{
	
	static final String  TAG = "EntertainmentActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	void initView(){
		setContentView(R.layout.layout_entertainment);
		findViewById(R.id.id_ente_camera).setOnClickListener(this);
		findViewById(R.id.id_ente_music).setOnClickListener(this);
		//findViewById(R.id.id_ente_weixin).setOnClickListener(this);
		//findViewById(R.id.id_ente_qq).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent mIntent =new Intent();
		switch(v.getId()){
		case R.id.id_ente_camera:
			//Toast.makeText(EntertainmentActivity.this, "id_ente_camera", Toast.LENGTH_SHORT).show();
			mIntent.setClassName("com.android.camera2", "com.android.camera.CameraActivity");
			break;
		case R.id.id_ente_music:
			//Toast.makeText(EntertainmentActivity.this, "id_ente_music", Toast.LENGTH_SHORT).show();
			mIntent.setClassName("com.toycloud.cmccwatchsdk", "com.toycloud.cmccwatchsdk.demo.SDKTestListActivity");
			break;
		default:
			break;
		}
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try{
			startActivity(mIntent);
		}catch(Exception e){
			Toast.makeText(this, "App not install", Toast.LENGTH_SHORT).show();
		}
	}


	
	
}
