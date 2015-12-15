package com.krosshuang.krosslib.test.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.krosshuang.krosslib.R;

import java.util.ArrayList;

/**
 * Created by krosshuang on 2015/12/15.
 */
public class TestAdapter extends BaseAdapter {

    private static int mIndex = 0;

    private Context mContext = null;
    private ArrayList<String> mData = new ArrayList<String>();

    public TestAdapter(Context c) {
        mContext = c;

        for (int i = 0; i < 30; i++) {
            mData.add(mIndex++ + "");
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_demo, null);
            holder.text = (TextView)convertView.findViewById(R.id.tv_demo_name);
            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }

        holder.text.setText(mData.get(position));

        return convertView;
    }

    class Holder {
        TextView text;
    }
}
