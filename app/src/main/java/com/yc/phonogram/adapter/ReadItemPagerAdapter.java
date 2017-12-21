package com.yc.phonogram.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.yc.phonogram.domain.PhonogramInfo;
import com.yc.phonogram.ui.fragments.ReadItemFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * FragmentPager适配器
 */

public class ReadItemPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {

    private List<ReadItemFragment> mFragment;

    private List<PhonogramInfo> datas;

    private ViewPager mViewPager;

    public ReadItemPagerAdapter(FragmentManager fm,ViewPager viewPager) {
        super(fm);
        mViewPager = viewPager;
    }

    public void setDatas(List<PhonogramInfo> list) {
        this.datas = list;
        mFragment = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            ReadItemFragment readItemFragment = new ReadItemFragment();
            readItemFragment.setPhonogramInfo(datas.get(i));
            mFragment.add(readItemFragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
        //return (ReadItemFragment) this.instantiateItem(mViewPager, mViewPager.getCurrentItem());
    }

    @Override
    public int getCount() {
        return null == mFragment ? 0 : mFragment.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }


}
