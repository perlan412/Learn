package com.example.perlan.fragmenttest;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;

public class MainActivity extends Activity {

    private Button bt;
    private TextView another;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        another = (TextView)findViewById(R.id.another_fragment);
        bt = (Button)findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Class clz = Class.forName("com.example.perlan.fragmenttest.AnotherFragment");
                    Field[] fields = clz.getDeclaredFields();
                    for(Field field:fields){
                        Log.d("pepsl","field.getModifiers() = " + field.getModifiers() + " " + " field.getType().getName() = " + field.getType().getName() +
                        " field.getName() = " + field.getName() + " clz.getName = " + clz.getName());
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                AnotherFragment anotherFragment = new AnotherFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.right_lyout,anotherFragment);
                //返回到上一个fragment
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

}
