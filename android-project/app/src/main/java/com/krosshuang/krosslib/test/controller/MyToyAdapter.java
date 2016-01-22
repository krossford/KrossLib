package com.krosshuang.krosslib.test.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.krosshuang.krosslib.R;
import com.krosshuang.krosslib.toy.MyBaseAdapter;

import java.util.ArrayList;

/**
 * Created by krosshuang on 2016/1/22.
 */
public class MyToyAdapter extends MyBaseAdapter {

    private Context mContext = null;
    private ArrayList<String> mData = null;

    public MyToyAdapter(Context context) {
        mContext = context;
    }

    public void setData(ArrayList<String> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public View getView(View convertView, int index) {
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_demo, null);
            vh.content = (TextView) convertView.findViewById(R.id.tv_demo_name);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.content.setText(mData.get(index));
        return convertView;
    }

    @Override
    public int getViewType(int index) {
        return 0;
    }


    private static class ViewHolder {
        TextView content;
    }
}
