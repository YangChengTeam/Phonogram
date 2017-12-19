package com.yc.phonogram.ui.pager;

import android.content.Context;
import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.xinqu.videoplayer.XinQuVideoPlayerStandard;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.MClassInfo;

/**
 * TinyHung@Outlook.com
 * 2017/12/19.
 * 首页拼读视频播放View
 */

public class PhonicsVideoPager  {

    private final MClassInfo mData;
    private final Context mContext;
    private View mRootView;

    public PhonicsVideoPager(Context context, MClassInfo data) {
        this.mContext=context;
        this.mData=data;
        initViews();
    }

    private void initViews() {
        mRootView = View.inflate(mContext, R.layout.pager_phonics_video_player_layout, null);
        XinQuVideoPlayerStandard videoPlayerStandard = (XinQuVideoPlayerStandard) mRootView.findViewById(R.id.video_player);
        videoPlayerStandard.setUp(mData.getVideo(), XinQuVideoPlayer.SCREEN_WINDOW_LIST,true,"测试");
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.main_bg);
        options.error(R.mipmap.main_bg);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);//缓存源资源和转换后的资源
        options.skipMemoryCache(true);//跳过内存缓存
        Glide.with(mContext).load(mData.getCover()).apply(options).thumbnail(0.1f).into(videoPlayerStandard.thumbImageView);
    }

    public View getView() {
        return mRootView;
    }
}
