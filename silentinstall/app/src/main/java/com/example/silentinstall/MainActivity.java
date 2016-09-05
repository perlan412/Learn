package com.example.silentinstall;

import java.io.File;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public TextView apkpath;
	String path;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apkpath = (TextView)findViewById(R.id.apk_path);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0 && resultCode == RESULT_OK){
			path = data.getStringExtra("apk_path");
			apkpath.setText(path);
		}
	}

	public void onChooseApkFile(View view) {
		Intent intent = new Intent(this, FileExplorerActivity.class);
		startActivityForResult(intent, 0);
	}
	
	public void openService(View view){
		Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
		startActivity(intent);
	}
	
	public void onSmartInstall(View view){
		if(TextUtils.isEmpty(path)){
			Toast.makeText(this, "path is null", Toast.LENGTH_LONG).show();
			return;
		}
		Uri uri = Uri.fromFile(new File(path));
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		startActivity(intent);
	}
}
