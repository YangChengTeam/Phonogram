package com.yc.phonogram.ui.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import com.yc.phonogram.R;
import com.yc.phonogram.adapter.LPFragmentPagerAdapter;
import com.yc.phonogram.ui.views.MainBgView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangkai on 2017/12/15.
 * 学音标
 */


public class LearnPhonogramFragment extends BaseFragment  {

    private static final String TAG =LearnPhonogramFragment.class.getSimpleName() ;
    private MainBgView mMainBgView;
    private ViewPager mView_pager;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_learn;
    }

    @Override
    public void init() {
        mMainBgView= (MainBgView) getView(R.id.mainBgView);
        mView_pager = (ViewPager) getView(R.id.view_pager);
        List<Fragment> fragmentList=new ArrayList<>();
        for (int i = 0; i < 48; i++) {
            fragmentList.add(LearnPhonogramChildContentFragment.newInstance("english"+i));
        }
        LPFragmentPagerAdapter LPFragmentPagerAdapter =new LPFragmentPagerAdapter(getChildFragmentManager(),fragmentList);
        mView_pager.setOffscreenPageLimit(1);
        mView_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mMainBgView.setIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mView_pager.setAdapter(LPFragmentPagerAdapter);
        mMainBgView.setIndexListener(new MainBgView.IndexListener() {
            @Override
            public void leftClick(int position) {
                mView_pager.setCurrentItem(position);
            }

            @Override
            public void rightClcik(int position) {
                mView_pager.setCurrentItem(position);
            }
        });
        mMainBgView.showIndex(fragmentList.size());
        mMainBgView.setIndex(0);
    }

    @Override
    public void loadData() {

    }
}
