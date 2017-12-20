package com.xinqu.videoplayer.full;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.xinqu.videoplayer.R;
import com.xinqu.videoplayer.XinQuUserAction;
import com.xinqu.videoplayer.full.manager.WindowMediaManager;
import com.xinqu.videoplayer.full.manager.WindowVideoPlayerManager;
import com.xinqu.videoplayer.listener.PerfectClickListener;
import com.xinqu.videoplayer.util.XinQuUtils;

/**
 * Created by Nathen
 * On 2016/04/18 16:15
 * 全屏状态下的视频播放
 */
public class WindowVideoPlayerStandard extends WindowVideoPlayer {

    public ProgressBar loadingProgressBar;
    public ImageView thumbImageView;
    public LinearLayout mRetryLayout;
    private ImageView mBtn_start;

    public WindowVideoPlayerStandard(Context context) {
        super(context);
    }

    public WindowVideoPlayerStandard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void onClickTouch() {

        if (currentState == CURRENT_STATE_ERROR) {
            Log.d(TAG, "onClick 点击前播放状态：error");
            startVideo();
            return;
        }
        if (currentState == CURRENT_STATE_PAUSE) {
            Log.d(TAG, "onClick 点击前播放状态：pause");
            goOnPlayOnResume();
            return;
        }
        if (currentState == CURRENT_STATE_PLAYING) {
            Log.d(TAG, "onClick 点击前播放状态：playing");
            goOnPlayOnPause();
            return;
        }
        if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
            onEvent(XinQuUserAction.ON_CLICK_START_AUTO_COMPLETE);
            startVideo();
            return;
        }
        if(currentState==CURRENT_STATE_NORMAL){
            Log.d(TAG, "onClick 点击前播放状态：NORMAL");
            startVideo();
        }
    }


    //将进度条传给外界设置
    public interface OnVideoPlayerProgressListener{
        void onStateAutoComplete(int progress);
        void onProgressAndText(int progress);
        void onBufferProgress(int progress);
        void onProgressAndTime(int progress);
    }

    private OnVideoPlayerProgressListener mOnVideoPlayerProgressListener;

    public void setOnVideoPlayerProgressListener(OnVideoPlayerProgressListener onVideoPlayerProgressListener) {
        mOnVideoPlayerProgressListener = onVideoPlayerProgressListener;
    }


    @Override
    public void init(Context context) {
        super.init(context);
        thumbImageView = findViewById(R.id.thumb);
        loadingProgressBar = findViewById(R.id.loading);
        mRetryLayout = findViewById(R.id.retry_layout);
        mBtn_start = findViewById(R.id.btn_start);

        FrameLayout surface_container = findViewById(R.id.surface_container);

        OnClickListener onClickListener=new OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = v.getId();
                 if (i == R.id.thumb) {
                    if (dataSourceObjects == null || XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex) == null) {
                        Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (currentState == CURRENT_STATE_NORMAL) {
                        if (!XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex).toString().startsWith("file") &&
                                !XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex).toString().startsWith("/") &&
                                !XinQuUtils.isWifiConnected(getContext()) && !XinQuUtils.WIFI_TIP_DIALOG_SHOWED) {
                            showWifiDialog(WindowUserActionStandard.ON_CLICK_START_THUMB);
                            return;
                        }
                        onEvent(WindowUserActionStandard.ON_CLICK_START_THUMB);
                        startVideo();
                    } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {

                    }

                } else if (i == R.id.retry_layout) {
                    if (!XinQuUtils.isWifiConnected(getContext()) && !XinQuUtils.WIFI_TIP_DIALOG_SHOWED) {
                        showWifiDialog(WindowUserAction.ON_CLICK_START_ICON);
                        return;
                    }
                    initTextureView();//和开始播放的代码重复
                    addTextureView();
                    WindowMediaManager.setDataSource(dataSourceObjects);
                    WindowMediaManager.setCurrentDataSource(XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex));
                    onStatePreparing();
                    onEvent(WindowUserAction.ON_CLICK_START_ERROR);
                }
            }
        };

        surface_container.setOnClickListener(onClickListener);
        surface_container.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (currentState == CURRENT_STATE_ERROR) {
                    Log.d(TAG, "onClick 点击前播放状态：error");
                    startVideo();
                    return;
                }
                if (currentState == CURRENT_STATE_PAUSE) {
                    Log.d(TAG, "onClick 点击前播放状态：pause");
                    goOnPlayOnResume();
                    return;
                }
                if (currentState == CURRENT_STATE_PLAYING) {
                    Log.d(TAG, "onClick 点击前播放状态：playing");
                    goOnPlayOnPause();
                    return;
                }
            }
        });

        mBtn_start.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (currentState == CURRENT_STATE_ERROR) {
                    Log.d(TAG, "onClick 点击前播放状态：error");
                    startVideo();
                    return;
                }
                if (currentState == CURRENT_STATE_PAUSE) {
                    Log.d(TAG, "onClick 点击前播放状态：pause");
                    goOnPlayOnResume();
                    return;
                }
                if (currentState == CURRENT_STATE_PLAYING) {
                    Log.d(TAG, "onClick 点击前播放状态：playing");
                    goOnPlayOnPause();
                    return;
                }
                if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
                    onEvent(XinQuUserAction.ON_CLICK_START_AUTO_COMPLETE);
                    startVideo();
                    return;
                }
                if(currentState==CURRENT_STATE_NORMAL){
                    Log.d(TAG, "onClick 点击前播放状态：NORMAL");
                    startVideo();
                }
            }
        });

        thumbImageView.setOnClickListener(onClickListener);
        mRetryLayout.setOnClickListener(onClickListener);
    }

    public void setUp(Object[] dataSourceObjects, int defaultUrlMapIndex, int screen, boolean loop,Object... objects) {
        super.setUp(dataSourceObjects, defaultUrlMapIndex, screen,loop, objects);
        if (currentScreen == SCREEN_WINDOW_TINY) {
            setAllControlsVisiblity(INVISIBLE, INVISIBLE, INVISIBLE, INVISIBLE, INVISIBLE, INVISIBLE, INVISIBLE);
        }
        if (tmp_test_back) {
            tmp_test_back = false;
            WindowVideoPlayerManager.setFirstFloor(this);
            backPress();
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.video_play_full_standard;
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
        changeUiToNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        changeUiToPreparing();
    }

    @Override
    public void onStatePreparingChangingUrl(int urlMapIndex, long seekToInAdvance) {
        super.onStatePreparingChangingUrl(urlMapIndex, seekToInAdvance);
        if(null!=loadingProgressBar){
            loadingProgressBar.setVisibility(VISIBLE);
        }
        if(null!=mBtn_start){
            mBtn_start.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        changeUiToPlayingClear();
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        changeUiToPauseShow();
    }

    @Override
    public void onStateError() {
        super.onStateError();
        changeUiToError();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        changeUiToComplete();
        if(null!=mOnVideoPlayerProgressListener){
            mOnVideoPlayerProgressListener.onStateAutoComplete(100);
        }
    }

    @Override
    public void showWifiDialog(int action) {
        super.showWifiDialog(action);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getString(R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onEvent(WindowUserActionStandard.ON_CLICK_START_THUMB);
                XinQuUtils.WIFI_TIP_DIALOG_SHOWED = true;
                startVideo();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.tips_not_wifi_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
    }

    @Override
    public void setProgressAndText(int progress, long position, long duration) {
        super.setProgressAndText(progress, position, duration);
        if (progress != 0) {
            if(null!=mOnVideoPlayerProgressListener){
                mOnVideoPlayerProgressListener.onProgressAndText(progress);
            }
        }
    }

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

    public void changeUiToNormal() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(VISIBLE, INVISIBLE, VISIBLE,
                        INVISIBLE, VISIBLE, INVISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(VISIBLE, INVISIBLE, VISIBLE,
                        INVISIBLE, VISIBLE, INVISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }
    }

    public void changeUiToPreparing() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(INVISIBLE, INVISIBLE, INVISIBLE,
                        VISIBLE, VISIBLE, INVISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(INVISIBLE, INVISIBLE, INVISIBLE,
                        VISIBLE, VISIBLE, INVISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPlayingClear() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(INVISIBLE, INVISIBLE, INVISIBLE,
                        INVISIBLE, INVISIBLE, VISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(INVISIBLE, INVISIBLE, INVISIBLE,
                        INVISIBLE, INVISIBLE, VISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPauseShow() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(VISIBLE, VISIBLE, VISIBLE, INVISIBLE, INVISIBLE, INVISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(VISIBLE, VISIBLE, VISIBLE,
                        INVISIBLE, INVISIBLE, INVISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }
    }

    public void changeUiToComplete() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisiblity(VISIBLE, INVISIBLE, VISIBLE,
                        INVISIBLE, VISIBLE, INVISIBLE, INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(VISIBLE, INVISIBLE, VISIBLE,
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
                setAllControlsVisiblity(INVISIBLE, INVISIBLE, VISIBLE, INVISIBLE, INVISIBLE, INVISIBLE, VISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisiblity(INVISIBLE, INVISIBLE, VISIBLE, INVISIBLE, INVISIBLE, INVISIBLE, VISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro, int thumbImg, int bottomPro, int retryLayout) {
        mBtn_start.setVisibility(startBtn);
        loadingProgressBar.setVisibility(loadingPro);
        thumbImageView.setVisibility(thumbImg);
        //播放失败了
        if(retryLayout==0x00000000){
            mBtn_start.setVisibility(INVISIBLE);
        }
        mRetryLayout.setVisibility(retryLayout);
        updateStartImage();
    }


    public void updateStartImage() {
        if(currentState == CURRENT_STATE_PLAYING) {
            mBtn_start.setImageResource(R.drawable.jz_click_pause_selector);
        }else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
            mBtn_start.setImageResource(R.drawable.jz_click_replay_selector);
        } else {
            mBtn_start.setImageResource(R.drawable.jz_click_play_selector);
        }
    }

    @Override
    public void showProgressDialog(float deltaX, String seekTime, long seekTimePosition, String totalTime, long totalTimeDuration) {
        super.showProgressDialog(deltaX, seekTime, seekTimePosition, totalTime, totalTimeDuration);
    }

    @Override
    public void dismissProgressDialog() {
        super.dismissProgressDialog();
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

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
    }
}
