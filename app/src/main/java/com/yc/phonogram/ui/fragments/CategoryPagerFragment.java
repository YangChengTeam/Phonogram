package com.yc.phonogram.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;

import com.xinqu.videoplayer.XinQuVideoPlayerStandard;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.CategoryInfo;
import com.yc.phonogram.ui.activitys.MainActivity;
import com.yc.phonogram.ui.popupwindow.PayPopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglin  on 2019/5/15 11:47.
 */
public class CategoryPagerFragment extends BaseFragment {

    private List<Fragment> fragmentList = new ArrayList<>();

    private List<CategoryInfo> categoryInfos;

    private int prePos;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_category_viewpager;
    }

    @Override
    public void init() {

        if (getArguments() != null) {
            categoryInfos = getArguments().getParcelableArrayList("categoryInfos");
            prePos = getArguments().getInt("position");
        }
        if (categoryInfos != null) {
            for (int i = 0; i < categoryInfos.size(); i++) {
                CategoryDetailFragment categoryDetailFragment = new CategoryDetailFragment();

                Bundle bundle = new Bundle();
                bundle.putParcelable("category", categoryInfos.get(i));
                categoryDetailFragment.setArguments(bundle);

                categoryDetailFragment.setOnBackListener(new CategoryDetailFragment.onBackListener() {
                    @Override
                    public void onBack() {
                        if (onBackListener != null) {
                            onBackListener.onBack();
                        }
                    }
                });
                fragmentList.add(categoryDetailFragment);
            }
            final ViewPager viewPager = (ViewPager) getView(R.id.category_viewPager);


            viewPager.setAdapter(new CategoryAdapter(getChildFragmentManager()));

            if (prePos < categoryInfos.size())
                viewPager.setCurrentItem(prePos);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    XinQuVideoPlayerStandard.releaseAllVideos();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
    }

    public class CategoryAdapter extends FragmentPagerAdapter {
        public CategoryAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    private onBackListener onBackListener;

    public void setOnBackListener(onBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    public interface onBackListener {
        void onBack();
    }
}
