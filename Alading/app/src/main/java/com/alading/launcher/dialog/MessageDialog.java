package com.alading.launcher.dialog;

import com.alading.launcher.R;
import com.alading.launcher.utils.DateUtilsEx;
import com.alading.launcher.utils.ReadSqlUtils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MessageDialog extends BaseDialog implements OnClickListener {

	static final String TAG = "MessageDialog";

	private static final String MESSAGE_TAG = "message";
	private static final String CALL_TAG = "call";

	private ImageView notifaction_empty;
	private LinearLayout notifaction_message_parent;
	private Context mContext;
	private ReadSqlUtils mReadSqlUtils;

	public MessageDialog(Context context) {
		super(context);
		mContext = context;

		mContentParent = (ViewGroup) View.inflate(mContext, R.layout.dialog_message, null);
		notifaction_empty = (ImageView) mContentParent.findViewById(R.id.notifaction_empty);
		notifaction_message_parent = (LinearLayout) mContentParent.findViewById(R.id.notifaction_message_parent);

		mReadSqlUtils = ReadSqlUtils.getInstance(mContext);
		int count = mReadSqlUtils.getUnReadCallsCount();
		updateUnReadCallCount(count);
		count = mReadSqlUtils.getUnReadMmsCount();
		updateUnReadMessageCount(count);

	}

	public void updateUnReadMessageCount(int count) {
		if (count != 0) {
			notifaction_empty.setVisibility(View.GONE);
			for (int i = 0; i < notifaction_message_parent.getChildCount(); i++) {
				String tag = (String) notifaction_message_parent.getChildAt(i).getTag();
				if (tag != null && tag.equals(MESSAGE_TAG)) {
					notifaction_message_parent.removeViewAt(i);
				}
			}

			String title = mContext.getResources().getString(R.string.unread_message_title);
			String msg = mContext.getResources().getString(R.string.unread_message, count);
			String time = DateUtilsEx.getTime(mContext);
			int titleColor = mContext.getResources().getColor(R.color.message_txt_weiliao);
			View mView = createItemView(title, msg, time, titleColor, MESSAGE_TAG);
			mView.setOnClickListener(this);
			notifaction_message_parent.addView(mView, createParams());
		} else {
			for (int i = 0; i < notifaction_message_parent.getChildCount(); i++) {
				String tag = (String) notifaction_message_parent.getChildAt(i).getTag();
				if (tag != null && tag.equals(MESSAGE_TAG)) {
					notifaction_message_parent.removeViewAt(i);
				}
			}
			if (notifaction_message_parent.getChildCount() == 0) {
				notifaction_empty.setVisibility(View.VISIBLE);
			}
		}
	}

	public void updateUnReadCallCount(int count) {
		if (count != 0) {
			notifaction_empty.setVisibility(View.GONE);
			for (int i = 0; i < notifaction_message_parent.getChildCount(); i++) {
				String tag = (String) notifaction_message_parent.getChildAt(i).getTag();
				if (tag != null && tag.equals(CALL_TAG)) {
					notifaction_message_parent.removeViewAt(i);
				}
			}
			String title = mContext.getResources().getString(R.string.unread_call_title);
			String msg = mContext.getResources().getString(R.string.unread_call, count);
			String time = DateUtilsEx.getTime(mContext);
			int titleColor = mContext.getResources().getColor(R.color.message_txt_unreadcall);
			View mView = createItemView(title, msg, time, titleColor, CALL_TAG);
			mView.setOnClickListener(this);
			notifaction_message_parent.addView(mView, createParams());
		} else {
			for (int i = 0; i < notifaction_message_parent.getChildCount(); i++) {
				String tag = (String) notifaction_message_parent.getChildAt(i).getTag();
				if (tag != null && tag.equals(CALL_TAG)) {
					notifaction_message_parent.removeViewAt(i);
				}
			}
			if (notifaction_message_parent.getChildCount() == 0) {
				notifaction_empty.setVisibility(View.VISIBLE);
			}
		}
	}

	public View createItemView(String title, String msg, String time, int titleColor, String tag) {
		LayoutInflater mInflater = LayoutInflater.from(mContext);
		LinearLayout mLayout = (LinearLayout) mInflater.inflate(R.layout.message_item, null);
		TextView message_title = (TextView) mLayout.findViewById(R.id.message_title);
		TextView message = (TextView) mLayout.findViewById(R.id.message);
		TextView message_time = (TextView) mLayout.findViewById(R.id.message_time);
		message_title.setText(title);
		message.setText(msg);
		message_time.setText(time);
		message_title.setTextColor(titleColor);
		mLayout.setTag(tag);
		return mLayout;

	}

	public LinearLayout.LayoutParams createParams() {
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		mParams.topMargin = 10;
		return mParams;
	}

	@Override
	public void onClick(View v) {
		String tag = (String) v.getTag();
		if (tag == null) {
			Log.e(TAG, "tag == null");
			return;
		}
		Intent mIntent = new Intent();
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (tag.equals(MESSAGE_TAG)) {
			mIntent.setClassName("com.android.mms", "com.android.mms.ui.ConversationList");
		} else if (tag.equals(CALL_TAG)) {
			mIntent.setClassName("com.android.dialer", "com.android.dialer.calllog.CallLogActivity");
		}
		try {
			mContext.startActivity(mIntent);
		} catch (Exception e) {
			Toast.makeText(mContext, R.string.app_not_install, Toast.LENGTH_SHORT).show();
		}
	}

}
