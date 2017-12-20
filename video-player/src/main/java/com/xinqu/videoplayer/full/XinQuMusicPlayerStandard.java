package com.xinqu.videoplayer.full;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.xinqu.videoplayer.R;
import com.xinqu.videoplayer.XinQuUserActionStandard;
import com.xinqu.videoplayer.util.XinQuUtils;

import java.util.Timer;

/**
 * TinyHung@outlook.com
 * 2017/11/9
 * 音乐播放器定制
 */

public class XinQuMusicPlayerStandard extends WindowVideoPlayer {

    protected static Timer DISMISS_CONTROL_VIEW_TIMER;

    private ProgressBar loadingProgressBar;
    private ImageView mBtn_play;
    public ImageView thumbImageView;


    public interface  OnPlayStateListener{
        void onError();
    }

    private OnPlayStateListener mOnPlayStateListener;

    public void setOnPlayStateListener(OnPlayStateListener onPlayStateListener) {
        mOnPlayStateListener = onPlayStateListener;
    }

    //将进度条传给外界设置
    public interface OnVideoPlayerProgressListener{
        void onTouchProgress(int progress);
        void onStateAutoComplete(int progress);
        void onProgressAndText(int progress);
        void onBufferProgress(int progress);
        void onProgressAndTime(int progress);
    }

    private OnVideoPlayerProgressListener mOnVideoPlayerProgressListener;

    public void setOnVideoPlayerProgressListener(OnVideoPlayerProgressListener onVideoPlayerProgressListener) {
        mOnVideoPlayerProgressListener = onVideoPlayerProgressListener;
    }

    public XinQuMusicPlayerStandard(Context context) {
        super(context);
    }

    public XinQuMusicPlayerStandard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        loadingProgressBar = (ProgressBar) findViewById(R.id.loading);
        mBtn_play = (ImageView) findViewById(R.id.btn_play);
        thumbImageView=((ImageView) findViewById(R.id.thumb));
        if(null!=textureViewContainer){
            textureViewContainer.setOnClickListener(null);
            textureViewContainer.setOnTouchListener(null);
        }
    }

    /**
     * 全屏，正常，小窗口
     * @param url
     * @param screen
     * @param objects
     */
    @Override
    public void setUp(String url, int screen, boolean loop,Object... objects) {
        super.setUp(url, screen,loop, objects);
        if (objects.length == 0) return;
        if (currentScreen == SCREEN_WINDOW_TINY) {
            setAllControlsVisible(INVISIBLE, INVISIBLE, INVISIBLE, INVISIBLE, INVISIBLE, INVISIBLE, INVISIBLE);
        }
    }



    @Override
    public int getLayoutId() {
        return R.layout.layout_music_standard;
    }



    @Override
    public void onStateNormal() {
        super.onStateNormal();
        changeUiToNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        changeUiToPreparingShow();
        startDismissControlViewTimer();
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        changeUiToPlayingShow();
        startDismissControlViewTimer();
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        changeUiToPauseShow();
        cancelDismissControlViewTimer();
    }


    @Override
    public void onStateError() {
        super.onStateError();
        changeUiToError();
        if(null!=mOnPlayStateListener){
            mOnPlayStateListener.onError();
        }
    }


    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        changeUiToCompleteShow();
        cancelDismissControlViewTimer();
        if(null!=mOnVideoPlayerProgressListener){
            mOnVideoPlayerProgressListener.onStateAutoComplete(100);
        }
    }


    @Override
    public void showWifiDialog(int action) {
        super.showWifiDialog(action);
        if(!isWifiTips) return;
        try{
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
            builder.setMessage(getResources().getString(R.string.tips_not_wifi));
            builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    XinQuUtils.WIFI_TIP_DIALOG_SHOWED = true;
                    onEvent(XinQuUserActionStandard.ON_CLICK_START_THUMB);
                    startVideo();
                }
            });

            builder.setNegativeButton(getResources().getString(R.string.tips_not_wifi_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                        dialog.dismiss();
                        clearFullscreenLayout();
                    }
                }
            });

            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                    if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                        dialog.dismiss();
                        clearFullscreenLayout();
                    }
                }
            });
            builder.create().show();
        }catch (Exception e){

        }
    }

