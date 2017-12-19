package com.yc.phonogram.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.kk.utils.LogUtil;
import com.yc.phonogram.R;
import com.yc.phonogram.adapter.ReadItemPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangkai on 2017/12/15.
 */

public class ReadToMeFragment extends BaseFragment {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_read_to_me;
    }

    @Override
    public void init() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager view_pager = (ViewPager) getView(R.id.view_pager);
        List<Fragment> fragmentList=new ArrayList<>();
        for (int i = 0; i < 48; i++) {
            fragmentList.add(ReadItemFragment.newInstance(i));
        }
        ReadItemPagerAdapter readItemPagerAdapter =new ReadItemPagerAdapter(getChildFragmentManager(),fragmentList);
        view_pager.setAdapter(readItemPagerAdapter);
        view_pager.setOffscreenPageLimit(1);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.msg(position+"");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
