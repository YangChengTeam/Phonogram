package com.yc.phonogram.ui.fragments;

import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.danikula.videocache.HttpProxyCacheServer;
import com.jakewharton.rxbinding.view.RxView;
import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.xinqu.videoplayer.XinQuVideoPlayerStandard;
import com.yc.phonogram.App;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.CategoryDetailInfo;
import com.yc.phonogram.domain.CategoryInfo;
import com.yc.phonogram.engin.CategoryDetailEngine;
import com.yc.phonogram.helper.UserInfoHelper;
import com.yc.phonogram.ui.activitys.MainActivity;
import com.yc.phonogram.ui.popupwindow.PayPopupWindow;

import java.util.concurrent.TimeUnit;

import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;

/**
 * Created by wanglin  on 2019/5/15 11:29.
 */
public class CategoryDetailFragment extends BaseFragment {

    private ImageView ivBackList;
    private XinQuVideoPlayerStandard videoPlayer;
    private TextView strokeTitle, tvDesp;
    private CategoryInfo categoryInfo;
    private View videoVip;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_category_detail;
    }

    @Override
    public void init() {
        if (getArguments() != null) {
            categoryInfo = getArguments().getParcelable("category");
        }

        ivBackList = (ImageView) getView(R.id.iv_back_list);
        videoPlayer = (XinQuVideoPlayerStandard) getView(R.id.video_player);

        strokeTitle = (TextView) getView(R.id.stroke_title);

        tvDesp = (TextView) getView(R.id.tv_ph_desp);
        videoVip = getView(R.id.video_vip);

        if (MainActivity.getMainActivity().isPhonicsVip() || (categoryInfo != null && categoryInfo.getIs_free() == 0)) {
            videoVip.setVisibility(View.GONE);
        } else {
            videoVip.setVisibility(View.VISIBLE);
        }

        RxView.clicks(ivBackList).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (onBackListener != null) {
                onBackListener.onBack();
            }
        });

    }

    @Override
    public void loadData() {
        super.loadData();
        getData();
    }

    private void getData() {
        new CategoryDetailEngine(getActivity()).getCategoryDetail(categoryInfo.getId()).subscribe(new DisposableObserver<ResultInfo<CategoryDetailInfo>>() {
            @Override
            public void onNext(ResultInfo<CategoryDetailInfo> categoryDetailInfoResultInfo) {
                if (categoryDetailInfoResultInfo != null && categoryDetailInfoResultInfo.code == HttpConfig.STATUS_OK && categoryDetailInfoResultInfo.data != null) {
                    CategoryDetailInfo categoryDetailInfo = categoryDetailInfoResultInfo.data;

                    videoPlayer.widthRatio = 16;
                    videoPlayer.heightRatio = 9;
                    String proxyUrl = categoryDetailInfo.getUrl();
//                    HttpProxyCacheServer proxy = App.getProxy();
//                    if (null != proxy) {
//                        proxyUrl = proxy.getProxyUrl(categoryDetailInfo.getUrl());
//                    }
                    videoPlayer.setUp(proxyUrl, XinQuVideoPlayer.SCREEN_WINDOW_LIST, false, null == categoryDetailInfo.getTitle() ? "" : categoryDetailInfo.getTitle());

                    Glide.with(getActivity()).load(categoryDetailInfo.getImg()).apply(new RequestOptions()
                            .placeholder(R.mipmap.ic_player_error)
                            .error(R.mipmap.ic_player_error)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)//缓存源资源和转换后的资源
                            .skipMemoryCache(true)//跳过内存缓存
                    ).into(videoPlayer.thumbImageView);
                    playVideo();

                    strokeTitle.setText(categoryDetailInfo.getTitle());
                    tvDesp.setText(Html.fromHtml(categoryDetailInfo.getDesp()));


                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    private void playVideo() {
        RxView.clicks(videoPlayer.thumbImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> judgeVip());
        RxView.clicks(videoPlayer.startButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> judgeVip());

    }


    private void judgeVip() {
        if (MainActivity.getMainActivity().isPhonicsVip() || (categoryInfo != null && categoryInfo.getIs_free() == 0)) {

            videoPlayer.startVideo();

        } else {
            if (UserInfoHelper.isLogin(getActivity())) {
                PayPopupWindow payPopupWindow = new PayPopupWindow(getActivity());
                payPopupWindow.show(getActivity().getWindow().getDecorView().getRootView(), Gravity.CENTER);
            }
        }
    }

    private onBackListener onBackListener;

    public void setOnBackListener(CategoryDetailFragment.onBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    public interface onBackListener {
        void onBack();
    }

}
