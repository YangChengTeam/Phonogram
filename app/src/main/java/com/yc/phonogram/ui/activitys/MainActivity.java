package com.yc.phonogram.ui.activitys;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.LogUtil;
import com.kk.utils.PreferenceUtil;
import com.kk.utils.TaskUtil;
import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.yc.phonogram.App;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.Config;
import com.yc.phonogram.domain.LoginDataInfo;
import com.yc.phonogram.domain.PhonogramListInfo;
import com.yc.phonogram.domain.VipInfo;
import com.yc.phonogram.engin.PhonogramEngin;
import com.yc.phonogram.ui.fragments.IndexFragment;
import com.yc.phonogram.ui.fragments.LearnPhonogramFragment;
import com.yc.phonogram.ui.fragments.PhonicsFragments;
import com.yc.phonogram.ui.fragments.ReadToMeFragment;
import com.yc.phonogram.ui.popupwindow.LogoutPopupWindow;
import com.yc.phonogram.ui.popupwindow.PayPopupWindow;
import com.yc.phonogram.ui.popupwindow.PhonogramPopupWindow;
import com.yc.phonogram.ui.popupwindow.SharePopupWindow;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;


public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private ViewPager mViewPager;
    private ImageView mIndexBtn;
    private ImageView mLearnBtn;
    private ImageView mReadTomeBtn;
    private ImageView mPhonicsBtn;
    private ImageView mShareBtn;
    private static MainActivity INSTANSE;

    private int mCurrentIndex = -1;

    public static MainActivity getMainActivity() {
        return INSTANSE;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        SplashActivity.getApp().finish();

        INSTANSE = this;
        mViewPager = findViewById(R.id.viewpager);
        ImageView mCenterBtn = findViewById(R.id.iv_center);
        mIndexBtn = findViewById(R.id.iv_index);
        mLearnBtn = findViewById(R.id.iv_learn);
        mReadTomeBtn = findViewById(R.id.iv_read_to_me);
        mPhonicsBtn = findViewById(R.id.iv_phonics);
        mShareBtn = findViewById(R.id.iv_share);

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

            @Override
            public void onPageSelected(int position) {
                tab(position);
                if (position == 1 || position == 2) {
                    mShareBtn.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.mipmap.main_phonogram_view));
                } else {
                    mShareBtn.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.mipmap.main_share));
                }
                XinQuVideoPlayer.releaseAllVideos();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        RxView.clicks(mIndexBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mViewPager.setCurrentItem(0);
            }
        });

        RxView.clicks(mLearnBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mViewPager.setCurrentItem(1);
            }
        });

        RxView.clicks(mReadTomeBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mViewPager.setCurrentItem(2);
            }
        });

        RxView.clicks(mPhonicsBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mViewPager.setCurrentItem(3);
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
                    sharePopupWindow.show();
                }

            }
        });

        RxView.clicks(mCenterBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                PayPopupWindow payPopupWindow = new PayPopupWindow(MainActivity.this);
                payPopupWindow.show();
            }
        });
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
    private PhonicsFragments mPhonicsFragments;

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
                    mPhonicsFragments = new PhonicsFragments();
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

        new PhonogramEngin(this).getPhonogramList().subscribe(new Action1<ResultInfo<PhonogramListInfo>>() {
            @Override
            public void call(final ResultInfo<PhonogramListInfo> phonogramListInfoResultInfo) {
                if (phonogramListInfoResultInfo.code == HttpConfig.STATUS_OK && phonogramListInfoResultInfo.data !=
                        null && phonogramListInfoResultInfo.data.getPhonogramInfos() != null &&
                        phonogramListInfoResultInfo.data.getPhonogramInfos().size() > 0) {
                    showInfo(phonogramListInfoResultInfo.data);
                    TaskUtil.getImpl().runTask(new Runnable() {
                        @Override
                        public void run() {
                            PreferenceUtil.getImpl(getApplicationContext()).putString(Config.PHONOGRAM_LIST_URL, JSON.toJSONString
                                    (phonogramListInfoResultInfo.data));
                        }
                    });
                }
            }
        });
    }

    public void goToPage(int position) {
        if (mCurrentIndex == 1) {

        } else if (mCurrentIndex == 2) {
            
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
            PreferenceUtil.getImpl(this).putString("vip", vip);
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
            LoginDataInfo loginDataInfo = App.getApp().getLoginDataInfo();
            if (loginDataInfo != null) {
                List<VipInfo> vipInfoList = loginDataInfo.getVipInfoList();
                if (vipInfoList != null) {
                    for (VipInfo vipInfo : vipInfoList) {
                        if (vip.equals(vipInfo.getType() + "")) {
                            flag = true;
                            break;
                        }
                    }
                }
            }
        }
        return flag;
    }

    public boolean isPhonogramVip() {
        return isVip(Config.PHONOGRAM_VIP + "");
    }

    public boolean isPhonicsVip() {
        return isVip(Config.PHONICS_VIP + "");
    }

    public boolean isPhonogramOrPhonicsVip() {
        return isVip(Config.PHONOGRAMORPHONICS_VIP + "");
    }

    public boolean isSuperVip() {
        return isVip(Config.SUPER_VIP + "");
    }

}