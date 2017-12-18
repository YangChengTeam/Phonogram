package com.yc.phonogram.ui.activitys;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.phonogram.R;
import com.yc.phonogram.ui.fragments.IndexFragment;
import com.yc.phonogram.ui.fragments.LearnPhonogramFragment;
import com.yc.phonogram.ui.fragments.PhonicsFragments;
import com.yc.phonogram.ui.fragments.ReadToMeFragment;
import com.yc.phonogram.ui.popupwindow.SharePopupWindow;
import com.yc.phonogram.ui.views.MainBgView;

import java.util.concurrent.TimeUnit;
import rx.functions.Action1;


public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private ViewPager mViewPager;
    private ImageView mCenterBtn;
    private ImageView mIndexBtn;
    private ImageView mLearnBtn;
    private ImageView mReadTomeBtn;
    private ImageView mPhonicsBtn;
    private ImageView mShareBtn;


    private FragmentAdapter mFragmentAdapter;
    private int mCurrentIndex = -1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        mViewPager = findViewById(R.id.viewpager);
        mCenterBtn = findViewById(R.id.iv_center);
        mIndexBtn = findViewById(R.id.iv_index);
        mLearnBtn = findViewById(R.id.iv_learn);
        mReadTomeBtn = findViewById(R.id.iv_read_to_me);
        mPhonicsBtn = findViewById(R.id.iv_phonics);
        mShareBtn = findViewById(R.id.iv_share);


        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
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

}