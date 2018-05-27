package com.alading.launcher;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alading.launcher.adapter.ListviewAdapter;


public class CommunicationActivity extends AppBaseActivity implements AdapterView.OnItemClickListener {
	
	static final String  TAG = "CommunicationActivity";
	ListView list;
	ListviewAdapter adapter;
	private String name;
	private String call_type;
	private String phone;
	private String shortName;

	public static final String SCHEME_TEL = "tel";
	public static final String SCHEME_SIP = "sip";
	public static final String EXTRA_PHONE_ID = "phone_id";
	public static final String NOT_NEED_SIMCARD_SELECTION = "NOT_NEED_SIMCARD_SELECTION";
	public static final String ACTION_CALL_PRIVILEGED = "android.intent.action.CALL_PRIVILEGED";
	public static final String ACTION_CALL = "android.intent.action.CALL";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_communication);
		adapter = new ListviewAdapter(this);
		list =(ListView)findViewById(R.id.list_contacts);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this );
		getContacts();
	}

	public void getContacts(){
		Log.d(TAG,"getContacts");
		Uri uri = Uri.parse("content://com.android.contacts/contacts"); // 访问所有联系人
		ContentResolver resolver = this.getContentResolver();
		Cursor cursor = resolver.query(uri, new String[]{"_id"}, null, null, null);
		while(cursor.moveToNext()){
			int contactsId = cursor.getInt(0);
			StringBuilder sb = new StringBuilder("contactsId=");
			sb.append(contactsId);
			uri = Uri.parse("content://com.android.contacts/contacts/" + contactsId + "/data"); //某个联系人下面的所有数据
			Cursor dataCursor = resolver.query(uri, new String[]{"mimetype", "data1", "data2"}, null, null, null);
			while(dataCursor.moveToNext()){
				String data = dataCursor.getString(dataCursor.getColumnIndex("data1"));
				String type = dataCursor.getString(dataCursor.getColumnIndex("mimetype"));
				if("vnd.android.cursor.item/name".equals(type)){    // 如果他的mimetype类型是name
					name = data;
					Log.d(TAG," name = " + name);
				} else if("vnd.android.cursor.item/note".equals(type)){ // 如果他的mimetype类型是note
					call_type = data;
					Log.d(TAG," type = " + call_type);
				} else if("vnd.android.cursor.item/phone_v2".equals(type)){ // 如果他的mimetype类型是phone
					phone = data;
					Log.d(TAG," phone = " + phone);
				} else if("vnd.android.cursor.item/sip_address".equals(type)){
					shortName = data;
					Log.d(TAG," shortName = " + shortName);
				}
			}
			adapter.addDataToAdapter(new ContactInfo(this,name,phone));
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.d(TAG,"onItemClick --- view = " + view + " position = " + position + " id = " + id);
		String phone = ((ContactInfo)parent.getAdapter().getItem(position)).getNumber();
		if(phone != null){
			dial(phone);
		}
	}

	private  boolean isUriNumber(String number) {
		return number != null && (number.contains("@") || number.contains("%40"));
	}

	private void dial(String number) {
		Uri mUri = null;
		if (isUriNumber(number)) {
			mUri = Uri.fromParts(SCHEME_SIP, number, null);
		} else {
			mUri = Uri.fromParts(SCHEME_TEL, number, null);
		}
		Log.d(TAG," mUri = " + mUri);
		Intent intent = new Intent(ACTION_CALL, mUri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (PhoneNumberUtils.isEmergencyNumber(number)) {
			Log.d(TAG,"Emergency ---- " + number);
			intent.putExtra(NOT_NEED_SIMCARD_SELECTION, true);
			intent.putExtra(EXTRA_PHONE_ID, 0);
			startActivity(intent);
		}else{
			startActivity(intent);
		}
	}
}
