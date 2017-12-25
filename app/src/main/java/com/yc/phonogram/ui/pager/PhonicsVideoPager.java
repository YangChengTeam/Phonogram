package com.yc.phonogram.ui.pager;

import android.app.Activity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.danikula.videocache.HttpProxyCacheServer;
import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.xinqu.videoplayer.XinQuVideoPlayerStandard;
import com.yc.phonogram.App;
import com.yc.phonogram.R;
import com.yc.phonogram.base.BasePager;
import com.yc.phonogram.domain.MClassInfo;

/**
 * TinyHung@Outlook.com
 * 2017/12/19.
 * 首页拼读视频播放View
 */

public class PhonicsVideoPager extends BasePager {

    private final MClassInfo mData;

    public PhonicsVideoPager(Activity context, MClassInfo data) {
        super(context);
        this.mData=data;
        setContentView(R.layout.pager_phonics_video_player_layout);
    }


    @Override
    protected void initViews() {

    }

    @Override
    protected void loadData() {
        if(null==mData) return;
        XinQuVideoPlayerStandard videoPlayerStandard = mRootView.findViewById(R.id.video_player);
        videoPlayerStandard.widthRatio=16;
        videoPlayerStandard.heightRatio=9;
        String proxyUrl =mData.getVideo();
        HttpProxyCacheServer proxy = App.getProxy();
        if(null!=proxy){
            proxyUrl= proxy.getProxyUrl(mData.getVideo());
        }
        videoPlayerStandard.setUp(proxyUrl, XinQuVideoPlayer.SCREEN_WINDOW_LIST,false,null==mData.getTitle()?"":mData.getTitle());
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_player_error);
        options.error(R.mipmap.ic_player_error);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);//缓存源资源和转换后的资源
        options.skipMemoryCache(true);//跳过内存缓存
        Glide.with(mContext).load(mData.getCover()).apply(options).thumbnail(0.1f).into(videoPlayerStandard.thumbImageView);

    }
}
