package com.alading.launcher;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * @author：licheng@uzoo.com
 */

public class BottomLayout extends LinearLayout implements View.OnClickListener {

    private final String TAG = "BottomLayout";
    private View bottomLayout;//底部整体布局
    private RelativeLayout rel_input, rel_asr;
    private EditText ed_input;
    private Button btn_send, btn_start_asr;
    private Button btn_select_input, btn_send_tts;
    private Context context;


    public BottomLayout(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    public BottomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
    }

    public BottomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
    }

    private void initView(Context context) {
        bottomLayout = LayoutInflater.from(context).inflate(R.layout.base_bottom, null);

        //asr的整体
        rel_asr = (RelativeLayout) bottomLayout.findViewById(R.id.btn_select_asr);
        btn_start_asr = (Button) bottomLayout.findViewById(R.id.ed_input);
        btn_send = (Button) bottomLayout.findViewById(R.id.btn_send);
        btn_start_asr.setOnClickListener(this);
        addView(bottomLayout);
    }

    private void hiddenKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(ed_input, InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(ed_input.getWindowToken(), 0); //强制隐藏键盘
    }

    /**
     * 设置asr按钮的文本
     **/
    public void setTextBtnASR(String str) {
        btn_start_asr.setText(str);
    }

    /**
     * 获得asr按钮的文本
     **/
    public String getTextBtnASR() {
        return btn_start_asr.getText().toString();
    }

    /**
     * 清空edit里面的数据
     **/
    public void clearInputDate() {
        ed_input.setText("");
    }

    @Override
    public void onClick(View view) {

    }
}
