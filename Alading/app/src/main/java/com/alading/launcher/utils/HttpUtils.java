package com.alading.launcher.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;



public class HttpUtils {

	 /**
	   * 
	   * @param inStream
	   * @return
	   */
	  public static byte[] read(InputStream inStream) throws Exception{
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];
	    int len = 0;
	    while( (len = inStream.read(buffer)) != -1){
	      outputStream.write(buffer, 0, len);
	    }
	    inStream.close();
	    return outputStream.toByteArray();
	  }
	  
	  /**
	   * 
	   * @param url
	   * @return
	   */
	  public static byte[] getImageByte(String path) throws IOException {
	    URL url = new URL(path);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    conn.setReadTimeout(5 * 1000); 
	    InputStream inputStream = conn.getInputStream(); 
	    byte[] data = null;
		try {
			data = read(inputStream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    return data;
	  }

	  /**
	   * 
	   * @param url
	   * @return
	   */
	  public static Bitmap getImageBitmap(String path) throws Exception{
	    URL url = new URL(path);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setConnectTimeout(5000);
	    conn.setRequestMethod("GET");
	    if(conn.getResponseCode() == 200){
	      int length = conn.getContentLength();
	      Log.d("getContentLength", "getContentLength = " + length);
	      InputStream inStream = conn.getInputStream();
	      Bitmap bitmap = BitmapFactory.decodeStream(inStream);
	      return bitmap;
	    }else{
	    	Log.d("getContentLength", "reponse failed");
	    }
	    return null;
	  }
}