//    @Override
//    public void setProgressAndText(int progress, int position, int duration) {
//        super.setProgressAndText(progress, position, duration);
//        if (progress != 0) {
//            if(null!=mOnVideoPlayerProgressListener){
//                mOnVideoPlayerProgressListener.onProgressAndText(progress);
//            }
//        }
//    }

    @Override
    public void setBufferProgress(int bufferProgress) {
        super.setBufferProgress(bufferProgress);
        if (bufferProgress != 0){
            if(null!=mOnVideoPlayerProgressListener){
                mOnVideoPlayerProgressListener.onBufferProgress(bufferProgress);
            }
        }
    }

    @Override
    public void resetProgressAndTime() {
        super.resetProgressAndTime();
        if(null!=mOnVideoPlayerProgressListener){
            mOnVideoPlayerProgressListener.onProgressAndTime(0);
        }
    }

    //Unified management Ui
    public void changeUiToNormal() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(VISIBLE, INVISIBLE, VISIBLE,
                        INVISIBLE, VISIBLE, VISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(VISIBLE, INVISIBLE, VISIBLE,
                        INVISIBLE, VISIBLE, VISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }
    }

    public void changeUiToPreparingShow() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(VISIBLE, INVISIBLE, INVISIBLE,
                        VISIBLE, VISIBLE, VISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(VISIBLE, INVISIBLE, INVISIBLE,
                        VISIBLE, VISIBLE, VISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPreparingClear() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(VISIBLE, INVISIBLE, INVISIBLE,
                        VISIBLE, VISIBLE, VISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(VISIBLE, INVISIBLE, INVISIBLE,
                        VISIBLE, VISIBLE, VISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }


    @Override
    protected void startPlayer() {
        super.startPlayer();
        if(null!=mBtn_play){
            mBtn_play.setImageResource(R.drawable.media_record_music_pause);
        }
    }

    //JustPreparedUi
    @Override
    public void onPrepared() {
        super.onPrepared();
        setAllControlsVisible(VISIBLE, INVISIBLE, INVISIBLE,
                INVISIBLE, INVISIBLE, INVISIBLE, VISIBLE);
        startDismissControlViewTimer();
    }

    public void changeUiToPlayingShow() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(VISIBLE, VISIBLE, VISIBLE,
                        INVISIBLE, INVISIBLE, INVISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(VISIBLE, VISIBLE, VISIBLE,
                        INVISIBLE, INVISIBLE, INVISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }



    public void changeUiToPauseShow() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(VISIBLE, VISIBLE, VISIBLE,
                        INVISIBLE, INVISIBLE, INVISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(VISIBLE, VISIBLE, VISIBLE,
                        INVISIBLE, INVISIBLE, INVISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }


    public void changeUiToPlayingBufferingShow() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(VISIBLE, VISIBLE, INVISIBLE,
                        VISIBLE, INVISIBLE, INVISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(VISIBLE, VISIBLE, INVISIBLE,
                        VISIBLE, INVISIBLE, INVISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }


    public void changeUiToCompleteShow() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(VISIBLE, VISIBLE, VISIBLE,
                        INVISIBLE, VISIBLE, INVISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(VISIBLE, VISIBLE, VISIBLE,
                        INVISIBLE, VISIBLE, INVISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }


    public void changeUiToError() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(INVISIBLE, INVISIBLE, VISIBLE,
                        INVISIBLE, INVISIBLE, VISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(INVISIBLE, INVISIBLE, VISIBLE,
                        INVISIBLE, INVISIBLE, VISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void setAllControlsVisible(int topCon, int bottomCon, int startBtn, int loadingPro, int thumbImg, int coverImg, int bottomPro) {
        loadingProgressBar.setVisibility(loadingPro);
        mBtn_play.setVisibility(INVISIBLE ==loadingPro? VISIBLE : INVISIBLE);
        if(VISIBLE==coverImg){
            mBtn_play.setImageResource(R.drawable.media_record_music_play);
        }else{
            mBtn_play.setImageResource(R.drawable.media_record_music_pause);
        }
    }



    @Override
    public void showVolumeDialog(float deltaY, int volumePercent) {
        super.showVolumeDialog(deltaY, volumePercent);
    }

    @Override
    public void dismissVolumeDialog() {
        super.dismissVolumeDialog();
    }


    @Override
    public void showBrightnessDialog(int brightnessPercent) {
        super.showBrightnessDialog(brightnessPercent);
    }

    @Override
    public void dismissBrightnessDialog() {
        super.dismissBrightnessDialog();
    }

    public void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        DISMISS_CONTROL_VIEW_TIMER = new Timer();

    }

    public void cancelDismissControlViewTimer() {
        if (DISMISS_CONTROL_VIEW_TIMER != null) {
            DISMISS_CONTROL_VIEW_TIMER.cancel();
        }
    }


    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        cancelDismissControlViewTimer();
        if(null!=mBtn_play){
            mBtn_play.setImageResource(R.drawable.media_record_music_play);
        }
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
        cancelDismissControlViewTimer();
        if(null!=mBtn_play){
            mBtn_play.setImageResource(R.drawable.media_record_music_play);
        }
    }
}
