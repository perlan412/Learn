package com.alading.launcher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alading.launcher.ContactInfo;
import com.alading.launcher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chongming on 18-1-11.
 */

public class ListviewAdapter extends BaseAdapter {

    private Context context;
    private List<ContactInfo> datas = new ArrayList<ContactInfo>();

    private ViewHolder viewHolder;
    //给adapter添加数据
    public void addDataToAdapter(ContactInfo e) {
        datas.add(e);
    }

    public ListviewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //使用viewholder来优化listview
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.contacts_item, null);
            viewHolder = new ViewHolder(convertView);
            viewHolder.contact_name.setText(datas.get(position).getName());
            viewHolder.contact_number.setText(datas.get(position).getNumber());

        return convertView;

    }
    public static class ViewHolder {
        public View rootView;
        public TextView contact_name;
        public TextView contact_number;


        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.contact_name = (TextView) rootView.findViewById(R.id.contact_name);
            this.contact_number = (TextView) rootView.findViewById(R.id.contact_number);
        }

    }
}
