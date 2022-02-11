package com.xinqu.videoplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Surface;

import com.xinqu.videoplayer.manager.XinQuMediaManager;
import com.xinqu.videoplayer.manager.XinQuVideoPlayerManager;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Nathen on 2017/11/8.
 * 实现系统的播放引擎
 */

public class XinQuMediaSystem extends XinQuMediaInterface implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {

    //    public KSYMediaPlayer mKsyMdiaPlayer = null;
    public MediaPlayer mKsyMdiaPlayer;
    public static boolean CURRENT_PLING_LOOP;
    private final int RELOAD_CONNECT_COUNT = 3;//视频播放错误时候的最大重试次数
    private int CUREEN_RELOAD_CONNECT_COUNT = 0;


    @Override
    public void start() {
        if (null != mKsyMdiaPlayer) {
            mKsyMdiaPlayer.start();
        }
    }

    @Override
    public void prepare() {
        try {
            if (null != mKsyMdiaPlayer) mKsyMdiaPlayer.release();
            mKsyMdiaPlayer = new MediaPlayer();
            mKsyMdiaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mKsyMdiaPlayer.setOnPreparedListener(XinQuMediaSystem.this);
            mKsyMdiaPlayer.setOnCompletionListener(XinQuMediaSystem.this);
            mKsyMdiaPlayer.setOnBufferingUpdateListener(XinQuMediaSystem.this);
            mKsyMdiaPlayer.setScreenOnWhilePlaying(true);
            mKsyMdiaPlayer.setOnSeekCompleteListener(XinQuMediaSystem.this);
            mKsyMdiaPlayer.setOnErrorListener(XinQuMediaSystem.this);
            mKsyMdiaPlayer.setOnInfoListener(XinQuMediaSystem.this);
            mKsyMdiaPlayer.setOnVideoSizeChangedListener(XinQuMediaSystem.this);
            mKsyMdiaPlayer.setLooping(CURRENT_PLING_LOOP);
            Class<MediaPlayer> clazz = MediaPlayer.class;
            Method method = clazz.getDeclaredMethod("setDataSource", String.class, Map.class);
            if (null != dataSourceObjects && dataSourceObjects.length > 2) {
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
        if (null != mKsyMdiaPlayer) {
            mKsyMdiaPlayer.pause();
        }
    }

    @Override
    public boolean isPlaying() {
        if (null != mKsyMdiaPlayer) {
            return mKsyMdiaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void seekTo(long time) {
        if (null != mKsyMdiaPlayer) {
            mKsyMdiaPlayer.seekTo((int) time);
        }
    }

    @Override
    public void release() {
        if (null != mKsyMdiaPlayer) {
            mKsyMdiaPlayer.release();
        }
    }

    @Override
    public long getCurrentPosition() {
        if (null != mKsyMdiaPlayer) {
            return mKsyMdiaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public long getDuration() {
        if (null != mKsyMdiaPlayer) {
            return mKsyMdiaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public void setSurface(Surface surface) {
        if (null != mKsyMdiaPlayer) {
            mKsyMdiaPlayer.setSurface(surface);
        }
    }

    @Override
    public void setLoop(boolean flag) {
        this.CURRENT_PLING_LOOP = flag;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        CUREEN_RELOAD_CONNECT_COUNT = 0;
        mediaPlayer.start();
        if (currentDataSource.toString().toLowerCase().contains("mp3")) {
            XinQuMediaManager.instance().mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (XinQuVideoPlayerManager.getCurrentJzvd() != null) {
                        XinQuVideoPlayerManager.getCurrentJzvd().onPrepared();
                    }
                }
            });
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        XinQuMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (XinQuVideoPlayerManager.getCurrentJzvd() != null) {
                    XinQuVideoPlayerManager.getCurrentJzvd().onAutoCompletion();
                }
            }
        });
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, final int percent) {
        XinQuMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (XinQuVideoPlayerManager.getCurrentJzvd() != null) {
                    XinQuVideoPlayerManager.getCurrentJzvd().setBufferProgress(percent);
                }
            }
        });
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        XinQuMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (XinQuVideoPlayerManager.getCurrentJzvd() != null) {
                    XinQuVideoPlayerManager.getCurrentJzvd().onSeekComplete();
                }
            }
        });
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, final int what, final int extra) {
        if (null != mKsyMdiaPlayer && null != dataSourceObjects && dataSourceObjects.length > 2 && CUREEN_RELOAD_CONNECT_COUNT < RELOAD_CONNECT_COUNT) {
            //播放失败，尝试重试5次
            CUREEN_RELOAD_CONNECT_COUNT++;
//            mKsyMdiaPlayer.reload(dataSourceObjects[2].toString(), false);
            //达到错误重连次数之后，显示为失败状态
        } else {
            XinQuMediaManager.instance().mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (XinQuVideoPlayerManager.getCurrentJzvd() != null) {
                        XinQuVideoPlayerManager.getCurrentJzvd().onError(what, extra);
                    }
                }
            });
        }
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, final int what, final int extra) {
        XinQuMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (XinQuVideoPlayerManager.getCurrentJzvd() != null) {
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        XinQuVideoPlayerManager.getCurrentJzvd().onPrepared();
                    } else {
                        XinQuVideoPlayerManager.getCurrentJzvd().onInfo(what, extra);
                    }
                }
            }
        });
        return false;
    }


    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        XinQuMediaManager.instance().currentVideoWidth = width;
        XinQuMediaManager.instance().currentVideoHeight = height;
        XinQuMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (XinQuVideoPlayerManager.getCurrentJzvd() != null) {
                    XinQuVideoPlayerManager.getCurrentJzvd().onVideoSizeChanged();
                }
            }
        });
    }
}
