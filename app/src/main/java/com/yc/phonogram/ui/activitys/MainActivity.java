package com.yc.phonogram.ui.activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jakewharton.rxbinding.view.RxView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.yc.phonogram.App;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.AdvInfo;
import com.yc.phonogram.domain.Config;
import com.yc.phonogram.domain.PhonogramListInfo;
import com.yc.phonogram.domain.VipInfo;
import com.yc.phonogram.engin.PhonographEngine;
import com.yc.phonogram.helper.UserInfoHelper;
import com.yc.phonogram.ui.fragments.CategoryMainFragment;
import com.yc.phonogram.ui.fragments.IndexFragment;
import com.yc.phonogram.ui.fragments.LearnPhonogramFragment;
import com.yc.phonogram.ui.fragments.PersonCenterFragment;
import com.yc.phonogram.ui.fragments.ReadToMeFragment;
import com.yc.phonogram.ui.popupwindow.LogoutPopupWindow;
import com.yc.phonogram.ui.popupwindow.PhonogramPopupWindow;
import com.yc.phonogram.ui.popupwindow.SharePopupWindow;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rx.functions.Action1;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.util.LogUtil;
import yc.com.rthttplibrary.util.PreferenceUtil;
import yc.com.rthttplibrary.util.TaskUtil;


