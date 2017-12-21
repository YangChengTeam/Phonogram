package com.yc.phonogram.ui.fragments;

import android.support.v4.view.ViewPager;

import com.kk.utils.LogUtil;
import com.yc.phonogram.R;
import com.yc.phonogram.adapter.ReadItemPagerAdapter;
import com.yc.phonogram.domain.PhonogramInfo;
import com.yc.phonogram.domain.PhonogramListInfo;
import com.yc.phonogram.ui.activitys.MainActivity;
import com.yc.phonogram.ui.views.MainBgView;

import java.util.List;

/**
 * Created by zhangkai on 2017/12/15.
 */

public class ReadToMeFragment extends BaseFragment {

    private MainBgView mainBgView;

    private ReadItemPagerAdapter readItemPagerAdapter;

    private ViewPager viewPager;

    private int lastCurrentPosition = 0;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_read_to_me;
    }

    @Override
    public void init() {
        mainBgView = (MainBgView) getView(R.id.mainBgView);
        viewPager = (ViewPager) getView(R.id.view_pager);

        readItemPagerAdapter = new ReadItemPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(readItemPagerAdapter);
        viewPager.setOffscreenPageLimit(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.msg(position + "");
                mainBgView.setIndex(position);

                if (lastCurrentPosition > -1 && lastCurrentPosition != position) {
                    ((ReadItemFragment)readItemPagerAdapter.getItem(lastCurrentPosition)).stop();
                }
                lastCurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void loadData() {
        PhonogramListInfo phonogramListInfo = MainActivity.getMainActivity().getPhonogramListInfo();
        if (phonogramListInfo == null || phonogramListInfo.getPhonogramInfos() == null || phonogramListInfo.getPhonogramInfos().size() == 0) {
            return;
        }
        List<PhonogramInfo> phonogramInfos = phonogramListInfo.getPhonogramInfos();

        readItemPagerAdapter.setDatas(phonogramInfos);
        readItemPagerAdapter.notifyDataSetChanged();

        mainBgView.showIndex(phonogramInfos.size());
        mainBgView.setIndex(0);
        mainBgView.setIndexListener(new MainBgView.IndexListener() {
            @Override
            public void leftClick(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void rightClcik(int position) {
                viewPager.setCurrentItem(position);
            }
        });
    }
}
