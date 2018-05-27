package com.alading.launcher.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.BaseColumns;
import android.provider.CallLog;
import android.util.Log;

/**
 *
 * @author : zpa
 *
 * @version : 1.0.0
 *
 *          2017年12月12日 下午3:17:45
 *
 */
public class ReadSqlUtils {

	private static final String TAG = "ReadSqlUtils";
	private static ReadSqlUtils mReadSqlUtils;
	public static final Uri CALLS_CONTENT_URI = CallLog.Calls.CONTENT_URI;
	private static final String MISSED_CALLS_SELECTION = CallLog.Calls.TYPE + " = " + CallLog.Calls.MISSED_TYPE
			+ " AND " + CallLog.Calls.NEW + " = 1";

	private static final Uri SMS_CONTENT_URI = Uri.parse("content://sms");
	private static final Uri MMS_CONTENT_URI = Uri.parse("content://mms");
	public static final Uri MMSSMS_CONTENT_URI = Uri.parse("content://mms-sms");
	
	private Context mContext;
	private UnreadMessageContentObserver mUnreadMessage;
    private MissCallContentObserver mMissCall;
    
    private ReadCallBack mReadCallBack;
    private Handler mHandler=new Handler();
	public static ReadSqlUtils getInstance(Context context) {
		if (mReadSqlUtils == null) {
			mReadSqlUtils = new ReadSqlUtils(context);
		}
		return mReadSqlUtils;
	}

	private ReadSqlUtils(Context context) {
		mContext = context;
		mUnreadMessage = new UnreadMessageContentObserver(mHandler);
		mMissCall = new MissCallContentObserver(mHandler);
		mContext.getContentResolver().registerContentObserver(MMSSMS_CONTENT_URI, true, mUnreadMessage);
		mContext.getContentResolver().registerContentObserver(CALLS_CONTENT_URI, true, mMissCall);
	}
	public void setCallBack(ReadCallBack callBack){
		mReadCallBack=callBack;
	}

	
	public void onDestory(){
		mContext.getContentResolver().unregisterContentObserver(mUnreadMessage);
		mContext.getContentResolver().unregisterContentObserver(mMissCall);
	}
	
	public interface ReadCallBack{
		public void unReadCallsCallBack(int count);
		public void unReadMessageCallBack(int count);
	}
	
	private class MissCallContentObserver extends ContentObserver {
		public MissCallContentObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			int count = getUnReadCallsCount();
			if(mReadCallBack!=null){
				mReadCallBack.unReadCallsCallBack(count);
			}
		}

	}

	private class UnreadMessageContentObserver extends ContentObserver {
		public UnreadMessageContentObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			int count = getUnReadMmsCount();
			if(mReadCallBack!=null){
				mReadCallBack.unReadMessageCallBack(count);
			}
		}

	}

	public int getUnReadCallsCount() {
		long startTime = System.currentTimeMillis();
		int missedCalls = 0;
		ContentResolver resolver = mContext.getContentResolver();
		final Cursor cursor = resolver.query(CALLS_CONTENT_URI, new String[] { BaseColumns._ID },
				MISSED_CALLS_SELECTION, null, null);
		if (cursor != null) {
			try {
				missedCalls = cursor.getCount();
				Log.i(TAG, "Missed Call count = " + missedCalls);
			} finally {
				cursor.close();
			}
		}
		long endTime = System.currentTimeMillis();
		Log.i(TAG, "get miss call need " + (endTime - startTime) / 1000);
		return missedCalls;
	}

	public int getUnReadMmsCount() {
		long startTime = System.currentTimeMillis();

		int unreadSms = 0;
		int unreadMms = 0;

		ContentResolver resolver = mContext.getContentResolver();

		// get Unread SMS count
		final Cursor smsCursor = resolver.query(SMS_CONTENT_URI, new String[] { BaseColumns._ID },
				"type =1 AND read = 0", null, null);
		if (smsCursor != null) {
			try {
				unreadSms = smsCursor.getCount();
				Log.i(TAG, "SMS count = " + unreadSms);
			} finally {
				smsCursor.close();
			}
		}
		final Cursor mmsCursor = resolver.query(MMS_CONTENT_URI, new String[] { BaseColumns._ID },
				"msg_box = 1 AND read = 0 AND ( m_type =130 OR m_type = 132 ) AND thread_id > 0", null, null);
		if (mmsCursor != null) {
			try {
				unreadMms = mmsCursor.getCount();
				Log.i(TAG, "MMS count = " + unreadMms);
			} finally {
				mmsCursor.close();
			}
		}

		long endTime = System.currentTimeMillis();
		Log.i(TAG, "get unread message need " + (endTime - startTime) / 1000);
		return unreadMms + unreadSms;
	}
}
