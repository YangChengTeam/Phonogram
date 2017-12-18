package com.yc.phonogram.ui.fragments;

import com.yc.phonogram.R;
import com.yc.phonogram.ui.views.MainBgView;

/**
 * Created by zhangkai on 2017/12/15.
 */

public class IndexFragment extends BaseFragment {
    private MainBgView mainBgView;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    public void init() {
        mainBgView = (MainBgView) getView(R.id.mainBgView);
        mainBgView.showInnerBg();
    }

    @Override
    public void loadData() {

    }
}
