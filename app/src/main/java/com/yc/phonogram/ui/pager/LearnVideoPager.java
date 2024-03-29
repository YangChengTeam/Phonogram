package com.yc.phonogram.ui.pager;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.danikula.videocache.HttpProxyCacheServer;

import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.xinqu.videoplayer.XinQuVideoPlayerStandard;
import com.yc.phonogram.App;
import com.yc.phonogram.R;
import com.yc.phonogram.adapter.RecyclerViewLPContentListAdapter;
import com.yc.phonogram.base.BasePager;
import com.yc.phonogram.domain.ExampleInfo;
import com.yc.phonogram.domain.PhonogramInfo;
import com.yc.phonogram.listener.PerfectClickListener;
import com.yc.phonogram.ui.views.holder.LearnRecyclerHolder;
import com.yc.phonogram.ui.views.layout.NoScrollingGridLayoutManager;

import java.io.IOException;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import yc.com.rthttplibrary.util.ToastUtil;

/**
 * TinyHung@Outlook.com
 * 2017/12/19.
 * 首页学音标
 */

public class LearnVideoPager  extends BasePager{

    private final PhonogramInfo mData;
    private ImageView mIvLpLogo;
    private TextView mTvLpTipsContent;
    private XinQuVideoPlayerStandard mVidepPlayer;
    private MediaPlayer mKsyMediaPlayer;
    private Animation mScaleAnimation;
    private AnimationDrawable mAnimationDrawable;
    private RecyclerViewLPContentListAdapter mReContentListAdapter;
    private LearnRecyclerHolder mAttachHolder=null;//当前正在播放的ItemView Holder

    public LearnVideoPager(Activity context, PhonogramInfo phonogramInfo) {
        super(context);
        this.mData=phonogramInfo;
        setContentView(R.layout.pager_learn_child_content);
    }