public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = "MainActivity";

    private ViewPager mViewPager;
    private ImageView mIndexBtn;
    private ImageView mLearnBtn;
    private ImageView mReadTomeBtn;
    private ImageView mPhonicsBtn;
    private ImageView mShareBtn;
    private static MainActivity INSTANSE;
    private FrameLayout mainContainer;

    private int mCurrentIndex = -1;

    private int mChildCureenItemIndex = 0;

    public void setChildCureenItemIndex(int cureenItemIndex) {
        mChildCureenItemIndex = cureenItemIndex;
    }

    public int getChildCureenItemIndex() {
        return mChildCureenItemIndex;
    }

    public static MainActivity getMainActivity() {
        return INSTANSE;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {

        if (SplashActivity.getInstance() != null) {
            SplashActivity.getInstance().finish();
        }


        INSTANSE = this;
        mViewPager = findViewById(R.id.viewpager);
        ImageView mCenterBtn = findViewById(R.id.iv_center);
        mIndexBtn = findViewById(R.id.iv_index);
        mLearnBtn = findViewById(R.id.iv_learn);
        mReadTomeBtn = findViewById(R.id.iv_read_to_me);
        mPhonicsBtn = findViewById(R.id.iv_phonics);
        mShareBtn = findViewById(R.id.iv_share);
        TextView tvH5page = findViewById(R.id.tv_h5page);
        RelativeLayout rlH5page = findViewById(R.id.rl_h5page);
        mainContainer = findViewById(R.id.main_container);
        PersonCenterFragment centerFragment = new PersonCenterFragment();
        switchFragment(centerFragment, centerFragment.getClass().getName());
        String str = PreferenceUtil.getImpl(this).getString(Config.ADV_INFO, "");
        final AdvInfo advInfo = JSON.parseObject(str, AdvInfo.class);
        if (null != advInfo && !TextUtils.isEmpty(advInfo.getButton_txt())) {
            tvH5page.setText(advInfo.getButton_txt());
        }

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setCurrentItem(0);
        tab(0);
        mCurrentIndex = 0;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mCurrentIndex == position) {
                    return;
                }
                mCurrentIndex = position;
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1:
                        MobclickAgent.onEvent(MainActivity.this, "xueyinbiao_click", "学音标");
                        break;
                    case 2:
                        MobclickAgent.onEvent(MainActivity.this, "genwoxue_click", "跟我学");
                        break;
                    case 3:
                        MobclickAgent.onEvent(MainActivity.this, "quwei_click", "趣味音标课");
                        break;
                    default:
                        break;
                }

                tab(position);
                if (position == 1 || position == 2) {
                    mShareBtn.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.main_view_selector));
                } else {
                    mShareBtn.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.main_share_selector));
                }
                if (position == 1) {
                    mLearnPhonogramFragment.pause();
                }
                stop();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        RxView.clicks(mIndexBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                setCurrent(0);
            }
        });

        RxView.clicks(mLearnBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                setCurrent(1);
            }
        });

        RxView.clicks(mReadTomeBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                setCurrent(2);
            }
        });

        RxView.clicks(mPhonicsBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                setCurrent(3);
            }
        });

        RxView.clicks(rlH5page).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (null != advInfo) {
                    Intent intent = new Intent(MainActivity.this, AdvInfoActivity.class);
                    intent.putExtra("url", advInfo.getUrl());
                    intent.putExtra("title", advInfo.getButton_txt());
                    startActivity(intent);
                }

            }
        });

        RxView.clicks(mShareBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (mCurrentIndex == 1 || mCurrentIndex == 2) {
                    PhonogramPopupWindow phonogramPopupWindow = new PhonogramPopupWindow(MainActivity.this);
                    phonogramPopupWindow.show();
                } else {
                    SharePopupWindow sharePopupWindow = new SharePopupWindow(MainActivity.this);
                    sharePopupWindow.show(getWindow().getDecorView().getRootView(), Gravity.CENTER);
                }

            }
        });

        RxView.clicks(mCenterBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mViewPager.setVisibility(View.GONE);
                mainContainer.setVisibility(View.VISIBLE);
//                PayPopupWindow payPopupWindow = new PayPopupWindow(MainActivity.this);
//                payPopupWindow.show();
            }
        });

        requestPermission();

        App.getApp().getLoginInfo(new Runnable() {
            @Override
            public void run() {
                LogUtil.msg("初始化数据完成--->");
            }
        });

    }

    public void switchFragment(Fragment fragment, String tag) {
        FragmentTransaction bt = getSupportFragmentManager().beginTransaction();
        bt.replace(R.id.main_container, fragment);

        bt.addToBackStack(tag);
        bt.commit();
    }

    public void popFragment() {
        getSupportFragmentManager().popBackStack();
    }

    private void setCurrent(int index) {
        mViewPager.setVisibility(View.VISIBLE);
        mainContainer.setVisibility(View.GONE);
        mViewPager.setCurrentItem(index);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void stop() {
        mReadToMeFragment.stop();
        XinQuVideoPlayer.releaseAllVideos();
    }

    private void tab(int index) {
        reset();
        switch (index) {
            case 0:
                mIndexBtn.setSelected(true);
                break;
            case 1:
                mLearnBtn.setSelected(true);
                break;
            case 2:
                mReadTomeBtn.setSelected(true);
                break;
            case 3:
                mPhonicsBtn.setSelected(true);
                break;
        }
    }

    private void reset() {
        mIndexBtn.setSelected(false);
        mLearnBtn.setSelected(false);
        mReadTomeBtn.setSelected(false);
        mPhonicsBtn.setSelected(false);
    }


    private IndexFragment mIndexFragment;
    private LearnPhonogramFragment mLearnPhonogramFragment;
    private ReadToMeFragment mReadToMeFragment;
    //    private PhonicsFragments mPhonicsFragments;
    private CategoryMainFragment mPhonicsFragments;


    class FragmentAdapter extends FragmentStatePagerAdapter {
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (mIndexFragment == null) {
                    mIndexFragment = new IndexFragment();
                }
                return mIndexFragment;
            } else if (position == 1) {
                if (mLearnPhonogramFragment == null) {
                    mLearnPhonogramFragment = new LearnPhonogramFragment();
                }
                return mLearnPhonogramFragment;
            } else if (position == 2) {
                if (mReadToMeFragment == null) {
                    mReadToMeFragment = new ReadToMeFragment();
                }
                return mReadToMeFragment;
            } else if (position == 3) {
                if (mPhonicsFragments == null) {
                    mPhonicsFragments = new CategoryMainFragment();
                }
                return mPhonicsFragments;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    private PhonogramListInfo phonogramListInfo;

    public PhonogramListInfo getPhonogramListInfo() {
        return phonogramListInfo;
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadData() {
        super.loadData();

        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                String data = PreferenceUtil.getImpl(getApplicationContext()).getString(Config.PHONOGRAM_LIST_URL, "");
                if (!data.isEmpty()) {
                    try {
                        final PhonogramListInfo resultInfo = JSON.parseObject(data, new TypeReference<PhonogramListInfo>() {
                        }.getType());
                        if (resultInfo != null) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showInfo(resultInfo);
                                }
                            });
                        }
                    } catch (Exception e) {
                        LogUtil.msg("getPhonogramList本地缓存" + e);
                    }
                }
            }
        });

        new PhonographEngine(this).getPhonogramList().subscribe(phonogramListInfoResultInfo -> {
            if (phonogramListInfoResultInfo != null && phonogramListInfoResultInfo.code == HttpConfig.STATUS_OK && phonogramListInfoResultInfo.data !=
                    null && phonogramListInfoResultInfo.data.getPhonogramInfos() != null &&
                    phonogramListInfoResultInfo.data.getPhonogramInfos().size() > 0) {
                showInfo(phonogramListInfoResultInfo.data);
                TaskUtil.getImpl().runTask(() -> PreferenceUtil.getImpl(getApplicationContext()).putString(Config.PHONOGRAM_LIST_URL, JSON.toJSONString
                        (phonogramListInfoResultInfo.data)));
            }
        });
    }

    public void goToPage(int position) {
        if (mCurrentIndex == 1) {
            mLearnPhonogramFragment.setCurrentItem(position);
        } else if (mCurrentIndex == 2) {
            mReadToMeFragment.setReadCurrentPosition(position);
        }
    }

    private void showInfo(PhonogramListInfo phonogramListInfo) {
        this.phonogramListInfo = phonogramListInfo;
        if (mLearnPhonogramFragment != null && mReadToMeFragment != null) {
            mLearnPhonogramFragment.loadData();
            mReadToMeFragment.loadData();
        }
    }

    @Override
    public void onBackPressed() {
        if (XinQuVideoPlayer.backPress()) {
            return;
        }
        final LogoutPopupWindow logoutPopupWindow = new LogoutPopupWindow(this);
        logoutPopupWindow.setLogoutListener(new LogoutPopupWindow.LogoutListener() {
            @Override
            public void logout() {
                logoutPopupWindow.dismiss();
                stop();
                finish();
            }
        });
        logoutPopupWindow.show();
    }

    public void saveVip(String vip) {
        boolean flag = false;
        String vips = PreferenceUtil.getImpl(this).getString("vip", "");
        String[] vipArr = vips.split(",");
        for (String tmp : vipArr) {
            if (tmp.equals(vip)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            PreferenceUtil.getImpl(this).putString("vip", vips + "," + vip);
        }
    }

    public boolean isVip(String vip) {
        boolean flag = false;
        String vips = PreferenceUtil.getImpl(this).getString("vip", "");
        String[] vipArr = vips.split(",");

        for (String tmp : vipArr) {
            if (tmp.equals(vip)) {
                flag = true;
                break;
            }
        }

        if (!flag) {
//            LoginDataInfo loginDataInfo = App.getApp().getLoginDataInfo();
//            if (loginDataInfo != null) {
            List<VipInfo> vipInfoList = UserInfoHelper.getVipInfos();
            if (vipInfoList != null) {
                for (VipInfo vipInfo : vipInfoList) {
                    if (vip.equals(vipInfo.getType() + "")) {
                        flag = true;
                        break;
                    }
                }
            }
//            }
        }
        return flag;
    }

    /**
     * 点读会员
     *
     * @return
     */

    public boolean isPhonogramVip() {
        return isVip(Config.PHONOGRAM_VIP + "") || isPhonogramOrPhonicsVip() || isSuperVip();
    }

    /**
     * 微课会员
     *
     * @return
     */
    public boolean isPhonicsVip() {
        return isVip(Config.PHONICS_VIP + "") || isPhonogramOrPhonicsVip() || isSuperVip();
    }

    /**
     * 点读加微课会员
     *
     * @return
     */
    public boolean isPhonogramOrPhonicsVip() {
        return isVip(Config.PHONOGRAMORPHONICS_VIP + "") || isSuperVip() || (isVip(Config.PHONOGRAM_VIP + "") && isVip(Config.PHONICS_VIP + ""));
    }

    public boolean isSuperVip() {
        return isVip(Config.SUPER_VIP + "") || isCorrectPromiss() || isCorrectPronunciation();
    }

    public boolean isCorrectPronunciation() {
        return isVip(Config.CORRECTPRONUNCIATION_VIP + "");
    }

    public boolean isCorrectPromiss() {
        return isVip(Config.CORRECTPRONUNCIATIONPROMISS_VIP + "");
    }


    private static final int WRITE = 100;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(WRITE)
    public void requestPermission() {
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)) {
            EasyPermissions.requestPermissions(this, "请允许文件读写权限", WRITE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


}