package com.yc.phonogram.ui.fragments;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.phonogram.R;
import com.yc.phonogram.helper.UserInfoHelper;
import com.yc.phonogram.ui.activitys.MainActivity;
import com.yc.phonogram.ui.popupwindow.PayPopupWindow;
import com.yc.phonogram.ui.popupwindow.PrivatePolicyWindow;
import com.yc.phonogram.ui.popupwindow.UserInfoWindow;
import com.yc.phonogram.ui.views.BaseUserView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by suns  on 2020/4/3 09:33.
 */
public class PersonCenterFragment extends BaseFragment {

    private BaseUserView userInfoView;
    private BaseUserView vipInfoView;
    private BaseUserView orderInfoView;
    private BaseUserView settingView;
    private BaseUserView privacyView;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_person_center;
    }

    @Override
    public void init() {
        userInfoView = (BaseUserView) getView(R.id.user_info_view);
        vipInfoView = (BaseUserView) getView(R.id.vip_info_view);
        orderInfoView = (BaseUserView) getView(R.id.order_info_view);
        settingView = (BaseUserView) getView(R.id.setting_view);
        privacyView = (BaseUserView) getView(R.id.privacy_view);
        initListener();
    }

    private void initListener() {
        RxView.clicks(userInfoView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (UserInfoHelper.isLogin(getActivity())) {
                    UserInfoWindow userInfoWindow = new UserInfoWindow(getActivity());
                    userInfoWindow.show();
                }
            }
        });
        RxView.clicks(vipInfoView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (UserInfoHelper.isLogin(getActivity())) {
                PayPopupWindow payPopupWindow = new PayPopupWindow(getActivity());
                payPopupWindow.show();
            }

        });
        RxView.clicks(orderInfoView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (UserInfoHelper.isLogin(getActivity())) {
                if (getActivity() != null) {
                    OrderInfoFragment orderInfoFragment = new OrderInfoFragment();
                    ((MainActivity) getActivity()).switchFragment(orderInfoFragment, orderInfoFragment.getClass().getName());
                }
            }
        });
        RxView.clicks(settingView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (getActivity() != null) {
                SettingFragment settingFragment = new SettingFragment();
                ((MainActivity) getActivity()).switchFragment(settingFragment, settingFragment.getClass().getName());
            }
        });
        RxView.clicks(privacyView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
//            String str = PreferenceUtil.getImpl(getActivity()).getString(Config.ADV_INFO, "");
//            final AdvInfo advInfo = JSON.parseObject(str, AdvInfo.class);
//            if (null != advInfo) {
//                Intent intent = new Intent(getActivity(), AdvInfoActivity.class);
//                intent.putExtra("url", advInfo.getUrl());
//                intent.putExtra("title", advInfo.getButton_txt());
//                startActivity(intent);
//            }
            PrivatePolicyWindow privatePolicyWindow = new PrivatePolicyWindow(getActivity());
            privatePolicyWindow.show();

        });
    }
}
