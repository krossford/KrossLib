package com.krosshuang.krosslib.test.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.krosshuang.krosslib.R;
import com.krosshuang.krosslib.toy.MyListView;

import java.util.ArrayList;

/**
 * Created by krosshuang on 2016/1/22.
 */
public class TestToyMyListViewFragment extends Fragment {

    private MyListView mListView = null;

    private MyToyAdapter mAdapter = null;

    private ArrayList<String> mData = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < 100; i++) {
            mData.add(i+"");
        }

        mAdapter = new MyToyAdapter(getActivity());
        mAdapter.setData(mData);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_test_toy_my_list_view, null);

        NumberPicker np = (NumberPicker) root.findViewById(R.id.number_picker);
        np.setMinValue(0);
        np.setMaxValue(12);
        np.setWrapSelectorWheel(false);

//        mListView = (MyListView) root.findViewById(R.id.my_list_view);
//        mListView.setAdapter(mAdapter);

        return root;
    }

}
