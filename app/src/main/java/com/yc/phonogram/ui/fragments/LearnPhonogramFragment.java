package com.yc.phonogram.ui.fragments;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.PhonogramInfo;
import com.yc.phonogram.domain.PhonogramListInfo;
import com.yc.phonogram.helper.SeekBarHelper;
import com.yc.phonogram.helper.UserInfoHelper;
import com.yc.phonogram.ui.activitys.MainActivity;
import com.yc.phonogram.ui.pager.LearnVideoPager;
import com.yc.phonogram.ui.popupwindow.PayPopupWindow;
import com.yc.phonogram.ui.views.MainBgView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by zhangkai on 2017/12/15.
 * 首页学音标
 */
public class LearnPhonogramFragment extends BaseFragment {

    private static final String TAG = LearnPhonogramFragment.class.getSimpleName();
    private MainBgView mMainBgView;
    private ViewPager mViewPager;
    private LearnPagerAdapter mLearnPagerAdapter = null;
    private List<PhonogramInfo> mPhonogramInfos = null;
    private Map<Integer, LearnVideoPager> mPagerMap = null;//方便调用View的伪生命周期方法

    private int oldCureenIndex = 0;//过去显示到第几个Poistion 了
    private int CHANGE_ODE_DESTROY = 1;
    private int CHANGE_ODE_RESUME = 2;
    private int CHANGE_ODE_PAUSE = 3;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_learn;
    }

    @Override
    public void init() {
        mPagerMap = new HashMap<>();
        mMainBgView = (MainBgView) getView(R.id.mainBgView);
        mViewPager = (ViewPager) getView(R.id.view_pager);
        initPagerAdapter();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (null != mLearnPagerAdapter && null != mViewPager) {
                mViewPager.setCurrentItem(MainActivity.getMainActivity().getChildCureenItemIndex(), false);
            }
        }
    }

    private void initPagerAdapter() {
        mLearnPagerAdapter = new LearnPagerAdapter();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                XinQuVideoPlayer.releaseAllVideos();
                onLifeChange(oldCureenIndex, CHANGE_ODE_PAUSE);
                //如果用户没有购买章节
                if (position >= 8 && !MainActivity.getMainActivity().isPhonogramVip()) {
                    mMainBgView.setIndex(oldCureenIndex);
                    mViewPager.setCurrentItem(oldCureenIndex, false);
                    if (UserInfoHelper.isLogin(getActivity())) {
                        PayPopupWindow payPopupWindow = new PayPopupWindow(getActivity());
                        payPopupWindow.show(getActivity().getWindow().getDecorView(), Gravity.CENTER);
                    }
                    return;
                }
                mMainBgView.setIndex(position);
                (MainActivity.getMainActivity()).setChildCureenItemIndex(position);
                oldCureenIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mLearnPagerAdapter);
        mMainBgView.setIndexListener(new SeekBarHelper.IndexListener() {
            @Override
            public void leftClick(int position) {
                if (null != mViewPager && mViewPager.getChildCount() > 0) {
                    mViewPager.setCurrentItem(position);
                    (MainActivity.getMainActivity()).setChildCureenItemIndex(position);
                }
            }

            @Override
            public void rightClcik(int position) {
                if (null != mViewPager && mViewPager.getChildCount() > 0) {
                    mViewPager.setCurrentItem(position);
                    (MainActivity.getMainActivity()).setChildCureenItemIndex(position);
                }
            }
        });
    }

    @Override
    public void loadData() {
        PhonogramListInfo phonogramListInfo = MainActivity.getMainActivity().getPhonogramListInfo();
        if (null == phonogramListInfo || null == phonogramListInfo.getPhonogramInfos()) return;
        mPhonogramInfos = phonogramListInfo.getPhonogramInfos();
        if (null != mPhonogramInfos && mPhonogramInfos.size() > 0) {
            mMainBgView.showIndex(mPhonogramInfos.size());
            mMainBgView.setIndex(0);
            if (null != mLearnPagerAdapter) {
                mLearnPagerAdapter.notifyDataSetChanged();
            } else {
                initPagerAdapter();
            }
        }
    }


    private class LearnPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return null == mPhonogramInfos ? 0 : mPhonogramInfos.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhonogramInfo phonogramInfo = mPhonogramInfos.get(position);
            if (null != phonogramInfo) {
                LearnVideoPager videoPager = new LearnVideoPager(getActivity(), phonogramInfo);
                View view = videoPager.getItemView();
                view.setId(position);
                mPagerMap.put(position, videoPager);
                container.addView(view);
                return view;
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(container.findViewById(position));
            mPagerMap.remove(position);
        }
    }

    private void onLifeChange(int poistion, int CHANGE_MODE) {
        if (null != mPagerMap && mPagerMap.size() > 0) {
            Iterator<Map.Entry<Integer, LearnVideoPager>> iterator = mPagerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, LearnVideoPager> next = iterator.next();
                if (poistion == next.getKey()) {
                    LearnVideoPager playerTempPager = next.getValue();
                    if (null != playerTempPager) {
                        if (CHANGE_ODE_DESTROY == CHANGE_MODE) {
                            playerTempPager.onDestroyView();
                            return;
                        } else if (CHANGE_ODE_RESUME == CHANGE_MODE) {
                            playerTempPager.onResume();
                            return;
                        } else if (CHANGE_ODE_PAUSE == CHANGE_MODE) {
                            playerTempPager.onPause();
                            return;
                        }
                    }
                }
            }
        }
    }

    public void setCurrentItem(int index) {
        if (null != mViewPager && mViewPager.getChildCount() > 0) {
            mViewPager.setCurrentItem(index);
        }
    }

    public void pause() {
        onLifeChange(mViewPager.getCurrentItem(), CHANGE_ODE_PAUSE);
    }

    @Override
    public void onResume() {
        super.onResume();
        onLifeChange(mViewPager.getCurrentItem(), CHANGE_ODE_RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        XinQuVideoPlayer.goOnPlayOnPause();
        onLifeChange(mViewPager.getCurrentItem(), CHANGE_ODE_PAUSE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onLifeChange(mViewPager.getCurrentItem(), CHANGE_ODE_DESTROY);
        if (null != mPagerMap) {
            mPagerMap.clear();
        }
    }
}
