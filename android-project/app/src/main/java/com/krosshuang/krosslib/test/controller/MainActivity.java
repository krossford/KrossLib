package com.krosshuang.krosslib.test.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.krosshuang.krosslib.R;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private static final String LOG_TAG = "MainActivity";

    private static final class ViewHolder {
        ListView listView;
    }

    private ViewHolder mViewHolder = new ViewHolder();

    private DemoListAdapter mAdapter = null;

    private String[][] mData = new String[][]{
            {"Bezier Curve", "BezierFragment"},
            {"Test BottomLoadListView", "TestBottomLoadListViewFragment"},
            {"Test Ellipsize", "TestEllipsizeTextViewFragment"},
            {"Test AlarmManager", "TestAlarmManagerFragment"},
            {"Test Toy MyListView", "TestToyMyListViewFragment"},
            {"TestSpanFragment", "TestSpanFragment"},
            {"TestBindingFragment", "TestBindingFragment"},

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new DemoListAdapter();

        mViewHolder.listView = (ListView)findViewById(R.id.lv_list);
        mViewHolder.listView.setAdapter(mAdapter);
        mViewHolder.listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ShowFragmentActivity.start(this, mData[position][1]);
    }

    class DemoListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mData.length;
        }

        @Override
        public Object getItem(int position) {
            return mData[position][0];
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
                convertView = MainActivity.this.getLayoutInflater().inflate(R.layout.item_demo, null);
                holder.demoName = (TextView)convertView.findViewById(R.id.tv_demo_name);
                convertView.setTag(holder);
            } else {
                holder = (Holder)convertView.getTag();
            }

            holder.demoName.setText(mData[position][0]);

            return convertView;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(LOG_TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(LOG_TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "onDestroy");
    }

    class Holder {
        public TextView demoName;
    }
}
