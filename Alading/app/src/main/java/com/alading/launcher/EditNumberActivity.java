package com.alading.launcher;

import com.alading.launcher.utils.EditNumberUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


public class EditNumberActivity extends Activity implements OnClickListener, OnFocusChangeListener, OnEditorActionListener {

	private static final String TAG="EditNumberActivity";
	
	private RelativeLayout edit_dialog;
	private Button btn_yes;
	private Button btn_no;
	private ScrollView edit_parent;
	private LinearLayout bottom_btn_parent;

	private Button btn_option;
	private Button btn_del;
	private EditText edit_number_1;
	private EditText edit_number_2;
	private EditText edit_number_3;

	private EditNumberUtils mEditNumberUtils;
	private LinearLayout option_view;
	private Button btn_save;
	private Button btn_cancle;

	private String mCurrentNumber1 = "";
	private String mCurrentNumber2 = "";
	private String mCurrentNumber3 = "";

	private boolean from_launcher;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_number);

		Intent i = getIntent();
		if(i != null) {
			from_launcher = i.getBooleanExtra("from_launcher", false);
			Log.d(TAG," from_launcher = " + from_launcher);
		}
		edit_dialog = (RelativeLayout) findViewById(R.id.edit_dialog);
		edit_parent = (ScrollView) findViewById(R.id.edit_parent);
		bottom_btn_parent = (LinearLayout) findViewById(R.id.bottom_btn_parent);
		option_view = (LinearLayout) findViewById(R.id.option_view);

		btn_yes = (Button) findViewById(R.id.btn_yes);
		btn_no = (Button) findViewById(R.id.btn_no);

		btn_option = (Button) findViewById(R.id.btn_option);
		btn_del = (Button) findViewById(R.id.btn_del);

		btn_cancle = (Button) findViewById(R.id.btn_cancle);
		btn_save = (Button) findViewById(R.id.btn_save);

		edit_number_1 = (EditText) findViewById(R.id.edit_number_1);
		edit_number_2 = (EditText) findViewById(R.id.edit_number_2);
		edit_number_3 = (EditText) findViewById(R.id.edit_number_3);

		mEditNumberUtils = EditNumberUtils.getInstance(this);
		String number = mEditNumberUtils.getNumber(EditNumberUtils.PRE_EDIT_NUMBER_MOM);
		if (number != null) {
			mCurrentNumber1 = number;
			edit_number_1.setText(number);
			btn_del.setText(R.string.edit_del);
		}else{
			btn_del.setText(R.string.back);
		}
		number = mEditNumberUtils.getNumber(EditNumberUtils.PRE_EDIT_NUMBER_DAD);
		if (number != null) {
			mCurrentNumber2 = number;
			edit_number_2.setText(number);
		}

		number = mEditNumberUtils.getNumber(EditNumberUtils.PRE_EDIT_NUMBER_SOS);
		if (number != null) {
			mCurrentNumber3 = number;
			edit_number_3.setText(number);
		}

		edit_number_1.requestFocus();
		btn_yes.setOnClickListener(this);
		btn_no.setOnClickListener(this);

		btn_del.setOnClickListener(this);
		btn_option.setOnClickListener(this);

		btn_cancle.setOnClickListener(this);
		btn_save.setOnClickListener(this);

		edit_number_1.setOnFocusChangeListener(this);
		edit_number_2.setOnFocusChangeListener(this);
		edit_number_3.setOnFocusChangeListener(this);
		
		edit_number_1.setOnEditorActionListener(this);
		edit_number_2.setOnEditorActionListener(this);
		edit_number_3.setOnEditorActionListener(this);
		if(!from_launcher){
			edit_dialog.setVisibility(View.GONE);
			edit_parent.setVisibility(View.VISIBLE);
			bottom_btn_parent.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_yes:
			edit_dialog.setVisibility(View.GONE);
			edit_parent.setVisibility(View.VISIBLE);
			bottom_btn_parent.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_no:
			finish();
			break;
		case R.id.btn_option:
			option_view.setVisibility(View.VISIBLE);
			btn_del.setText(R.string.back);
			btn_option.setVisibility(View.GONE);
			break;
		case R.id.btn_cancle:
			btn_del.setText(R.string.edit_del);
			option_view.setVisibility(View.GONE);
			btn_option.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_save: {
			String number = edit_number_1.getText().toString();
			if (number != null && !number.equals(mCurrentNumber1)) {
				mEditNumberUtils.setNumber(EditNumberUtils.PRE_EDIT_NUMBER_MOM, number);
			}
			number = edit_number_2.getText().toString();
			if (number != null && !number.equals(mCurrentNumber2)) {
				mEditNumberUtils.setNumber(EditNumberUtils.PRE_EDIT_NUMBER_DAD, number);
			}
			number = edit_number_3.getText().toString();
			if (number != null && !number.equals(mCurrentNumber3)) {
				mEditNumberUtils.setNumber(EditNumberUtils.PRE_EDIT_NUMBER_SOS, number);
			}
			finish();
		}
			break;
		case R.id.btn_del: {
			if (option_view.getVisibility() == View.VISIBLE) {
				btn_del.setText(R.string.edit_del);
				option_view.setVisibility(View.GONE);
				btn_option.setVisibility(View.VISIBLE);
			} else {
				if (edit_number_1.isFocused()) {
					updateEditView(edit_number_1);
				} else if (edit_number_2.isFocused()) {
					updateEditView(edit_number_2);
				} else if (edit_number_3.isFocusable()) {
					updateEditView(edit_number_3);
				}
			}
		}
			break;

		}
	}

	private void updateEditView(EditText editText) {
		String number = editText.getText().toString();
		if (number != null && number.length() > 1) {
			number = number.substring(0, number.length() - 1);
			editText.setText(number);
		} else if (number != null && number.length() == 1) {
			editText.setText("");
			btn_del.setText(R.string.back);
		} else {
			finish();
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			EditText editText=(EditText)v;
			String number=editText.getText().toString();
			if(number!=null && number.length()>0){
				btn_del.setText(R.string.edit_del);
			}else{
				btn_del.setText(R.string.back);
			}
		}
		
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		String number=v.getText().toString();
		if(number!=null && number.length()!=0){
			btn_del.setText(R.string.edit_del);
		}else{
			btn_del.setText(R.string.back);
		}
		return false;
	}
}
