package com.yc.phonogram.ui.fragments;

import android.os.Bundle;
import android.os.Parcelable;

import com.xinqu.videoplayer.XinQuVideoPlayerStandard;
import com.yc.phonogram.R;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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


        categoryFragment.setOnItemClickListener((position, categoryInfos) -> {
            CategoryPagerFragment categoryPagerFragment = new CategoryPagerFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("categoryInfos", (ArrayList<? extends Parcelable>) categoryInfos);
            bundle.putInt("position", position);
            categoryPagerFragment.setArguments(bundle);

            categoryPagerFragment.setOnBackListener(() -> {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                    XinQuVideoPlayerStandard.releaseAllVideos();
                }
            });
            addFragment(categoryPagerFragment, categoryPagerFragment.getClass().getName());
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
