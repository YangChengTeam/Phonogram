package com.xinqu.videoplayer.full;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Surface;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.xinqu.videoplayer.full.manager.WindowMediaManager;
import com.xinqu.videoplayer.full.manager.WindowVideoPlayerManager;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Nathen on 2017/11/8.
 * 实现系统的播放引擎
 */
public class WindowMediaSystem extends WindowMediaInterface implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnSeekCompleteListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnVideoSizeChangedListener {

    public KSYMediaPlayer mKsyMdiaPlayer = null;
    public static boolean CURRENT_PLING_LOOP=false;
    private final int RELOAD_CONNECT_COUNT=3;//视频播放错误时候的最大重试次数
    private  int CUREEN_RELOAD_CONNECT_COUNT=0;


    @Override
    public void start() {
        if(null!=mKsyMdiaPlayer){
            mKsyMdiaPlayer.start();
        }
    }

    @Override
    public void prepare() {
        try {
            if(null!=mKsyMdiaPlayer) mKsyMdiaPlayer.release();
            mKsyMdiaPlayer =  new KSYMediaPlayer.Builder(WindowVideoPlayer.getmContext()).build();
            mKsyMdiaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mKsyMdiaPlayer.setOnPreparedListener(WindowMediaSystem.this);
            mKsyMdiaPlayer.setOnCompletionListener(WindowMediaSystem.this);
            mKsyMdiaPlayer.setOnBufferingUpdateListener(WindowMediaSystem.this);
            mKsyMdiaPlayer.setScreenOnWhilePlaying(true);
            mKsyMdiaPlayer.setOnSeekCompleteListener(WindowMediaSystem.this);
            mKsyMdiaPlayer.setOnErrorListener(WindowMediaSystem.this);
            mKsyMdiaPlayer.setOnInfoListener(WindowMediaSystem.this);
            mKsyMdiaPlayer.setOnVideoSizeChangedListener(WindowMediaSystem.this);
            mKsyMdiaPlayer.setLooping(CURRENT_PLING_LOOP);
            Class<KSYMediaPlayer> clazz = KSYMediaPlayer.class;
            Method method = clazz.getDeclaredMethod("setDataSource", String.class, Map.class);
            if (dataSourceObjects.length > 2) {
                method.invoke(mKsyMdiaPlayer, currentDataSource.toString(), dataSourceObjects[2]);
            } else {
                method.invoke(mKsyMdiaPlayer, currentDataSource.toString(), null);
            }
            mKsyMdiaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        if(null!=mKsyMdiaPlayer){
            mKsyMdiaPlayer.pause();
        }
    }

    @Override
    public boolean isPlaying() {
        if(null!=mKsyMdiaPlayer){
            return mKsyMdiaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void seekTo(long time) {
        if(null!=mKsyMdiaPlayer){
            mKsyMdiaPlayer.seekTo((int) time);
        }
    }

    @Override
    public void release() {
        if(null!=mKsyMdiaPlayer){
            mKsyMdiaPlayer.release();
        }
    }

    @Override
    public long getCurrentPosition() {
        if(null!=mKsyMdiaPlayer){
            return mKsyMdiaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public long getDuration() {
        if(null!=mKsyMdiaPlayer){
            return mKsyMdiaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public void setSurface(Surface surface) {
        if(null!=mKsyMdiaPlayer){
            mKsyMdiaPlayer.setSurface(surface);
        }
    }

    @Override
    public void setLoop(boolean flag) {
        Log.d("setLoop","flag="+flag);
        CURRENT_PLING_LOOP=flag;
    }

    @Override
    public void onPrepared(IMediaPlayer mediaPlayer) {
        CUREEN_RELOAD_CONNECT_COUNT=0;
        mediaPlayer.start();
        if (currentDataSource.toString().toLowerCase().contains("mp3")) {
            WindowMediaManager.instance().mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (WindowVideoPlayerManager.getCurrentJzvd() != null) {
                        WindowVideoPlayerManager.getCurrentJzvd().onPrepared();
                    }
                }
            });
        }
    }

    @Override
    public void onCompletion(IMediaPlayer mediaPlayer) {
        WindowMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (WindowVideoPlayerManager.getCurrentJzvd() != null) {
                    WindowVideoPlayerManager.getCurrentJzvd().onAutoCompletion();
                }
            }
        });
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mediaPlayer, final int percent) {
        WindowMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (WindowVideoPlayerManager.getCurrentJzvd() != null) {
                    WindowVideoPlayerManager.getCurrentJzvd().setBufferProgress(percent);
                }
            }
        });
    }

    @Override
    public void onSeekComplete(IMediaPlayer mediaPlayer) {
        WindowMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (WindowVideoPlayerManager.getCurrentJzvd() != null) {
                    WindowVideoPlayerManager.getCurrentJzvd().onSeekComplete();
                }
            }
        });
    }

    @Override
    public boolean onError(IMediaPlayer mediaPlayer, final int what, final int extra) {

        if(null!=mKsyMdiaPlayer&&null!=dataSourceObjects&&dataSourceObjects.length>2&&CUREEN_RELOAD_CONNECT_COUNT<RELOAD_CONNECT_COUNT){
            //播放失败，尝试重试5次
            CUREEN_RELOAD_CONNECT_COUNT++;
            mKsyMdiaPlayer.reload(dataSourceObjects[2].toString(),false);
            //达到错误重连次数之后，显示为失败状态
        }else{
            WindowMediaManager.instance().mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (WindowVideoPlayerManager.getCurrentJzvd() != null) {
                        WindowVideoPlayerManager.getCurrentJzvd().onError(what, extra);
                    }
                }
            });
        }
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer mediaPlayer, final int what, final int extra) {
        WindowMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (WindowVideoPlayerManager.getCurrentJzvd() != null) {
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        WindowVideoPlayerManager.getCurrentJzvd().onPrepared();
                    } else {
                        WindowVideoPlayerManager.getCurrentJzvd().onInfo(what, extra);
                    }
                }
            }
        });
        return false;
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int width, int height, int i2, int i3) {
        WindowMediaManager.instance().currentVideoWidth = width;
        WindowMediaManager.instance().currentVideoHeight = height;
        WindowMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (WindowVideoPlayerManager.getCurrentJzvd() != null) {
                    WindowVideoPlayerManager.getCurrentJzvd().onVideoSizeChanged();
                }
            }
        });
    }
}
