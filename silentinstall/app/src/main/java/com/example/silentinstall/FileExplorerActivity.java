package com.example.silentinstall;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.support.v4.app.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
public class FileExplorerActivity extends Activity implements AdapterView.OnItemClickListener {

	public ListView listview;
	SimpleAdapter simpleadapter;
	String currentpath = Environment.getExternalStorageDirectory().getPath();
	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_explorer);
		listview = (ListView)findViewById(R.id.list_view);
		simpleadapter = new SimpleAdapter(this, list, R.layout.list_item,
				new String[]{"name","img"}, new int[]{R.id.name,R.id.img});
		listview.setAdapter(simpleadapter);
		listview.setOnItemClickListener(this);
		ReflushPath(currentpath);
	}

	private void ReflushPath(String path) {
		// TODO Auto-generated method stub
		setTitle(path);
		File [] files = new File(path).listFiles();
		list.clear();
		if(files != null){
			for(File file : files){
				Map<String, Object> map = new HashMap<String, Object>();
				if(file.isDirectory()){
					map.put("img", R.drawable.directory);
				}else{
					map.put("img", R.drawable.file_doc);
				}
				map.put("name", file.getName());
				map.put("curpath", file.getPath());
				list.add(map);
			}
			
		} else {
			Log.d("pepsl","files is null !!");
		}
		simpleadapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		// TODO Auto-generated method stub
		 currentpath = (String) list.get(position).get("curpath");
		 File file = new File(currentpath);
		 if(file.isDirectory()){
			 ReflushPath(currentpath);
		 }else {
			 Intent intent = new Intent();
			 intent.putExtra("apk_path", file.getPath());
			 setResult(RESULT_OK, intent);
			 finish();
		 }
	}

}
