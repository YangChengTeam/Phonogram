package com.yc.phonogram.ui.fragments;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.yc.phonogram.R;
import com.yc.phonogram.helper.UserInfoHelper;
import com.yc.phonogram.ui.activitys.MainActivity;
import com.yc.phonogram.utils.DataCleanManagerUtils;
import com.yc.phonogram.utils.UIUtils;

import java.util.concurrent.TimeUnit;

import androidx.viewpager.widget.ViewPager;
import rx.functions.Action1;

/**
 * Created by suns  on 2020/4/4 10:09.
 */
public class SettingFragment extends BaseFragment {

    private ImageView ivBack;
    private ImageView ivQuit;
    private RelativeLayout rlVersion;
    private RelativeLayout rlCache;
    private TextView tvCache;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    public void init() {
        ivBack = (ImageView) getView(R.id.iv_back);
        ivQuit = (ImageView) getView(R.id.iv_quit);
        TextView tvVersion = (TextView) getView(R.id.tv_version);
        rlVersion = (RelativeLayout) getView(R.id.rl_version);
        tvCache = (TextView) getView(R.id.tv_cache);
        rlCache = (RelativeLayout) getView(R.id.rl_cache);
        if (TextUtils.isEmpty(UserInfoHelper.getUid())) {
            ivQuit.setVisibility(View.GONE);
        } else {
            ivQuit.setVisibility(View.VISIBLE);
        }
        tvVersion.setText(UIUtils.getVersion());
        try {
            tvCache.setText(DataCleanManagerUtils.getTotalCacheSize(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        initListener();
    }

    private void initListener() {
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).popFragment();
            }
        });
        RxView.clicks(ivQuit).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                UserInfoHelper.logout();
                ivQuit.setVisibility(View.GONE);
            }
        });
        RxView.clicks(rlVersion).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Beta.checkUpgrade(true, false);
            }
        });
        RxView.clicks(rlCache).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            DataCleanManagerUtils.clearAllCache(getActivity());
            tvCache.setText("0M");
        });
    }
}
