/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alading.launcher.utils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.format.DateFormat;

public class DateUtilsEx {
	
	public static String TAG = "Utils";
    
	/**
	 * return yyyy-mm-dd
	 * @return
	 */
	public static String getDate(Context context){  
       //Time localTime = new Time("Asia/Hong_Kong");  
       //localTime.setToNow();  
        
        return DateFormat.getDateFormat(context).format(new Date());  
    }  
	
	
	/**
	 * return HH:MM 
	 * 
	 * @return
	 */
	public static String getTime(Context context) {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		int ampm = calendar.get(Calendar.AM_PM);
		if (DateFormat.is24HourFormat(context)) {
			if (ampm == 1) {
				hour += 12;
			}
		} else {
			if (hour == 0) {
				hour = 12;
			}
		}
		return hour + ":" + ((minute < 10) ? "0" + minute : minute);
	}

	public static int[] getTimeIndex(Context context) {
		int[] timeIndex = new int[8];
		Calendar calendar = Calendar.getInstance();

		int month = calendar.get(Calendar.MONTH);
		int date = calendar.get(Calendar.DATE);
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		int ampm = calendar.get(Calendar.AM_PM);

		if (DateFormat.is24HourFormat(context)) {
			if (ampm == 1) {
				hour += 12;
			}
		} else {
			if (hour == 0) {
				hour = 12;
			}
		}
		timeIndex[0] = month;
		timeIndex[1] = week;

		timeIndex[2] = date / 10;
		timeIndex[3] = date % 10;

		timeIndex[4] = hour / 10;
		timeIndex[5] = hour % 10;

		timeIndex[6] = minute / 10;
		timeIndex[7] = minute % 10;

		return timeIndex;
	}
	
	public static int[] getTimeIndexEx(Context context) {
		
		int[] timeIndex = new int[13];
		Calendar calendar = Calendar.getInstance();

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int date = calendar.get(Calendar.DATE);
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		int ampm = calendar.get(Calendar.AM_PM);

		if (DateFormat.is24HourFormat(context)) {
			if (ampm == 1) {
				hour += 12;
			}
		} else {
			if (hour == 0) {
				hour = 12;
			}
		}
		timeIndex[0] = month / 10;
		timeIndex[1] = month % 10;

		timeIndex[2] = date / 10;
		timeIndex[3] = date % 10;

		timeIndex[4] = hour / 10;
		timeIndex[5] = hour % 10;

		timeIndex[6] = minute / 10;
		timeIndex[7] = minute % 10;
		
		timeIndex[8]  = year / 1000;
		timeIndex[9]  = year % 1000 / 100;
		timeIndex[10] = year % 100 / 10;
		timeIndex[11] = year % 10;
		timeIndex[12] = week;

		return timeIndex;
	}

	public static String getAMPM() {
		return Calendar.getInstance().get(Calendar.AM_PM) == 0 ? "AM" : "PM";
	}
	
	public static Boolean isAM() {
		return Calendar.getInstance().get(Calendar.AM_PM) == 0 ? true : false;
	}

	public static boolean isFileExist(String filePathName) {
		File file = new File(filePathName);
		return file.exists();
		
	}
    

}
