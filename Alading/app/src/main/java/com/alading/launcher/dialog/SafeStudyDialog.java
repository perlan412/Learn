package com.alading.launcher.dialog;

import com.alading.launcher.R;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public class SafeStudyDialog extends BaseDialog implements OnClickListener{

	static final String  TAG = "SafeStudyDialog";
	public SafeStudyDialog(Context context) {
		
		super(context);
		mContentParent = (ViewGroup) View.inflate(mContext, R.layout.dialog_safe_study, null); 
		if(mContentParent != null){
			mContentParent.setOnClickListener(this);
		}
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.item_1:
		{
			Log.d(TAG, "item_1");
			
			final Dialog dialog = new AppMenuDialog(mContext,R.style.Dialog_Fullscreen);  
			ViewGroup viewGroup = (ViewGroup)View.inflate(mContext, R.layout.layout_safe_study, null);
			dialog.setContentView(viewGroup);  
			dialog.findViewById(R.id.id_safe_alading).setOnClickListener(this);
			dialog.findViewById(R.id.id_safe_chat).setOnClickListener(this);
			dialog.findViewById(R.id.id_safe_friends).setOnClickListener(this);
			dialog.findViewById(R.id.id_safe_step_count).setOnClickListener(this);
				
			dialog.show();
			break;
		}
		case R.id.id_safe_alading:
			Toast.makeText(mContext, "id_safe_alading", Toast.LENGTH_SHORT).show();
			break;
		case R.id.id_safe_chat:
			Toast.makeText(mContext, "id_safe_chat", Toast.LENGTH_SHORT).show();
			break;
		case R.id.id_safe_friends:
			Toast.makeText(mContext, "id_safe_friends", Toast.LENGTH_SHORT).show();
			break;
		case R.id.id_safe_step_count:
			Toast.makeText(mContext, "id_safe_step_count", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

}
