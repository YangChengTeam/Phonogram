package com.yc.phonogram.ui.fragments;

import android.widget.TextView;

import com.yc.phonogram.App;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.LoginDataInfo;
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

        LoginDataInfo loginDataInfo = App.getApp().getLoginDataInfo();
        if (loginDataInfo != null && loginDataInfo.getStatusInfo() != null) {
            ((TextView) getView(R.id.tv_user)).setText("用户ID: SE" + loginDataInfo.getStatusInfo().getUid());
        }
    }

    @Override
    public void loadData() {

    }
}
