package com.yc.phonogram.ui.fragments;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.phonogram.App;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.LoginDataInfo;
import com.yc.phonogram.ui.activitys.MainActivity;
import com.yc.phonogram.ui.popupwindow.PayPopupWindow;
import com.yc.phonogram.ui.popupwindow.VipPopupWindow;
import com.yc.phonogram.ui.views.MainBgView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/12/15.
 */

public class IndexFragment extends BaseFragment {
    private MainBgView mainBgView;
    private ImageView ivGifShow;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    public void init() {
        mainBgView = (MainBgView) getView(R.id.mainBgView);
        mainBgView.showInnerBg();
        ivGifShow = (ImageView)getView(R.id.iv_gif_show);
        LoginDataInfo loginDataInfo = App.getApp().getLoginDataInfo();
        if (loginDataInfo != null && loginDataInfo.getStatusInfo() != null) {
            ((TextView) getView(R.id.tv_user)).setText("用户ID: SE" + loginDataInfo.getStatusInfo().getUid());
        }
        Glide.with(getActivity()).load(R.mipmap.vip_show).into(ivGifShow);

        RxView.clicks(ivGifShow).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                VipPopupWindow vipPopupWindow = new VipPopupWindow(getActivity());
                vipPopupWindow.show();
            }
        });

    }

    @Override
    public void loadData() {

    }
}
