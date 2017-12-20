package com.xinqu.videoplayer.manager;

import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import com.xinqu.videoplayer.XinQuMediaInterface;
import com.xinqu.videoplayer.XinQuMediaSystem;
import com.xinqu.videoplayer.XinQuResizeTextureView;

/**
 * 这个类用来和jzvd互相调用，当jzvd需要调用Media的时候调用这个类，当MediaPlayer有回调的时候，通过这个类回调JZVD
 * Created by Nathen on 2017/11/18.
 */
public class XinQuMediaManager implements TextureView.SurfaceTextureListener {

    public static final String TAG = "JiaoZiVideoPlayer";
    public static final int HANDLER_PREPARE = 0;
    public static final int HANDLER_RELEASE = 2;

    public static XinQuResizeTextureView textureView;
    public static SurfaceTexture savedSurfaceTexture;
    public static Surface surface;
    public static XinQuMediaManager mediaManager;
    public int positionInList = -1;
    public XinQuMediaInterface mXinQuMediaInterface;
    public int currentVideoWidth = 0;
    public int currentVideoHeight = 0;

    public HandlerThread mMediaHandlerThread;
    public MediaHandler mMediaHandler;
    public Handler mainThreadHandler;

    public XinQuMediaManager() {
        mMediaHandlerThread = new HandlerThread(TAG);
        mMediaHandlerThread.start();
        mMediaHandler = new MediaHandler(mMediaHandlerThread.getLooper());
        mainThreadHandler = new Handler();
        if (mXinQuMediaInterface == null)
            mXinQuMediaInterface = new XinQuMediaSystem();
    }

    public static XinQuMediaManager instance() {
        if (mediaManager == null) {
            mediaManager = new XinQuMediaManager();
        }
        return mediaManager;
    }

    public static Object[] getDataSource() {
        if(null!=instance().mXinQuMediaInterface){
            return instance().mXinQuMediaInterface.dataSourceObjects;
        }
        return null;
    }

    //这几个方法是不是多余了，为了不让其他地方动MediaInterface的方法
    public static void setDataSource(Object[] dataSourceObjects) {
        if(null!=instance().mXinQuMediaInterface.dataSourceObjects){
            instance().mXinQuMediaInterface.dataSourceObjects = dataSourceObjects;
        }
    }

    //正在播放的url或者uri
    public static Object getCurrentDataSource() {
        if(null!= instance().mXinQuMediaInterface){
            return instance().mXinQuMediaInterface.currentDataSource;
        }
        return null;
    }

    public static void setCurrentDataSource(Object currentDataSource) {
        if(null!=instance().mXinQuMediaInterface){
            instance().mXinQuMediaInterface.currentDataSource = currentDataSource;
        }
    }

    public static long getCurrentPosition() {
        if(null!=instance().mXinQuMediaInterface){
            return instance().mXinQuMediaInterface.getCurrentPosition();
        }
        return 0;
    }

    public static long getDuration() {
        if(null!=instance().mXinQuMediaInterface){
            return instance().mXinQuMediaInterface.getDuration();
        }
        return 0;
    }

    public static void seekTo(long time) {
        if(null!=instance().mXinQuMediaInterface){
            instance().mXinQuMediaInterface.seekTo(time);
        }
    }

    public static void pause() {
        if(null!=instance().mXinQuMediaInterface){
            instance().mXinQuMediaInterface.pause();
        }
    }

    public static void start() {
        if(null!=instance().mXinQuMediaInterface){
            instance().mXinQuMediaInterface.start();
        }
    }

    public static void setLoop(boolean flag) {
        if(null!=instance().mXinQuMediaInterface){
            instance().mXinQuMediaInterface.setLoop(flag);
        }
    }


    public static boolean isPlaying() {
        if(null!=instance().mXinQuMediaInterface){
            return instance().mXinQuMediaInterface.isPlaying();
        }
        return false;
    }

    public void releaseMediaPlayer() {
        Message msg = new Message();
        msg.what = HANDLER_RELEASE;
        mMediaHandler.sendMessage(msg);
    }

    public void prepare() {
        releaseMediaPlayer();
        Message msg = new Message();
        msg.what = HANDLER_PREPARE;
        mMediaHandler.sendMessage(msg);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.i(TAG, "onSurfaceTextureAvailable [" + XinQuVideoPlayerManager.getCurrentJzvd().hashCode() + "] ");
        if (savedSurfaceTexture == null) {
            savedSurfaceTexture = surfaceTexture;
            prepare();
        } else {
            textureView.setSurfaceTexture(savedSurfaceTexture);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return savedSurfaceTexture == null;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }



    public class MediaHandler extends Handler {
        public MediaHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_PREPARE:
                    currentVideoWidth = 0;
                    currentVideoHeight = 0;
                    if(null!=mXinQuMediaInterface){
                        mXinQuMediaInterface.prepare();
                    }
                    if (surface != null) {
                        surface.release();
                    }
                    if(null!=savedSurfaceTexture){
                        surface = new Surface(savedSurfaceTexture);
                        if(null!=mXinQuMediaInterface){
                            mXinQuMediaInterface.setSurface(surface);
                        }
                    }
                    break;
                case HANDLER_RELEASE:
                    if(null!=mXinQuMediaInterface){
                        mXinQuMediaInterface.release();
                    }
                    break;
            }
        }
    }
}