    @Override
    protected void initViews() {
        if(null==mContext) return;
        //封面
        mIvLpLogo = (ImageView) getView(R.id.iv_lp_logo);
        mTvLpTipsContent = (TextView) getView(R.id.tv_lp_tips_content);
        //点击了音标的说LOGO
        ((ImageView) getView(R.id.iv_lp_tips_logo)).setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onClickView(View v) {
                if(null!=mData&&!TextUtils.isEmpty(mData.getDesp_audio())){
                    startMusic(mData.getDesp_audio());
                }
            }
        });
        mVidepPlayer = (XinQuVideoPlayerStandard) getView(R.id.video_player);
        mVidepPlayer.widthRatio=16;
        mVidepPlayer.heightRatio=9;
        RecyclerView recyclerView = (RecyclerView) getView(R.id.recyclerView);
        recyclerView.setLayoutManager(new NoScrollingGridLayoutManager(getActivity(),2, GridLayoutManager.VERTICAL,false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        mReContentListAdapter = new RecyclerViewLPContentListAdapter(getActivity());
        mReContentListAdapter.setOnItemClickListener(new RecyclerViewLPContentListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(LearnRecyclerHolder holder, int position) {
                if(null!=mReContentListAdapter){
                    List<ExampleInfo> exampleInfos=mReContentListAdapter.getData();
                    if(null!=exampleInfos&&exampleInfos.size()>0){
                        ExampleInfo exampleInfo = exampleInfos.get(position);
                        if(null!=exampleInfo){
                            //http://sc.wk2.com/upload/music/9/2017-11-17/5a0e4b51c34e7.mp3
                            startMusic(exampleInfo.getVideo(),true,holder);
                        }
                    }
                }
            }
        });
        recyclerView.setAdapter(mReContentListAdapter);
    }


    @Override
    protected void loadData() {
        if(null==mData) return;
        if(null!=mReContentListAdapter){
            mReContentListAdapter.setNewData(mData.getExampleInfos(),mData.getName());
        }
        RequestOptions options = new RequestOptions();
        options.error(R.mipmap.ic_player_error);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);//缓存源资源和转换后的资源
        options.skipMemoryCache(true);//跳过内存缓存
        String proxyUrl =mData.getVideo();
        HttpProxyCacheServer proxy = App.getProxy();
        if(null!=proxy){
            proxyUrl= proxy.getProxyUrl(mData.getVideo());
        }
        mVidepPlayer.setUp(proxyUrl, XinQuVideoPlayer.SCREEN_WINDOW_LIST,false,null==mData.getName()?"":mData.getName());
        Glide.with(mContext).load(mData.getImg()).apply(options).thumbnail(0.1f).into(mIvLpLogo);//音标
        Glide.with(mContext).load(mData.getCover()).apply(options).thumbnail(0.1f).into(mVidepPlayer.thumbImageView);//视频播放器封面
        if(null!=mTvLpTipsContent){
            mTvLpTipsContent.setMovementMethod(ScrollingMovementMethod.getInstance());
            mTvLpTipsContent.setText(mData.getDesp());
        }
    }


    private void startMusic(String musicUrl){
        startMusic(musicUrl,false,null);
    }

    /**
     * 开始播放音乐
     * @param musicUrl
     * @param attachHolder 当前Itemd的Holder
     * @param isListPlay 是否是列表播放
     */
    private void startMusic(String musicUrl, final boolean isListPlay, LearnRecyclerHolder attachHolder){
        stopMusic();
        if(null!=attachHolder){
            this.mAttachHolder=attachHolder;
        }
        if(TextUtils.isEmpty(musicUrl)) return;
        if(null==mKsyMediaPlayer){
            mKsyMediaPlayer = new MediaPlayer();
            mKsyMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        mKsyMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if(isListPlay){
                    cleanLoadingAnimation();//清除和隐藏加载进度条
                }
                //开始播放动画，如果当前有动画正在播放
                if(isListPlay&&null!=mAttachHolder){
                    if(null!= mScaleAnimation){
                        mScaleAnimation.reset();
                        mScaleAnimation.cancel();
                        mScaleAnimation =null;
                    }
                    mScaleAnimation = getScaleAnimation();
                    if(null!=mAttachHolder.itemView){
                        mAttachHolder.itemView.startAnimation(mScaleAnimation);
                    }
                }
                //开始播放
                mp.start();
            }
        });
        mKsyMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                cleanLoadingAnimation();//清除和隐藏加载进度条
                ToastUtil.toast(getActivity(),"单词播放失败！");
                return true;
            }
        });
        try {
            String proxyUrl =musicUrl;
            HttpProxyCacheServer proxy = App.getProxy();
            if(null!=proxy){
                proxyUrl= proxy.getProxyUrl(musicUrl);
            }
            mKsyMediaPlayer.setDataSource(proxyUrl);
            mKsyMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(null!=attachHolder&&null!=mAttachHolder&&isListPlay){
            if(null!=mAttachHolder.progressLoad){
                mAttachHolder.progressLoad.setVisibility(View.VISIBLE);
                mAnimationDrawable = (AnimationDrawable) mAttachHolder.progressLoad.getDrawable();
                if(null!=mAnimationDrawable){
                    mAnimationDrawable.start();
                }
            }
        }
    }

    public void stopMusic(){
        //停止ItemView缩放动画播放
        if(null!= mScaleAnimation){
            mScaleAnimation.reset();
            mScaleAnimation.cancel();
            mScaleAnimation =null;
        }
        //停止音乐播放
        if(null!=mKsyMediaPlayer){
            if(mKsyMediaPlayer.isPlaying()){
                mKsyMediaPlayer.stop();
            }
//            mKsyMediaPlayer.release();
            mKsyMediaPlayer.reset();
//            mKsyMediaPlayer.resetListeners();

            mKsyMediaPlayer=null;
        }
        cleanLoadingAnimation();
    }

    /**
     * 清除缓冲进度
     */
    private void cleanLoadingAnimation() {
        if(null!=mAnimationDrawable&&mAnimationDrawable.isRunning()){
            mAnimationDrawable.stop();
        }
        mAnimationDrawable=null;
        //刚才正在播放音标的ItemView
        if(null!=mAttachHolder&&null!=mAttachHolder.progressLoad){
            mAttachHolder.progressLoad.setVisibility(View.GONE);
        }
    }

    private  ScaleAnimation getScaleAnimation(){
        ScaleAnimation followScaleAnimation = new ScaleAnimation(1.0f, 1.6f, 1.0f, 1.6f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        followScaleAnimation.setRepeatCount(0);
        followScaleAnimation.setDuration(1000);
        return followScaleAnimation;
    }


    @Override
    protected void onRefresh() {
        super.onRefresh();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopMusic();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
