package com.krosshuang.myphotoshop.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.krosshuang.myphotoshop.R;

/**
 * Created by krosshuang on 2015/11/5.
 */
public class MenuDrawerListAdapter extends BaseAdapter {

    private Context mContext = null;
    private String[] mData = null;

    public MenuDrawerListAdapter(Context context, String[] list) {
        mContext = context;
        mData = list;
    }

    @Override
    public int getCount() {
        return mData.length;
    }

    @Override
    public Object getItem(int position) {
        return mData[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_left_drawer, null);
            holder.menuName = (TextView)convertView.findViewById(R.id.menu_name);
            convertView.setTag(convertView);
        } else {
            holder = (Holder)convertView.getTag();
        }

        holder.menuName.setText(mData[position]);
        return convertView;
    }

    class Holder {
        public TextView menuName;
    }
}
