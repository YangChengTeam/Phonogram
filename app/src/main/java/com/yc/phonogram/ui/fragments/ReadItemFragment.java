package com.yc.phonogram.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yc.phonogram.R;

public class ReadItemFragment extends BaseFragment {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_read_item;
    }

    @Override
    public void init() {

    }

    public static ReadItemFragment newInstance() {
        ReadItemFragment childContentFragment=new ReadItemFragment();
        return childContentFragment;
    }

    /**
     * 取参
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
