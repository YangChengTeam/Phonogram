package com.yc.phonogram.ui.activitys;

import android.content.Intent;

import android.os.Build;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ScreenUtil;
import com.yc.phonogram.R;
import com.yc.phonogram.ui.popupwindow.PayPopupWindow;
import com.yc.phonogram.ui.popupwindow.SharePopupWindow;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.Config;
import com.yc.phonogram.domain.PhonogramListInfo;
import com.yc.phonogram.engin.PhonogramEngin;
import com.yc.phonogram.ui.fragments.IndexFragment;
import com.yc.phonogram.ui.fragments.LearnPhonogramFragment;
import com.yc.phonogram.ui.fragments.PhonicsFragments;
import com.yc.phonogram.ui.fragments.ReadToMeFragment;
import com.yc.phonogram.ui.popupwindow.SharePopupWindow;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;


public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private ViewPager mViewPager;
    private ImageView mIndexBtn;
    private ImageView mLearnBtn;
    private ImageView mReadTomeBtn;
    private ImageView mPhonicsBtn;

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
        INSTANSE = this;
        mViewPager = findViewById(R.id.viewpager);
        ImageView mCenterBtn = findViewById(R.id.iv_center);
        mIndexBtn = findViewById(R.id.iv_index);
        mLearnBtn = findViewById(R.id.iv_learn);
        mReadTomeBtn = findViewById(R.id.iv_read_to_me);
        mPhonicsBtn = findViewById(R.id.iv_phonics);
        ImageView mShareBtn = findViewById(R.id.iv_share);

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
                SharePopupWindow sharePopupWindow = new SharePopupWindow(MainActivity.this);
                sharePopupWindow.show();
            }
        });

        RxView.clicks(mCenterBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                PayPopupWindow payPopupWindow =new PayPopupWindow(MainActivity.this);
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

    private void showInfo(PhonogramListInfo phonogramListInfo) {
        this.phonogramListInfo = phonogramListInfo;
        if(mLearnPhonogramFragment != null && mReadToMeFragment != null) {
            mLearnPhonogramFragment.loadData();
            mReadToMeFragment.loadData();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(XinQuVideoPlayer.backPress()){
                XinQuVideoPlayer.releaseAllVideos();
                return true;
            }
            new QMUIDialog.MessageDialogBuilder(MainActivity.this)
                    .setMessage("确定要退出吗？")
                    .addAction("确定", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                            finish();

                        }
                    })
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}