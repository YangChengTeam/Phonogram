package com.yc.phonogram.ui.fragments;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;
import com.yc.phonogram.ui.IView;


/**
 * Created by zhangkai on 2017/7/21.
 */

public abstract class BaseFragment extends Fragment implements IView {
    protected View mRootView;
    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = View.inflate(getActivity(), getLayoutId(), null);
        }
        return mRootView;
    }


    public void loadData() {
    }

    public View getView(@IdRes int id) {
        return mRootView.findViewById(id);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        loadData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
//        Log.e(TAG, "onActivityCreated: ");
        prepareFetchData();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
//        LogUtil.msg("TAG ： " + getClass().getName() + "  isVisibleToUser   " + isVisibleToUser + "  isDataInitiated  " + isDataInitiated + "  isViewInitiated  " + isViewInitiated);
        prepareFetchData();
//        Log.e(TAG, "setUserVisibleHint: ");
    }

    public void fetchData() {
    }

    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }

}
