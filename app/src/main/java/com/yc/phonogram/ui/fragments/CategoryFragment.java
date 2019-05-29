package com.yc.phonogram.ui.fragments;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.CategoryInfo;
import com.yc.phonogram.domain.CategoryInfoWrapperWrapper;
import com.yc.phonogram.engin.CategoryEngine;
import com.yc.phonogram.helper.CategoryInfoHelper;
import com.yc.phonogram.helper.ObservManager;
import com.yc.phonogram.ui.activitys.MainActivity;
import com.yc.phonogram.ui.adapter.CategoryItemAdapter;
import com.yc.phonogram.ui.popupwindow.PayPopupWindow;
import com.yc.phonogram.utils.ItemDecorationHelper;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import rx.functions.Action1;

/**
 * Created by wanglin  on 2019/5/15 09:05.
 * <p>
 * 微课学习类
 */
public class CategoryFragment extends BaseFragment implements Observer {

    private RecyclerView topRecyclerView;
    private RecyclerView bottomRecyclerView;

    private CategoryItemAdapter topItemAdapter;
    private CategoryItemAdapter bottomItemAdapter;
    private ItemDecorationHelper decorationHelper;

    private List<CategoryInfo> topCategoryInfos;

    private List<CategoryInfo> bottomCategoryInfos;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    public void init() {
        ObservManager.get().addMyObserver(this);
        topRecyclerView = (RecyclerView) getView(R.id.category_top_recyclerView);
        bottomRecyclerView = (RecyclerView) getView(R.id.category_bottom_recyclerView);
        topRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 6));
        topItemAdapter = new CategoryItemAdapter(null);
        topRecyclerView.setAdapter(topItemAdapter);
        decorationHelper = new ItemDecorationHelper(getActivity(), 8, 3);
        topRecyclerView.addItemDecoration(decorationHelper);


        bottomRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 6));
        bottomItemAdapter = new CategoryItemAdapter(null);
        bottomRecyclerView.setAdapter(bottomItemAdapter);
        bottomRecyclerView.addItemDecoration(decorationHelper);


        initListener();
    }


    @Override
    public void loadData() {
        super.loadData();

        CategoryInfoWrapperWrapper infoWrapperWrapper = CategoryInfoHelper.getInfoWrapperWrapper();
        if (infoWrapperWrapper != null && infoWrapperWrapper.getPindu() != null && infoWrapperWrapper.getYinbiao() != null) {

            bottomCategoryInfos = infoWrapperWrapper.getYinbiao().getDetail();
            bottomItemAdapter.setNewData(bottomCategoryInfos);

            topCategoryInfos = infoWrapperWrapper.getPindu().getDetail();
            topItemAdapter.setNewData(topCategoryInfos);

        }

        getData();

    }

    private void initListener() {
        topItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, topCategoryInfos);
                }


            }
        });

        bottomItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, bottomCategoryInfos);
                }


            }
        });
    }


    private void getData() {

        new CategoryEngine(getActivity()).getCategorys().subscribe(new Action1<ResultInfo<CategoryInfoWrapperWrapper>>() {
            @Override
            public void call(ResultInfo<CategoryInfoWrapperWrapper> categoryInfoWrapperWrapperResultInfo) {

                if (categoryInfoWrapperWrapperResultInfo != null && categoryInfoWrapperWrapperResultInfo.code == HttpConfig.STATUS_OK
                        && categoryInfoWrapperWrapperResultInfo.data != null) {

                    CategoryInfoHelper.setInfoWrapperWrapper(categoryInfoWrapperWrapperResultInfo.data);
                    CategoryInfoWrapperWrapper infoWrapperWrapper = categoryInfoWrapperWrapperResultInfo.data;
                    if (infoWrapperWrapper.getYinbiao() != null) {
                        bottomCategoryInfos = infoWrapperWrapper.getYinbiao().getDetail();
                        bottomItemAdapter.setNewData(bottomCategoryInfos);
                    }
                    if (infoWrapperWrapper.getPindu() != null) {
                        topCategoryInfos = infoWrapperWrapper.getPindu().getDetail();
                        topItemAdapter.setNewData(topCategoryInfos);
                    }
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        topRecyclerView.removeItemDecoration(decorationHelper);
        bottomRecyclerView.removeItemDecoration(decorationHelper);
    }

    private onItemClickListener onItemClickListener;

    public void setOnItemClickListener(CategoryFragment.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public interface onItemClickListener {
        void onItemClick(int position, List<CategoryInfo> categoryInfos);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null) {
            if (arg instanceof String) {
                String str = (String) arg;
                if (TextUtils.equals("pay success", str)) {
                    topItemAdapter.notifyDataSetChanged();
                    bottomItemAdapter.notifyDataSetChanged();
                }

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ObservManager.get().deleteMyObserver(this);
    }
}
