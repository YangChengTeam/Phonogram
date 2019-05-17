package com.yc.phonogram.ui.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.xinqu.videoplayer.XinQuVideoPlayerStandard;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.CategoryInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglin  on 2019/5/15 10:47.
 * <p>
 * 微课学习主界面
 */
public class CategoryMainFragment extends BaseFragment {


    @Override
    public int getLayoutId() {
        return R.layout.fragment_category_main;
    }

    @Override
    public void init() {

        final CategoryFragment categoryFragment = new CategoryFragment();


        categoryFragment.setOnItemClickListener(new CategoryFragment.onItemClickListener() {


            @Override
            public void onItemClick(int position, List<CategoryInfo> categoryInfos) {
                CategoryPagerFragment categoryPagerFragment = new CategoryPagerFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("categoryInfos", (ArrayList<? extends Parcelable>) categoryInfos);
                bundle.putInt("position", position);
                categoryPagerFragment.setArguments(bundle);

                categoryPagerFragment.setOnBackListener(new CategoryPagerFragment.onBackListener() {
                    @Override
                    public void onBack() {
                        if (getActivity() != null) {
                            getActivity().getSupportFragmentManager().popBackStack();
                            XinQuVideoPlayerStandard.releaseAllVideos();
                        }
                    }
                });
                addFragment(categoryPagerFragment, categoryPagerFragment.getClass().getName());
            }

        });

        addFragment(categoryFragment, categoryFragment.getClass().getName());

    }


    public void addFragment(Fragment fragment, String tag) {


        if (getActivity() != null) {
            FragmentManager fm = getActivity().getSupportFragmentManager();

            FragmentTransaction ft = fm.beginTransaction();

            ft.replace(R.id.fl_container, fragment);
            ft.addToBackStack(tag);
            ft.commit();

        }
    }


}
