package com.xinqu.videoplayer.full;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.xinqu.videoplayer.R;
import com.xinqu.videoplayer.full.manager.WindowMediaManager;
import com.xinqu.videoplayer.full.manager.WindowVideoPlayerManager;
import com.xinqu.videoplayer.util.XinQuUtils;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nathen on 16/7/30.
 * 全屏幕的样式
 */
public abstract class WindowVideoPlayer extends FrameLayout implements  SeekBar.OnSeekBarChangeListener {

    public static final String TAG = WindowVideoPlayer.class.getSimpleName();
    public static final int THRESHOLD = 80;
    public static final int FULL_SCREEN_NORMAL_DELAY = 300;
    public static final int SCREEN_WINDOW_NORMAL = 0;
    public static final int SCREEN_WINDOW_LIST = 1;
    public static final int SCREEN_WINDOW_FULLSCREEN = 2;
    public static final int SCREEN_WINDOW_TINY = 3;
    public static final int CURRENT_STATE_NORMAL = 0;
    public static final int CURRENT_STATE_PREPARING = 1;
    public static final int CURRENT_STATE_PREPARING_CHANGING_URL = 2;
    public static final int CURRENT_STATE_PLAYING = 3;
    public static final int CURRENT_STATE_PAUSE = 5;
    public static final int CURRENT_STATE_AUTO_COMPLETE = 6;
    public static final int CURRENT_STATE_ERROR = 7;
    public static final String URL_KEY_DEFAULT = "URL_KEY_DEFAULT";//当播放的地址只有一个的时候的key
    public static final int VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER = 0;//default
    public static final int VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT = 1;
    public static final int VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP = 2;
    public static final int VIDEO_IMAGE_DISPLAY_TYPE_ORIGINAL = 3;
    public static boolean ACTION_BAR_EXIST = true;
    public static boolean TOOL_BAR_EXIST = true;
    public static int FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
    public static int NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    public static boolean SAVE_PROGRESS = true;
    public static int VIDEO_IMAGE_DISPLAY_TYPE = 0;
    public static long CLICK_QUIT_FULLSCREEN_TIME = 0;
    public static long lastAutoFullscreenTime = 0;
    public  static boolean loop = false;//是否循环播放
    public static boolean isWifiTips=true;
    protected static WindowUserAction JZ_USER_EVENT;
    protected static Timer UPDATE_PROGRESS_TIMER;
    private static Context mContext;
    public int currentState = -1;
    public int currentScreen = -1;
    public Object[] objects = null;
    public long seekToInAdvance = 0;
    public ViewGroup textureViewContainer;
    public int widthRatio = 0;
    public int heightRatio = 0;
    public Object[] dataSourceObjects;//这个参数原封不动直接通过XinQuMeidaManager传给WindowMediaInterface。
    public int currentUrlMapIndex = 0;
    public int positionInList = -1;
    public int videoRotation = 0;
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected AudioManager mAudioManager;
    protected Handler mHandler;
    protected ProgressTimerTask mProgressTimerTask;
    boolean tmp_test_back = false;



