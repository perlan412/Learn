package com.example.perlan.uibestpractice;

/**
 * Created by perlan on 16-9-3.
 */
public class Msg {
    public static final int TYPE_RECEIVED = 1;
    public static final int TYPE_SEND = 0;
    private String content;
    private int type;

    public  Msg(String content,int type){
        this.content = content;
        this.type = type;
    }

    public String getContent(){
        return content;
    }

    public int getType(){
        return type;
    }
}
