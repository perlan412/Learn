package com.alading.launcher.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class EditNumberUtils {

	private SharedPreferences mPreferences;
	private static final String PRE_EDIT_NUMBER_NAME="edit.number";
	public static final String PRE_EDIT_NUMBER_MOM="mom_number";
	public static final String PRE_EDIT_NUMBER_DAD="dad_number";
	public static final String PRE_EDIT_NUMBER_SOS="sos_number";
	
	private static EditNumberUtils mEditNumberUtils;
	public static EditNumberUtils getInstance(Context context){
		if(mEditNumberUtils==null){
			mEditNumberUtils=new EditNumberUtils(context);
		}
		return mEditNumberUtils;
	}
	private EditNumberUtils(Context context){
		mPreferences=context.getSharedPreferences(PRE_EDIT_NUMBER_NAME, Context.MODE_PRIVATE);
	}
	
	public String getNumber(String key){
		return mPreferences.getString(key, null);
	}
	public void setNumber(String key,String number){
		mPreferences.edit().putString(key, number).commit();
	}
}
