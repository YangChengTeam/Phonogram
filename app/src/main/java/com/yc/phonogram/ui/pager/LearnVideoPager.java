package com.yc.phonogram.ui.pager;

import android.app.Activity;
import android.media.AudioManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.danikula.videocache.HttpProxyCacheServer;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.xinqu.videoplayer.XinQuVideoPlayerStandard;
import com.yc.phonogram.App;
import com.yc.phonogram.R;
import com.yc.phonogram.adapter.LPContentListAdapter;
import com.yc.phonogram.base.BasePager;
import com.yc.phonogram.domain.ExampleInfo;
import com.yc.phonogram.domain.PhonogramInfo;
import java.io.IOException;
import java.util.List;

/**
 * TinyHung@Outlook.com
 * 2017/12/19.
 * 首页学音标
 */

public class LearnVideoPager  extends BasePager{

    private static final String TAG = LearnVideoPager.class.getSimpleName();
    private final PhonogramInfo mData;
    private LPContentListAdapter mLpContentListAdapter;
    private ImageView mIvLpLogo;
    private TextView mTvLpTipsContent;
    private XinQuVideoPlayerStandard mVidepPlayer;
    private KSYMediaPlayer mKsyMediaPlayer;
    private Animation mInputAnimation;


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
        mTvLpTipsContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        mVidepPlayer = (XinQuVideoPlayerStandard) getView(R.id.video_player);
        mVidepPlayer.widthRatio=16;
        mVidepPlayer.heightRatio=9;
        mLpContentListAdapter = new LPContentListAdapter(mContext,null);
        GridView gridView = (GridView) getView(R.id.grid_view);
        gridView.setAdapter(mLpContentListAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(null!=mLpContentListAdapter){
                    List<ExampleInfo> exampleInfos=mLpContentListAdapter.getData();
                    if(null!=exampleInfos&&exampleInfos.size()>0){
                        ExampleInfo exampleInfo = exampleInfos.get(position);
                        if(null!=exampleInfo){
                            //http://sc.wk2.com/upload/music/9/2017-11-17/5a0e4b51c34e7.mp3
                            startMusic("http://sc.wk2.com/upload/music/9/2017-11-17/5a0e4b51c34e7.mp3",view);
                        }
                    }
                }
            }
        });
    }



    @Override
    protected void loadData() {
        if(null==mData) return;
        mVidepPlayer.setUp(mData.getVideo(), XinQuVideoPlayer.SCREEN_WINDOW_LIST,false,mData.getName());
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_player_error);
        options.error(R.mipmap.ic_player_error);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);//缓存源资源和转换后的资源
        options.skipMemoryCache(true);//跳过内存缓存

        Glide.with(mContext).load(mData.getCover()).apply(options).thumbnail(0.1f).into(mVidepPlayer.thumbImageView);
        String proxyUrl =mData.getVideo();
        HttpProxyCacheServer proxy = App.getProxy();
        if(null!=proxy){
            proxyUrl= proxy.getProxyUrl(mData.getVideo());
        }
        mVidepPlayer.setUp(proxyUrl, XinQuVideoPlayer.SCREEN_WINDOW_LIST,false,null==mData.getName()?"":mData.getName());
        Glide.with(mContext).load(mData.getImg()).apply(options).thumbnail(0.1f).into(mIvLpLogo);//音标
        Glide.with(mContext).load(mData.getCover()).apply(options).thumbnail(0.1f).into(mVidepPlayer.thumbImageView);//视频播放器封面
        mTvLpTipsContent.setText(mData.getDesp());
        if(null!=mLpContentListAdapter&&null!=mData.getExampleInfos()){
            mLpContentListAdapter.setNewData(mData.getExampleInfos(),mData.getName());
        }
    }


    private void startMusic(String musicUrl,View attachView){
        stopMusic();
        if(null==mKsyMediaPlayer){
            mKsyMediaPlayer = new KSYMediaPlayer.Builder(getActivity()).build();
            mKsyMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        mKsyMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                mp.start();
            }
        });
        mKsyMediaPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {

            }
        });
        try {
            mKsyMediaPlayer.setDataSource(musicUrl);
            mKsyMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(null!=attachView){
            if(null!=mInputAnimation){
                mInputAnimation.reset();
                mInputAnimation.cancel();
                mInputAnimation=null;
            }
            mInputAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.view_shake);
            attachView.startAnimation(mInputAnimation);
        }
    }

    public void stopMusic(){
        if(null!=mKsyMediaPlayer){
            if(mKsyMediaPlayer.isPlaying()){
                mKsyMediaPlayer.stop();
            }
            mKsyMediaPlayer.release();
            mKsyMediaPlayer.reset();
            mKsyMediaPlayer=null;
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
        stopMusic();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView");
        stopMusic();
    }
}
