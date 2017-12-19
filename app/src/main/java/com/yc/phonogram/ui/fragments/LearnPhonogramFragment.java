package com.yc.phonogram.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
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
    private boolean isScorring=false;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_learn;
    }

    @Override
    public void init() {
        mMainBgView= (MainBgView) getView(R.id.mainBgView);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                if(!isScorring){
                    mMainBgView.setIndex(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                isScorring=false;
            }
        });

        mView_pager.setAdapter(LPFragmentPagerAdapter);
        mMainBgView.setIndexListener(new MainBgView.IndexListener() {
            @Override
            public void leftClick(int position) {
                isScorring=true;
                mView_pager.setCurrentItem(position);
            }

            @Override
            public void rightClcik(int position) {
                isScorring=true;
                mView_pager.setCurrentItem(position);
            }
        });
        mMainBgView.showIndex(fragmentList.size());
        mMainBgView.setIndex(0);
    }
}
