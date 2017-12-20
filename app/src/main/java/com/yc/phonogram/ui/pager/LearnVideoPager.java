package com.yc.phonogram.ui.pager;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.xinqu.videoplayer.XinQuVideoPlayerStandard;
import com.yc.phonogram.R;
import com.yc.phonogram.adapter.LPContentListAdapter;
import com.yc.phonogram.base.BasePager;
import com.yc.phonogram.domain.PhonogramInfo;

/**
 * TinyHung@Outlook.com
 * 2017/12/19.
 * 首页学音标
 */

public class LearnVideoPager  extends BasePager{

    private static final String TAG = LearnVideoPager.class.getSimpleName();
    private final PhonogramInfo mData;
    private LPContentListAdapter mLpContentListAdapter;
    private ImageView mIv_lp_logo;
    private TextView mTv_lp_tips_content;
    private XinQuVideoPlayerStandard mVidepPlayer;

    public LearnVideoPager(Activity context, PhonogramInfo phonogramInfo) {
        super(context);
        this.mData=phonogramInfo;
        setContentView(R.layout.pager_learn_child_content);
    }


    @Override
    protected void initViews() {
        if(null==mContext) return;
        //封面
        mIv_lp_logo = (ImageView) getView(R.id.iv_lp_logo);
        mTv_lp_tips_content = (TextView) getView(R.id.tv_lp_tips_content);
        mVidepPlayer = (XinQuVideoPlayerStandard) getView(R.id.video_player);
        RecyclerView recyclerView = (RecyclerView) getView(R.id.recyclerview_lp);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        mLpContentListAdapter = new LPContentListAdapter(mContext,null);
        recyclerView.setAdapter(mLpContentListAdapter);

        XinQuVideoPlayerStandard videoPlayerStandard = mRootView.findViewById(R.id.video_player);
        videoPlayerStandard.setUp(mData.getVideo(), XinQuVideoPlayer.SCREEN_WINDOW_LIST,true,"测试");
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.main_bg);
        options.error(R.mipmap.main_bg);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);//缓存源资源和转换后的资源
        options.skipMemoryCache(true);//跳过内存缓存
        Glide.with(mContext).load(mData.getCover()).apply(options).thumbnail(0.1f).into(videoPlayerStandard.thumbImageView);
    }

    @Override
    protected void loadData() {
        if(null==mData) return;
        mVidepPlayer.setUp("http://voice.wk2.com/video/2017112405.mp4", XinQuVideoPlayer.SCREEN_WINDOW_LIST,false,"测试");
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.main_bg);
        options.error(R.mipmap.main_bg);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);//缓存源资源和转换后的资源
        options.skipMemoryCache(true);//跳过内存缓存
        Glide.with(mContext).load(mData.getImg()).apply(options).thumbnail(0.1f).into(mIv_lp_logo);
        Glide.with(mContext).load("http://wk2-voice.oss-cn-shenzhen.aliyuncs.com/mp3/2017-11-28/5a1d2677de6d5.jpg").apply(options).thumbnail(0.1f).into(mVidepPlayer.thumbImageView);
        mTv_lp_tips_content.setText(mData.getDesp());
        if(null!=mLpContentListAdapter&&null!=mData&&null!=mData.getExampleInfos()){
            mLpContentListAdapter.setNewData(mData.getExampleInfos());
        }
    }


    @Override
    protected void onRefresh() {
        super.onRefresh();

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView");
    }
}
