package com.example.perlan.uibestpractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button sendButton;
    private EditText inpuText;
    private ListView msgListView;
    private MsgAdapter adapter;
    private List<Msg> msgList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMsg();
        adapter = new MsgAdapter(this,R.layout.layout,msgList);
        sendButton = (Button) findViewById(R.id.send_text);
        inpuText = (EditText)findViewById(R.id.inut_text);
        msgListView = (ListView)findViewById(R.id.messgae_list_view);
        msgListView.setAdapter(adapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inpuText.getText().toString();
                if(!"".equals(content)){
                    Msg msg = new Msg(content,Msg.TYPE_SEND);
                    msgList.add(msg);
                    adapter.notifyDataSetChanged();
                    msgListView.setSelection(msgList.size());
                    inpuText.setText("");
                }
            }
        });
    }

    private void initMsg() {
        Msg msg1 = new Msg("hello pepsl",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("hello,who is that",Msg.TYPE_SEND);
        msgList.add(msg2);
        Msg msg3 = new Msg("he is momo,Nice talking to you",Msg.TYPE_RECEIVED);
        msgList.add(msg3);
    }
}
