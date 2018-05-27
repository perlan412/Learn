package com.alading.launcher;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.alading.launcher.fragment.WechatContactsFragment;
import com.alading.launcher.fragment.WechatListFragment;

//import com.ashokvarma.bottomnavigation.BottomNavigationBar;
//import com.ashokvarma.bottomnavigation.BottomNavigationItem;
/**
 * Created by chongming on 18-4-2.
 */

public class WechatMainActivity extends Activity implements View.OnClickListener{

    private View view;

    //Fragment
    private WechatListFragment fragment_first;
    private WechatContactsFragment fragment_second;

    //底端菜单栏LinearLayout
    private LinearLayout linear_first;
    private LinearLayout linear_second;

    //底端菜单栏Imageview
    private ImageView iv_first;
    private ImageView iv_second;

    //底端菜单栏textView
    private TextView tv_first;
    private TextView tv_second;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wechat_main);
        //初始化各个控件
        InitView();

        //初始化点击触发事件
        InitEvent();

        //初始化Fragment
        InitFragment(1);

        //将第一个图标设置为选中状态
        iv_first.setImageResource(R.drawable.wechat_press_ic);
        tv_first.setTextColor(getResources().getColor(R.color.colorTextViewPress));
    }

    private void InitView(){

        linear_first = (LinearLayout) findViewById(R.id.line1);
        linear_second = (LinearLayout) findViewById(R.id.line2);

        iv_first = (ImageView) findViewById(R.id.ic_1);
        iv_second = (ImageView) findViewById(R.id.ic_2);

        tv_first = (TextView) findViewById(R.id.textview_1);
        tv_second = (TextView) findViewById(R.id.textview_2);

    }

    private void InitFragment(int index){
        //v4包下的Fragment，使用getSupportFragmentManager获取
        FragmentManager fragmentManager = getFragmentManager();
        //启动事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //将所有的Fragment隐藏
        hideAllFragment(transaction);
        switch (index){
            case 1:
                if (fragment_first== null){
                    fragment_first = new WechatListFragment();
                    transaction.add(R.id.frame_content,fragment_first);
                }
                else{
                    transaction.show(fragment_first);
                }
                break;

            case 2:
                if (fragment_second== null){
                    fragment_second = new WechatContactsFragment();
                    transaction.add(R.id.frame_content,fragment_second);
                }
                else{
                    transaction.show(fragment_second);
                }
                break;
        }
        //提交事务
        transaction.commit();
    }
    private void InitEvent(){
        //设置LinearLayout监听
        linear_first.setOnClickListener(this);
        linear_second.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        //每次点击之后，将所有的ImageView和TextView设置为未选中
        restartButton();
        //再根据所选，进行跳转页面，并将ImageView和TextView设置为选中
        switch(view.getId()){
            case R.id.line1:
                iv_first.setImageResource(R.drawable.wechat_press_ic);
                tv_first.setTextColor(getResources().getColor(R.color.colorTextViewPress));
                InitFragment(1);
                break;

            case R.id.line2:
                iv_second.setImageResource(R.drawable.wechat_contacts_press_ic);
                tv_second.setTextColor(getResources().getColor(R.color.colorTextViewPress));
                InitFragment(2);
                break;
        }
    }

    //隐藏所有的Fragment
    private void hideAllFragment(FragmentTransaction transaction){
        if (fragment_first!= null){
            transaction.hide(fragment_first);
        }

        if (fragment_second!= null){
            transaction.hide(fragment_second);
        }

        // transaction.commit();
    }

    //重新设置ImageView和TextView的状态
    private void restartButton(){
        //设置为未点击状态
        iv_first.setImageResource(R.drawable.wechat_ic);
        iv_second.setImageResource(R.drawable.wechat_contacts_ic);

        //设置为灰色
        tv_first.setTextColor(getResources().getColor(R.color.colorTextViewNormal));
        tv_second.setTextColor(getResources().getColor(R.color.colorTextViewNormal));
    }
}