    public static AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {//是否新建个class，代码更规矩，并且变量的位置也很尴尬
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    releaseAllVideos();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    try {
                        if (//WindowMediaManager.instance().mediaPlayer != null &&
                                WindowMediaManager.isPlaying()) {
                            WindowMediaManager.pause();
                        }
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    break;
            }
        }
    };


    public static Context getmContext() {
        return mContext;
    }



    public WindowVideoPlayer(Context context) {
        super(context);
        init(context);
    }

    public WindowVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public static void releaseAllVideos() {
        if ((System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) > FULL_SCREEN_NORMAL_DELAY) {
            Log.d(TAG, "releaseAllVideos");
            WindowVideoPlayerManager.completeAll();
            WindowMediaManager.instance().positionInList = -1;
            WindowMediaManager.instance().releaseMediaPlayer();
        }
    }

    public static void startFullscreen(Context context, Class _class, String url, Object... objects) {
        LinkedHashMap map = new LinkedHashMap();
        map.put(URL_KEY_DEFAULT, url);
        Object[] dataSourceObjects = new Object[1];
        dataSourceObjects[0] = map;
        startFullscreen(context, _class, dataSourceObjects, 0, objects);
    }

    public static void startFullscreen(Context context, Class _class, Object[] dataSourceObjects, int defaultUrlMapIndex, Object... objects) {
        hideSupportActionBar(context);
        XinQuUtils.setRequestedOrientation(context, FULLSCREEN_ORIENTATION);
        ViewGroup vp = (ViewGroup) (XinQuUtils.scanForActivity(context)).getWindow().getDecorView();//.findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(R.id.xinqu_fullscreen_id);
        if (old != null) {
            vp.removeView(old);
        }
        try {
            Constructor<WindowVideoPlayer> constructor = _class.getConstructor(Context.class);
            final WindowVideoPlayer xinQuVideoPlayer = constructor.newInstance(context);
            xinQuVideoPlayer.setId(R.id.xinqu_fullscreen_id);
            LayoutParams lp = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            vp.addView(xinQuVideoPlayer, lp);
//            final Animation ra = AnimationUtils.loadAnimation(context, R.anim.start_fullscreen);
//            jzVideoPlayer.setAnimation(ra);
            xinQuVideoPlayer.setUp(dataSourceObjects, defaultUrlMapIndex, SCREEN_WINDOW_FULLSCREEN, loop,objects);
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean backPress() {
        Log.i(TAG, "backPress");
        if ((System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) < FULL_SCREEN_NORMAL_DELAY)
            return false;
        if (WindowVideoPlayerManager.getSecondFloor() != null) {
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            if (XinQuUtils.dataSourceObjectsContainsUri(WindowVideoPlayerManager.getFirstFloor().dataSourceObjects, WindowMediaManager.getCurrentDataSource())) {
                WindowVideoPlayer xinQuVideoPlayer = WindowVideoPlayerManager.getSecondFloor();
                xinQuVideoPlayer.onEvent(xinQuVideoPlayer.currentScreen == SCREEN_WINDOW_FULLSCREEN ?
                        WindowUserAction.ON_QUIT_FULLSCREEN :
                        WindowUserAction.ON_QUIT_TINYSCREEN);
                WindowVideoPlayerManager.getFirstFloor().playOnThisJzvd();
            } else {
                quitFullscreenOrTinyWindow();
            }
            return true;
        } else if (WindowVideoPlayerManager.getFirstFloor() != null &&
                (WindowVideoPlayerManager.getFirstFloor().currentScreen == SCREEN_WINDOW_FULLSCREEN ||
                        WindowVideoPlayerManager.getFirstFloor().currentScreen == SCREEN_WINDOW_TINY)) {//以前我总想把这两个判断写到一起，这分明是两个独立是逻辑
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            quitFullscreenOrTinyWindow();
            return true;
        }
        return false;
    }

    public static void quitFullscreenOrTinyWindow() {
        //直接退出全屏和小窗
        WindowVideoPlayerManager.getFirstFloor().clearFloatScreen();
        WindowMediaManager.instance().releaseMediaPlayer();
        WindowVideoPlayerManager.completeAll();
    }

    @SuppressLint("RestrictedApi")
    public static void showSupportActionBar(Context context) {
        if (ACTION_BAR_EXIST && XinQuUtils.getAppCompActivity(context) != null) {
            ActionBar ab = XinQuUtils.getAppCompActivity(context).getSupportActionBar();
            if (ab != null) {
                ab.setShowHideAnimationEnabled(false);
                ab.show();
            }
        }
        if (TOOL_BAR_EXIST) {
            XinQuUtils.getWindow(context).clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @SuppressLint("RestrictedApi")
    public static void hideSupportActionBar(Context context) {
        if (ACTION_BAR_EXIST && XinQuUtils.getAppCompActivity(context) != null) {
            ActionBar ab = XinQuUtils.getAppCompActivity(context).getSupportActionBar();
            if (ab != null) {
                ab.setShowHideAnimationEnabled(false);
                ab.hide();
            }
        }
        if (TOOL_BAR_EXIST) {
            XinQuUtils.getWindow(context).setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public static void clearSavedProgress(Context context, String url) {
        XinQuUtils.clearSavedProgress(context, url);
    }

    public static void setJzUserAction(WindowUserAction jzUserEvent) {
        JZ_USER_EVENT = jzUserEvent;
    }

    public static void goOnPlayOnResume() {
        if (WindowVideoPlayerManager.getCurrentJzvd() != null) {
            WindowVideoPlayer jzvd = WindowVideoPlayerManager.getCurrentJzvd();
            if (jzvd.currentState == WindowVideoPlayer.CURRENT_STATE_PAUSE) {
                jzvd.onStatePlaying();
                WindowMediaManager.start();
            }
        }
    }

    public static void goOnPlayOnPause() {
        if (WindowVideoPlayerManager.getCurrentJzvd() != null) {
            WindowVideoPlayer jzvd = WindowVideoPlayerManager.getCurrentJzvd();
            if (jzvd.currentState == WindowVideoPlayer.CURRENT_STATE_AUTO_COMPLETE ||
                    jzvd.currentState == WindowVideoPlayer.CURRENT_STATE_NORMAL) {
//                JZVideoPlayer.releaseAllVideos();
            } else {
                jzvd.onStatePause();
                WindowMediaManager.pause();
            }
        }
    }

    public static void onScrollAutoTiny(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastVisibleItem = firstVisibleItem + visibleItemCount;
        int currentPlayPosition = WindowMediaManager.instance().positionInList;
        if (currentPlayPosition >= 0) {
            if ((currentPlayPosition < firstVisibleItem || currentPlayPosition > (lastVisibleItem - 1))) {
                if (WindowVideoPlayerManager.getCurrentJzvd() != null &&
                        WindowVideoPlayerManager.getCurrentJzvd().currentScreen != WindowVideoPlayer.SCREEN_WINDOW_TINY) {
                    if (WindowVideoPlayerManager.getCurrentJzvd().currentState == WindowVideoPlayer.CURRENT_STATE_PAUSE) {
                        WindowVideoPlayer.releaseAllVideos();
                    } else {
                        Log.e(TAG, "onScroll: out screen");
                        WindowVideoPlayerManager.getCurrentJzvd().startWindowTiny();
                    }
                }
            } else {
                if (WindowVideoPlayerManager.getCurrentJzvd() != null &&
                        WindowVideoPlayerManager.getCurrentJzvd().currentScreen == WindowVideoPlayer.SCREEN_WINDOW_TINY) {
                    Log.e(TAG, "onScroll: into screen");
                    WindowVideoPlayer.backPress();
                }
            }
        }
    }

    public static void onScrollReleaseAllVideos(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastVisibleItem = firstVisibleItem + visibleItemCount;
        int currentPlayPosition = WindowMediaManager.instance().positionInList;
        if (currentPlayPosition >= 0) {
            if ((currentPlayPosition < firstVisibleItem || currentPlayPosition > (lastVisibleItem - 1))) {
                WindowVideoPlayer.releaseAllVideos();
            }
        }
    }

    public static void onChildViewAttachedToWindow(View view, int jzvdId) {
        if (WindowVideoPlayerManager.getCurrentJzvd() != null && WindowVideoPlayerManager.getCurrentJzvd().currentScreen == WindowVideoPlayer.SCREEN_WINDOW_TINY) {
            WindowVideoPlayer videoPlayer = view.findViewById(jzvdId);
            if (videoPlayer != null && XinQuUtils.getCurrentFromDataSource(videoPlayer.dataSourceObjects, videoPlayer.currentUrlMapIndex).equals(WindowMediaManager.getCurrentDataSource())) {
                WindowVideoPlayer.backPress();
            }
        }
    }

    public static void onChildViewDetachedFromWindow(View view) {
        if (WindowVideoPlayerManager.getCurrentJzvd() != null && WindowVideoPlayerManager.getCurrentJzvd().currentScreen != WindowVideoPlayer.SCREEN_WINDOW_TINY) {
            WindowVideoPlayer videoPlayer = WindowVideoPlayerManager.getCurrentJzvd();
            if (((ViewGroup) view).indexOfChild(videoPlayer) != -1) {
                if (videoPlayer.currentState == WindowVideoPlayer.CURRENT_STATE_PAUSE) {
                    WindowVideoPlayer.releaseAllVideos();
                } else {
                    videoPlayer.startWindowTiny();
                }
            }
        }
    }

    public static void setTextureViewRotation(int rotation) {
        if (WindowMediaManager.textureView != null) {
            WindowMediaManager.textureView.setRotation(rotation);
        }
    }

    public static void setVideoImageDisplayType(int type) {
        WindowVideoPlayer.VIDEO_IMAGE_DISPLAY_TYPE = type;
        if (WindowMediaManager.textureView != null) {
            WindowMediaManager.textureView.requestLayout();
        }
    }

    public Object getCurrentUrl() {
        return XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex);
    }

    public abstract int getLayoutId();

    public void init(Context context) {
        mContext=context;
        View.inflate(context, getLayoutId(), this);
        textureViewContainer = findViewById(R.id.surface_container);
        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mHandler = new Handler();
        try {
            if (isCurrentPlay()) {
                NORMAL_ORIENTATION = ((AppCompatActivity) context).getRequestedOrientation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUp(String url, int screen, boolean loop,Object... objects) {
        LinkedHashMap map = new LinkedHashMap();
        map.put(URL_KEY_DEFAULT, url);
        Object[] dataSourceObjects = new Object[1];
        dataSourceObjects[0] = map;
        setUp(dataSourceObjects, 0, screen, loop,objects);
    }

    public void setUp(Object[] dataSourceObjects, int defaultUrlMapIndex, int screen, boolean loop,Object... objects) {
        if (this.dataSourceObjects != null && XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex) != null &&
                XinQuUtils.getCurrentFromDataSource(this.dataSourceObjects, currentUrlMapIndex).equals(XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex))) {
            return;
        }
        if (isCurrentJZVD() && XinQuUtils.dataSourceObjectsContainsUri(dataSourceObjects, WindowMediaManager.getCurrentDataSource())) {
            WindowMediaManager.instance().releaseMediaPlayer();
        } else if (isCurrentJZVD() && !XinQuUtils.dataSourceObjectsContainsUri(dataSourceObjects, WindowMediaManager.getCurrentDataSource())) {
            startWindowTiny();
        } else if (!isCurrentJZVD() && XinQuUtils.dataSourceObjectsContainsUri(dataSourceObjects, WindowMediaManager.getCurrentDataSource())) {
            if (WindowVideoPlayerManager.getCurrentJzvd() != null &&
                    WindowVideoPlayerManager.getCurrentJzvd().currentScreen == WindowVideoPlayer.SCREEN_WINDOW_TINY) {
                //需要退出小窗退到我这里，我这里是第一层级
                tmp_test_back = true;
            }
            Log.d(TAG,"tmp_test_back");
        }
        Log.d(TAG,"onStateNormal");
        this.dataSourceObjects = dataSourceObjects;
        this.currentUrlMapIndex = defaultUrlMapIndex;
        this.currentScreen = screen;
        this.loop=loop;
        this.objects = objects;
        onStateNormal();
    }


    public void startVideo() {
        WindowVideoPlayerManager.completeAll();
        //如果设置为提示并且未提示过并且非WIFI并且有网络
        if(isWifiTips&&!XinQuUtils.WIFI_TIP_DIALOG_SHOWED&&!XinQuUtils.isWifiConnected(getContext())&&XinQuUtils.isCheckNetwork(getContext())){
            showWifiDialog(WindowUserActionStandard.ON_CLICK_START_ICON);
            return;
        }
        initTextureView();
        addTextureView();
        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        XinQuUtils.scanForActivity(getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowMediaManager.setLoop(loop);
        WindowMediaManager.setDataSource(dataSourceObjects);
        WindowMediaManager.setCurrentDataSource(XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex));
        WindowMediaManager.instance().positionInList = positionInList;
        onStatePreparing();
        startPlayer();
        WindowVideoPlayerManager.setFirstFloor(this);
    }


    protected void startPlayer(){

    }



    public void onPrepared() {
        Log.i(TAG, "onPrepared " + " [" + this.hashCode() + "] ");
        onStatePrepared();
        onStatePlaying();
        if(null!=mOnPlayerCallBackListener){
            mOnPlayerCallBackListener.startPlayCallBack();
        }
    }

    public void setState(int state) {
        setState(state, 0, 0);
    }

    public void setState(int state, int urlMapIndex, int seekToInAdvance) {
        switch (state) {
            case CURRENT_STATE_NORMAL:
                onStateNormal();
                break;
            case CURRENT_STATE_PREPARING:
                onStatePreparing();
                break;
            case CURRENT_STATE_PREPARING_CHANGING_URL:
                onStatePreparingChangingUrl(urlMapIndex, seekToInAdvance);
                break;
            case CURRENT_STATE_PLAYING:
                onStatePlaying();
                break;
            case CURRENT_STATE_PAUSE:
                onStatePause();
                break;
            case CURRENT_STATE_ERROR:
                onStateError();
                break;
            case CURRENT_STATE_AUTO_COMPLETE:
                onStateAutoComplete();
                break;
        }
    }

    public void onStateNormal() {
        Log.i(TAG, "onStateNormal " + " [" + this.hashCode() + "] ");
        currentState = CURRENT_STATE_NORMAL;
        cancelProgressTimer();
    }

    public void onStatePreparing() {
        Log.i(TAG, "onStatePreparing " + " [" + this.hashCode() + "] ");
        currentState = CURRENT_STATE_PREPARING;
        resetProgressAndTime();
    }

    public void onStatePreparingChangingUrl(int urlMapIndex, long seekToInAdvance) {
        currentState = CURRENT_STATE_PREPARING_CHANGING_URL;
        this.currentUrlMapIndex = urlMapIndex;
        this.seekToInAdvance = seekToInAdvance;
        WindowMediaManager.setDataSource(dataSourceObjects);
        WindowMediaManager.setCurrentDataSource(XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex));
        WindowMediaManager.instance().prepare();
    }

    public void onStatePrepared() {//因为这个紧接着就会进入播放状态，所以不设置state
        if (seekToInAdvance != 0) {
            WindowMediaManager.seekTo(seekToInAdvance);
            seekToInAdvance = 0;
        }
//        else {
//            long position = XinQuUtils.getSavedProgress(getContext(), XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex));
//            if (position != 0) {
//                WindowMediaManager.seekTo(position);
//            }
//        }
    }

    public void onStatePlaying() {
        Log.i(TAG, "onStatePlaying " + " [" + this.hashCode() + "] ");
        currentState = CURRENT_STATE_PLAYING;
        startProgressTimer();
    }

    public void onStatePause() {
        Log.i(TAG, "onStatePause " + " [" + this.hashCode() + "] ");
        currentState = CURRENT_STATE_PAUSE;
        startProgressTimer();
    }

    public void onStateError() {
        Log.i(TAG, "onStateError " + " [" + this.hashCode() + "] ");
        currentState = CURRENT_STATE_ERROR;
        cancelProgressTimer();
    }

    public void onStateAutoComplete() {
        Log.i(TAG, "onStateAutoComplete " + " [" + this.hashCode() + "] ");
        currentState = CURRENT_STATE_AUTO_COMPLETE;
        cancelProgressTimer();
    }

    public void onInfo(int what, int extra) {
        Log.d(TAG, "onInfo what - " + what + " extra - " + extra);
    }

    public void onError(int what, int extra) {
        Log.e(TAG, "onError " + what + " - " + extra + " [" + this.hashCode() + "] ");
        if (what != 38 && extra != -38 && what != -38 && extra != 38 && extra != -19) {
            onStateError();
            if (isCurrentPlay()) {
                WindowMediaManager.instance().releaseMediaPlayer();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN || currentScreen == SCREEN_WINDOW_TINY) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        if (widthRatio != 0 && heightRatio != 0) {
            int specWidth = MeasureSpec.getSize(widthMeasureSpec);
            int specHeight = (int) ((specWidth * (float) heightRatio) / widthRatio);
            setMeasuredDimension(specWidth, specHeight);

            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(specWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(specHeight, MeasureSpec.EXACTLY);
            getChildAt(0).measure(childWidthMeasureSpec, childHeightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }

    public void onAutoCompletion() {
        Runtime.getRuntime().gc();
        Log.i(TAG, "onAutoCompletion " + " [" + this.hashCode() + "] ");
        onEvent(WindowUserAction.ON_AUTO_COMPLETE);
        dismissVolumeDialog();
        dismissProgressDialog();
        dismissBrightnessDialog();
        cancelProgressTimer();
        onStateAutoComplete();

        if (currentScreen == SCREEN_WINDOW_FULLSCREEN || currentScreen == SCREEN_WINDOW_TINY) {
            backPress();
        }
        WindowMediaManager.instance().releaseMediaPlayer();
//        XinQuUtils.saveProgress(getContext(), XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex), 0);
    }

    public void onCompletion() {
        Log.i(TAG, "onCompletion " + " [" + this.hashCode() + "] ");
//        if (currentState == CURRENT_STATE_PLAYING || currentState == CURRENT_STATE_PAUSE) {
//            long position = getCurrentPositionWhenPlaying();
//            XinQuUtils.saveProgress(getContext(), XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex), position);
//        }
        cancelProgressTimer();
        onStateNormal();
        textureViewContainer.removeView(WindowMediaManager.textureView);
        WindowMediaManager.instance().currentVideoWidth = 0;
        WindowMediaManager.instance().currentVideoHeight = 0;

        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        XinQuUtils.scanForActivity(getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        clearFullscreenLayout();
        XinQuUtils.setRequestedOrientation(getContext(), NORMAL_ORIENTATION);

        if (WindowMediaManager.surface != null) WindowMediaManager.surface.release();
        if (WindowMediaManager.savedSurfaceTexture != null)
            WindowMediaManager.savedSurfaceTexture.release();
        WindowMediaManager.textureView = null;
        WindowMediaManager.savedSurfaceTexture = null;
    }

    public void release() {
        if (XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex).equals(WindowMediaManager.getCurrentDataSource()) &&
                (System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) > FULL_SCREEN_NORMAL_DELAY) {
            //在非全屏的情况下只能backPress()
            if (WindowVideoPlayerManager.getSecondFloor() != null &&
                    WindowVideoPlayerManager.getSecondFloor().currentScreen == SCREEN_WINDOW_FULLSCREEN) {//点击全屏
            } else if (WindowVideoPlayerManager.getSecondFloor() == null && WindowVideoPlayerManager.getFirstFloor() != null &&
                    WindowVideoPlayerManager.getFirstFloor().currentScreen == SCREEN_WINDOW_FULLSCREEN) {//直接全屏
            } else {
                Log.d(TAG, "releaseMediaPlayer [" + this.hashCode() + "]");
                releaseAllVideos();
            }
        }
    }

    public void initTextureView() {
        removeTextureView();
        WindowMediaManager.textureView = new WindowResizeTextureView(getContext());
        WindowMediaManager.textureView.setSurfaceTextureListener(WindowMediaManager.instance());
    }

    public void addTextureView() {
        Log.d(TAG, "addTextureView [" + this.hashCode() + "] ");
        LayoutParams layoutParams =
                new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER);
        textureViewContainer.addView(WindowMediaManager.textureView, layoutParams);
    }

    public void removeTextureView() {
        WindowMediaManager.savedSurfaceTexture = null;
        if (WindowMediaManager.textureView != null && WindowMediaManager.textureView.getParent() != null) {
            ((ViewGroup) WindowMediaManager.textureView.getParent()).removeView(WindowMediaManager.textureView);
        }
    }

    public void clearFullscreenLayout() {
        ViewGroup vp = (ViewGroup) (XinQuUtils.scanForActivity(getContext())).getWindow().getDecorView();//.findViewById(Window.ID_ANDROID_CONTENT);
        View oldF = vp.findViewById(R.id.xinqu_fullscreen_id);
        View oldT = vp.findViewById(R.id.xinqu_tiny_id);
        if (oldF != null) {
            vp.removeView(oldF);
        }
        if (oldT != null) {
            vp.removeView(oldT);
        }
        showSupportActionBar(getContext());
    }

    public void clearFloatScreen() {
        XinQuUtils.setRequestedOrientation(getContext(), NORMAL_ORIENTATION);
        showSupportActionBar(getContext());
        WindowVideoPlayer currJzvd = WindowVideoPlayerManager.getCurrentJzvd();
        currJzvd.textureViewContainer.removeView(WindowMediaManager.textureView);
        ViewGroup vp = (ViewGroup) (XinQuUtils.scanForActivity(getContext())).getWindow().getDecorView();//.findViewById(Window.ID_ANDROID_CONTENT);
        vp.removeView(currJzvd);
        WindowVideoPlayerManager.setSecondFloor(null);
    }

    public void onVideoSizeChanged() {
        Log.i(TAG, "onVideoSizeChanged " + " [" + this.hashCode() + "] ");
        if (WindowMediaManager.textureView != null) {
            if (videoRotation != 0) {
                WindowMediaManager.textureView.setRotation(videoRotation);
            }
            WindowMediaManager.textureView.setVideoSize(WindowMediaManager.instance().currentVideoWidth, WindowMediaManager.instance().currentVideoHeight);
        }
    }

    public void startProgressTimer() {
        Log.i(TAG, "startProgressTimer: " + " [" + this.hashCode() + "] ");
        cancelProgressTimer();
        UPDATE_PROGRESS_TIMER = new Timer();
        mProgressTimerTask = new ProgressTimerTask();
        UPDATE_PROGRESS_TIMER.schedule(mProgressTimerTask, 0, 300);
    }

    public void cancelProgressTimer() {
        if (UPDATE_PROGRESS_TIMER != null) {
            UPDATE_PROGRESS_TIMER.cancel();
        }
        if (mProgressTimerTask != null) {
            mProgressTimerTask.cancel();
        }
    }

    public void setProgressAndText(int progress, long position, long duration) {
        Log.d(TAG, "setProgressAndText: progress=" + progress + " position=" + position + " duration=" + duration);
    }

    public void setBufferProgress(int bufferProgress) {

    }

    public void resetProgressAndTime() {

    }

    public long getCurrentPositionWhenPlaying() {
        long position = 0;
        //TODO 这块的判断应该根据MediaPlayer来
//        if (WindowMediaManager.instance().mediaPlayer == null)
//            return position;//这行代码不应该在这，如果代码和逻辑万无一失的话，心头之恨呐
        if (currentState == CURRENT_STATE_PLAYING ||
                currentState == CURRENT_STATE_PAUSE) {
            try {
                position = WindowMediaManager.getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return position;
            }
        }
        return position;
    }

    public long getDuration() {
        long duration = 0;
        //TODO MediaPlayer 判空的问题
//        if (WindowMediaManager.instance().mediaPlayer == null) return duration;
        try {
            duration = WindowMediaManager.getDuration();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return duration;
        }
        return duration;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.i(TAG, "bottomProgress onStartTrackingTouch [" + this.hashCode() + "] ");
        cancelProgressTimer();
        ViewParent vpdown = getParent();
        while (vpdown != null) {
            vpdown.requestDisallowInterceptTouchEvent(true);
            vpdown = vpdown.getParent();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.i(TAG, "bottomProgress onStopTrackingTouch [" + this.hashCode() + "] ");
        onEvent(WindowUserAction.ON_SEEK_POSITION);
        startProgressTimer();
        ViewParent vpup = getParent();
        while (vpup != null) {
            vpup.requestDisallowInterceptTouchEvent(false);
            vpup = vpup.getParent();
        }
        if (currentState != CURRENT_STATE_PLAYING &&
                currentState != CURRENT_STATE_PAUSE) return;
        long time = seekBar.getProgress() * getDuration() / 100;
        WindowMediaManager.seekTo(time);
        Log.i(TAG, "seekTo " + time + " [" + this.hashCode() + "] ");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    public void startWindowFullscreen() {
        Log.i(TAG, "startWindowFullscreen " + " [" + this.hashCode() + "] ");
        hideSupportActionBar(getContext());
        XinQuUtils.setRequestedOrientation(getContext(), FULLSCREEN_ORIENTATION);

        ViewGroup vp = (ViewGroup) (XinQuUtils.scanForActivity(getContext())).getWindow().getDecorView();//.findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(R.id.xinqu_fullscreen_id);
        if (old != null) {
            vp.removeView(old);
        }
        textureViewContainer.removeView(WindowMediaManager.textureView);
        try {
            Constructor<WindowVideoPlayer> constructor = (Constructor<WindowVideoPlayer>) WindowVideoPlayer.this.getClass().getConstructor(Context.class);
            WindowVideoPlayer windowVideoPlayer = constructor.newInstance(getContext());
            windowVideoPlayer.setId(R.id.xinqu_fullscreen_id);
            LayoutParams lp = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            vp.addView(windowVideoPlayer, lp);
            windowVideoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);
            windowVideoPlayer.setUp(dataSourceObjects, currentUrlMapIndex, SCREEN_WINDOW_FULLSCREEN,loop, objects);
            windowVideoPlayer.setState(currentState);
            windowVideoPlayer.addTextureView();
            WindowVideoPlayerManager.setSecondFloor(windowVideoPlayer);
//            final Animation ra = AnimationUtils.loadAnimation(getContext(), R.anim.start_fullscreen);
//            jzVideoPlayer.setAnimation(ra);
            onStateNormal();
            windowVideoPlayer.startProgressTimer();
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startWindowTiny() {
        boolean flag=true;
        Log.d(TAG,"startWindowTiny");
        if(flag) return;
        onEvent(WindowUserAction.ON_ENTER_TINYSCREEN);
        if (currentState == CURRENT_STATE_NORMAL || currentState == CURRENT_STATE_ERROR || currentState == CURRENT_STATE_AUTO_COMPLETE)
            return;
        ViewGroup vp = (ViewGroup) (XinQuUtils.scanForActivity(getContext())).getWindow().getDecorView();//.findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(R.id.xinqu_tiny_id);
        if (old != null) {
            vp.removeView(old);
        }
        textureViewContainer.removeView(WindowMediaManager.textureView);

        try {
            Constructor<WindowVideoPlayer> constructor = (Constructor<WindowVideoPlayer>) WindowVideoPlayer.this.getClass().getConstructor(Context.class);
            WindowVideoPlayer xinQuVideoPlayer = constructor.newInstance(getContext());
            xinQuVideoPlayer.setId(R.id.xinqu_tiny_id);
            LayoutParams lp = new LayoutParams(400, 400);
            lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            vp.addView(xinQuVideoPlayer, lp);
            xinQuVideoPlayer.setUp(dataSourceObjects, currentUrlMapIndex, SCREEN_WINDOW_TINY, loop,objects);
            xinQuVideoPlayer.setState(currentState);
            xinQuVideoPlayer.addTextureView();
            WindowVideoPlayerManager.setSecondFloor(xinQuVideoPlayer);
            onStateNormal();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isCurrentPlay() {
        return isCurrentJZVD()
                && XinQuUtils.dataSourceObjectsContainsUri(dataSourceObjects, WindowMediaManager.getCurrentDataSource());//不仅正在播放的url不能一样，并且各个清晰度也不能一样
    }

    public boolean isCurrentJZVD() {
        return WindowVideoPlayerManager.getCurrentJzvd() != null
                && WindowVideoPlayerManager.getCurrentJzvd() == this;
    }

    //退出全屏和小窗的方法
    public void playOnThisJzvd() {
        Log.i(TAG, "playOnThisJzvd " + " [" + this.hashCode() + "] ");
        //1.清空全屏和小窗的jzvd
        currentState = WindowVideoPlayerManager.getSecondFloor().currentState;
        currentUrlMapIndex = WindowVideoPlayerManager.getSecondFloor().currentUrlMapIndex;
        clearFloatScreen();
        //2.在本jzvd上播放
        setState(currentState);
        addTextureView();
    }

    //重力感应的时候调用的函数，
    public void autoFullscreen(float x) {
        if (isCurrentPlay()
                && currentState == CURRENT_STATE_PLAYING
                && currentScreen != SCREEN_WINDOW_FULLSCREEN
                && currentScreen != SCREEN_WINDOW_TINY) {
            if (x > 0) {
                XinQuUtils.setRequestedOrientation(getContext(), ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                XinQuUtils.setRequestedOrientation(getContext(), ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            }
            onEvent(WindowUserAction.ON_ENTER_FULLSCREEN);
            startWindowFullscreen();
        }
    }

    public void autoQuitFullscreen() {
        if ((System.currentTimeMillis() - lastAutoFullscreenTime) > 2000
                && isCurrentPlay()
                && currentState == CURRENT_STATE_PLAYING
                && currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            lastAutoFullscreenTime = System.currentTimeMillis();
            backPress();
        }
    }

    public void onEvent(int type) {
        if (JZ_USER_EVENT != null && isCurrentPlay() && dataSourceObjects != null) {
            JZ_USER_EVENT.onEvent(type, XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex), currentScreen, objects);
        }
    }

    public static void setMediaInterface(WindowMediaInterface mediaInterface) {
        WindowMediaManager.instance().mXinQuMediaInterface = mediaInterface;
    }

    //TODO 是否有用
    public void onSeekComplete() {

    }

    public void showWifiDialog(int event) {
    }

    public void showProgressDialog(float deltaX,
                                   String seekTime, long seekTimePosition,
                                   String totalTime, long totalTimeDuration) {
    }

    public void dismissProgressDialog() {

    }

    public void showVolumeDialog(float deltaY, int volumePercent) {

    }

    public void dismissVolumeDialog() {

    }

    public void showBrightnessDialog(int brightnessPercent) {

    }

    public void dismissBrightnessDialog() {

    }


    public boolean isPlaying() {
        return  WindowMediaManager.instance().isPlaying();
    }

    public static class XinQuAutoFullscreenListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {//可以得到传感器实时测量出来的变化值
            final float x = event.values[SensorManager.DATA_X];
            float y = event.values[SensorManager.DATA_Y];
            float z = event.values[SensorManager.DATA_Z];
            //过滤掉用力过猛会有一个反向的大数值
            if (((x > -15 && x < -10) || (x < 15 && x > 10)) && Math.abs(y) < 1.5) {
                if ((System.currentTimeMillis() - lastAutoFullscreenTime) > 2000) {
                    if (WindowVideoPlayerManager.getCurrentJzvd() != null) {
                        WindowVideoPlayerManager.getCurrentJzvd().autoFullscreen(x);
                    }
                    lastAutoFullscreenTime = System.currentTimeMillis();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    public class ProgressTimerTask extends TimerTask {
        @Override
        public void run() {
            if (currentState == CURRENT_STATE_PLAYING || currentState == CURRENT_STATE_PAUSE) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        long position = getCurrentPositionWhenPlaying();
                        long duration = getDuration();
                        int progress = (int) (position * 100 / (duration == 0 ? 1 : duration));
                        setProgressAndText(progress, position, duration);
                    }
                });
            }
        }
    }



    public interface OnPlayerCallBackListener{
        void startPlayCallBack();
    }

    private OnPlayerCallBackListener mOnPlayerCallBackListener;

    public void setOnPlayerCallBackListener(OnPlayerCallBackListener onPlayerCallBackListener) {
        mOnPlayerCallBackListener = onPlayerCallBackListener;
    }




    public interface OnPlayCompletionListener{
        void onCompletion();
    }

    public void setOnPlayCompletionListener(OnPlayCompletionListener onPlayCompletionListener) {
        mOnPlayCompletionListener = onPlayCompletionListener;
    }

    public OnPlayCompletionListener mOnPlayCompletionListener;

    public int getViewHeight() {
        return 0;
    }
}
