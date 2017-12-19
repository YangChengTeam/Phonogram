package com.yc.phonogram.ui.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import com.yc.phonogram.R;
import com.yc.phonogram.adapter.LPFragmentPagerAdapter;
import com.yc.phonogram.domain.PhonogramInfo;
import com.yc.phonogram.domain.PhonogramListInfo;
import com.yc.phonogram.ui.activitys.MainActivity;
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
    private LPFragmentPagerAdapter mLPFragmentPagerAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_learn;
    }

    @Override
    public void init() {
        mMainBgView= (MainBgView) getView(R.id.mainBgView);
        mView_pager = (ViewPager) getView(R.id.view_pager);
        mLPFragmentPagerAdapter = new LPFragmentPagerAdapter(getChildFragmentManager(),null);
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

        mView_pager.setAdapter(mLPFragmentPagerAdapter);
        mMainBgView.setIndexListener(new MainBgView.IndexListener() {
            @Override
            public void leftClick(int position) {
                if(null!=mView_pager&&mView_pager.getChildCount()>0){
                    mView_pager.setCurrentItem(position);
                }
            }

            @Override
            public void rightClcik(int position) {
                if(null!=mView_pager&&mView_pager.getChildCount()>0){
                    mView_pager.setCurrentItem(position);
                }
            }
        });
    }

    @Override
    public void loadData() {
        PhonogramListInfo phonogramListInfo = MainActivity.getMainActivity().getPhonogramListInfo();
        if(null==phonogramListInfo||null==phonogramListInfo.getPhonogramInfos()) return;
        List<PhonogramInfo> phonogramInfos = phonogramListInfo.getPhonogramInfos();
        if(null!=phonogramInfos&&phonogramInfos.size()>0){
            Log.d(TAG,"loadData"+phonogramInfos.size());
            List<Fragment> fragmentList=new ArrayList<>();
            for (int i = 0; i < phonogramInfos.size(); i++) {
                fragmentList.add(LearnPhonogramChildContentFragment.newInstance(phonogramInfos.get(i)));
            }
            mLPFragmentPagerAdapter.setNewFragments(fragmentList,null);
            mMainBgView.showIndex(fragmentList.size());
            mMainBgView.setIndex(0);
        }
    }
}
