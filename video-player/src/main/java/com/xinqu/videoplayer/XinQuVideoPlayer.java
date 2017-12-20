package com.xinqu.videoplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.xinqu.videoplayer.listener.PerfectClickListener;
import com.xinqu.videoplayer.manager.XinQuMediaManager;
import com.xinqu.videoplayer.manager.XinQuVideoPlayerManager;
import com.xinqu.videoplayer.util.XinQuUtils;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR;


/**
 * Created by Nathen on 16/7/30.
 */
public abstract class XinQuVideoPlayer extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener {

    public static final String TAG = XinQuVideoPlayer.class.getSimpleName();
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
    public static int FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;//全屏方向
    public static int NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE;//默认方向
    public static boolean SAVE_PROGRESS = true;
    public static int VIDEO_IMAGE_DISPLAY_TYPE = 0;
    public static long CLICK_QUIT_FULLSCREEN_TIME = 0;
    public static long lastAutoFullscreenTime = 0;
    protected static XinQuUserAction JZ_USER_EVENT;
    protected static Timer UPDATE_PROGRESS_TIMER;
    private static Context mContext;
    public int currentState = -1;
    public int currentScreen = -1;
    public Object[] objects = null;
    public long seekToInAdvance = 0;
    public ImageView startButton;
    public SeekBar progressBar;
    public ImageView fullscreenButton;
    public TextView currentTimeTextView, totalTimeTextView;
    public ViewGroup textureViewContainer;
    public ViewGroup topContainer, bottomContainer;
    public  static boolean loop = false;
    public Object[] dataSourceObjects;//这个参数原封不动直接通过XinQuMeidaManager传给XinQuMediaInterface。
    public int currentUrlMapIndex = 0;
    public int positionInList = -1;
    public int videoRotation = 0;
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected AudioManager mAudioManager;
    protected Handler mHandler;
    protected ProgressTimerTask mProgressTimerTask;
    protected boolean mTouchingProgressBar;
    protected float mDownX;
    protected float mDownY;
    protected boolean mChangeVolume;
    protected boolean mChangePosition;
    protected boolean mChangeBrightness;
    protected long mGestureDownPosition;
    protected int mGestureDownVolume;
    protected float mGestureDownBrightness;
    protected long mSeekTimePosition;
    boolean tmp_test_back = false;



    public static Context getmContext() {
        return mContext;
    }


    public static AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {//是否新建个class，代码更规矩，并且变量的位置也很尴尬
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    releaseAllVideos();
                    Log.d(TAG, "AUDIOFOCUS_LOSS [" + this.hashCode() + "]");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    try {
                        if (//XinQuMediaManager.instance().mediaPlayer != null &&
                                XinQuMediaManager.isPlaying()) {
                            XinQuMediaManager.pause();
                        }
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT [" + this.hashCode() + "]");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    break;
            }
        }
    };


    public XinQuVideoPlayer(Context context) {
        super(context);
        init(context);
    }

    public XinQuVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public static void releaseAllVideos() {
        if ((System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) > FULL_SCREEN_NORMAL_DELAY) {
            Log.d(TAG, "releaseAllVideos");
            XinQuVideoPlayerManager.completeAll();
            XinQuMediaManager.instance().positionInList = -1;
            XinQuMediaManager.instance().releaseMediaPlayer();
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
        ViewGroup vp = (ViewGroup) (XinQuUtils.scanForActivity(context)).getWindow().getDecorView();
        View old = vp.findViewById(R.id.xinqu_fullscreen_id);
        if (old != null) {
            vp.removeView(old);
        }
        try {
            Constructor<XinQuVideoPlayer> constructor = _class.getConstructor(Context.class);
            final XinQuVideoPlayer xinQuVideoPlayer = constructor.newInstance(context);
            xinQuVideoPlayer.setId(R.id.xinqu_fullscreen_id);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            vp.addView(xinQuVideoPlayer, lp);
//            final Animation ra = AnimationUtils.loadAnimation(context, R.anim.start_fullscreen);
//            jzVideoPlayer.setAnimation(ra);
            xinQuVideoPlayer.setUp(dataSourceObjects, defaultUrlMapIndex, SCREEN_WINDOW_FULLSCREEN,loop,objects);
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            xinQuVideoPlayer.startButton.performClick();
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
        if (XinQuVideoPlayerManager.getSecondFloor() != null) {
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            if (XinQuUtils.dataSourceObjectsContainsUri(XinQuVideoPlayerManager.getFirstFloor().dataSourceObjects, XinQuMediaManager.getCurrentDataSource())) {
                XinQuVideoPlayer xinQuVideoPlayer = XinQuVideoPlayerManager.getSecondFloor();
                xinQuVideoPlayer.onEvent(xinQuVideoPlayer.currentScreen == SCREEN_WINDOW_FULLSCREEN ?
                        XinQuUserAction.ON_QUIT_FULLSCREEN :
                        XinQuUserAction.ON_QUIT_TINYSCREEN);
                XinQuVideoPlayerManager.getFirstFloor().playOnThisJzvd();
            } else {
                quitFullscreenOrTinyWindow();
            }
            return true;
        } else if (XinQuVideoPlayerManager.getFirstFloor() != null && (XinQuVideoPlayerManager.getFirstFloor().currentScreen == SCREEN_WINDOW_FULLSCREEN || XinQuVideoPlayerManager.getFirstFloor().currentScreen == SCREEN_WINDOW_TINY)) {//以前我总想把这两个判断写到一起，这分明是两个独立是逻辑
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            Log.d(TAG,"backPress,直接退出全屏或小窗口");
            quitFullscreenOrTinyWindow();
            return true;
        }
        return false;
    }

    public static void quitFullscreenOrTinyWindow() {
        //直接退出全屏和小窗
        XinQuVideoPlayerManager.getFirstFloor().clearFloatScreen();
        XinQuMediaManager.instance().releaseMediaPlayer();
        XinQuVideoPlayerManager.completeAll();
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

    public static void setJzUserAction(XinQuUserAction jzUserEvent) {
        JZ_USER_EVENT = jzUserEvent;
    }

    public static void goOnPlayOnResume() {
        if (XinQuVideoPlayerManager.getCurrentJzvd() != null) {
            XinQuVideoPlayer jzvd = XinQuVideoPlayerManager.getCurrentJzvd();
            if (jzvd.currentState == XinQuVideoPlayer.CURRENT_STATE_PAUSE) {
                jzvd.onStatePlaying();
                XinQuMediaManager.start();
            }
        }
    }

    public static void goOnPlayOnPause() {
        if (XinQuVideoPlayerManager.getCurrentJzvd() != null) {
            XinQuVideoPlayer jzvd = XinQuVideoPlayerManager.getCurrentJzvd();
            if (jzvd.currentState == XinQuVideoPlayer.CURRENT_STATE_AUTO_COMPLETE ||
                    jzvd.currentState == XinQuVideoPlayer.CURRENT_STATE_NORMAL) {
//                JZVideoPlayer.releaseAllVideos();
            } else {
                jzvd.onStatePause();
                XinQuMediaManager.pause();
            }
        }
    }

    public static void onScrollAutoTiny(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastVisibleItem = firstVisibleItem + visibleItemCount;
        int currentPlayPosition = XinQuMediaManager.instance().positionInList;
        if (currentPlayPosition >= 0) {
            if ((currentPlayPosition < firstVisibleItem || currentPlayPosition > (lastVisibleItem - 1))) {
                if (XinQuVideoPlayerManager.getCurrentJzvd() != null &&
                        XinQuVideoPlayerManager.getCurrentJzvd().currentScreen != XinQuVideoPlayer.SCREEN_WINDOW_TINY) {
                    if (XinQuVideoPlayerManager.getCurrentJzvd().currentState == XinQuVideoPlayer.CURRENT_STATE_PAUSE) {
                        XinQuVideoPlayer.releaseAllVideos();
                    } else {
                        Log.e(TAG, "onScroll: out screen");
                        XinQuVideoPlayerManager.getCurrentJzvd().startWindowTiny();
                    }
                }
            } else {
                if (XinQuVideoPlayerManager.getCurrentJzvd() != null &&
                        XinQuVideoPlayerManager.getCurrentJzvd().currentScreen == XinQuVideoPlayer.SCREEN_WINDOW_TINY) {
                    Log.e(TAG, "onScroll: into screen");
                    XinQuVideoPlayer.backPress();
                }
            }
        }
    }

    public static void onScrollReleaseAllVideos(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastVisibleItem = firstVisibleItem + visibleItemCount;
        int currentPlayPosition = XinQuMediaManager.instance().positionInList;
        if (currentPlayPosition >= 0) {
            if ((currentPlayPosition < firstVisibleItem || currentPlayPosition > (lastVisibleItem - 1))) {
                XinQuVideoPlayer.releaseAllVideos();
            }
        }
    }

    public static void onChildViewAttachedToWindow(View view, int jzvdId) {
        if (XinQuVideoPlayerManager.getCurrentJzvd() != null && XinQuVideoPlayerManager.getCurrentJzvd().currentScreen == XinQuVideoPlayer.SCREEN_WINDOW_TINY) {
            XinQuVideoPlayer videoPlayer = view.findViewById(jzvdId);
            if (videoPlayer != null && XinQuUtils.getCurrentFromDataSource(videoPlayer.dataSourceObjects, videoPlayer.currentUrlMapIndex).equals(XinQuMediaManager.getCurrentDataSource())) {
                XinQuVideoPlayer.backPress();
            }
        }
    }

    public static void onChildViewDetachedFromWindow(View view) {
        if (XinQuVideoPlayerManager.getCurrentJzvd() != null && XinQuVideoPlayerManager.getCurrentJzvd().currentScreen != XinQuVideoPlayer.SCREEN_WINDOW_TINY) {
            XinQuVideoPlayer videoPlayer = XinQuVideoPlayerManager.getCurrentJzvd();
            if (((ViewGroup) view).indexOfChild(videoPlayer) != -1) {
                if (videoPlayer.currentState == XinQuVideoPlayer.CURRENT_STATE_PAUSE) {
                    XinQuVideoPlayer.releaseAllVideos();
                } else {
                    videoPlayer.startWindowTiny();
                }
            }
        }
    }

    public static void setTextureViewRotation(int rotation) {
        if (XinQuMediaManager.textureView != null) {
            XinQuMediaManager.textureView.setRotation(rotation);
        }
    }

    public static void setVideoImageDisplayType(int type) {
        XinQuVideoPlayer.VIDEO_IMAGE_DISPLAY_TYPE = type;
        if (XinQuMediaManager.textureView != null) {
            XinQuMediaManager.textureView.requestLayout();
        }
    }

    public Object getCurrentUrl() {
        return XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex);
    }

    public abstract int getLayoutId();

    public void init(Context context) {
        mContext=context;
        View.inflate(context, getLayoutId(), this);
        startButton = findViewById(R.id.start);
        fullscreenButton = findViewById(R.id.fullscreen);
        progressBar = findViewById(R.id.bottom_seek_progress);
        currentTimeTextView = findViewById(R.id.current);
        totalTimeTextView = findViewById(R.id.total);
        bottomContainer = findViewById(R.id.layout_bottom);
        textureViewContainer = findViewById(R.id.surface_container);
        topContainer = findViewById(R.id.layout_top);

        fullscreenButton.setOnClickListener(this);
        progressBar.setOnSeekBarChangeListener(this);
        bottomContainer.setOnClickListener(this);
        textureViewContainer.setOnClickListener(this);
        textureViewContainer.setOnTouchListener(this);
        //避免600毫秒内多次点击
        startButton.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (dataSourceObjects == null || XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex) == null) {
                    Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (currentState == CURRENT_STATE_NORMAL) {
//                    if (!XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex).toString().startsWith("file") && !
//                            XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex).toString().startsWith("/")) {
//                        return;
//                    }
                    startVideo();
                    onEvent(XinQuUserAction.ON_CLICK_START_ICON);
                } else if (currentState == CURRENT_STATE_PLAYING) {
                    onEvent(XinQuUserAction.ON_CLICK_PAUSE);
                    Log.d(TAG, "pauseVideo [" + this.hashCode() + "] ");
                    XinQuMediaManager.pause();
                    onStatePause();
                } else if (currentState == CURRENT_STATE_PAUSE) {
                    onEvent(XinQuUserAction.ON_CLICK_RESUME);
                    XinQuMediaManager.start();
                    onStatePlaying();
                } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
                    onEvent(XinQuUserAction.ON_CLICK_START_AUTO_COMPLETE);
                    startVideo();
                }
            }
        });

        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mHandler = new Handler();
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
        if (isCurrentJZVD() && XinQuUtils.dataSourceObjectsContainsUri(dataSourceObjects, XinQuMediaManager.getCurrentDataSource())) {
            XinQuMediaManager.instance().releaseMediaPlayer();
        } else if (isCurrentJZVD() && !XinQuUtils.dataSourceObjectsContainsUri(dataSourceObjects, XinQuMediaManager.getCurrentDataSource())) {
            startWindowTiny();
        } else if (!isCurrentJZVD() && XinQuUtils.dataSourceObjectsContainsUri(dataSourceObjects, XinQuMediaManager.getCurrentDataSource())) {
            if (XinQuVideoPlayerManager.getCurrentJzvd() != null &&
                    XinQuVideoPlayerManager.getCurrentJzvd().currentScreen == XinQuVideoPlayer.SCREEN_WINDOW_TINY) {
                //需要退出小窗退到我这里，我这里是第一层级
                tmp_test_back = true;
            }
        } else if (!isCurrentJZVD() && !XinQuUtils.dataSourceObjectsContainsUri(dataSourceObjects, XinQuMediaManager.getCurrentDataSource())) {
        }
        this.dataSourceObjects = dataSourceObjects;
        this.currentUrlMapIndex = defaultUrlMapIndex;
        this.currentScreen = screen;
        this.objects = objects;
        this.loop=loop;
        onStateNormal();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.fullscreen) {
            Log.i(TAG, "onClick fullscreen [" + this.hashCode() + "] ");
            if (currentState == CURRENT_STATE_AUTO_COMPLETE) return;
            if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                //quit fullscreen
                backPress();
            } else {
                Log.d(TAG, "toFullscreenActivity [" + this.hashCode() + "] ");
                onEvent(XinQuUserAction.ON_ENTER_FULLSCREEN);
                startWindowFullscreen();
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int id = v.getId();
        if (id == R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i(TAG, "onTouch surfaceContainer actionDown [" + this.hashCode() + "] ");
                    mTouchingProgressBar = true;

                    mDownX = x;
                    mDownY = y;
                    mChangeVolume = false;
                    mChangePosition = false;
                    mChangeBrightness = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i(TAG, "onTouch surfaceContainer actionMove [" + this.hashCode() + "] ");
                    float deltaX = x - mDownX;
                    float deltaY = y - mDownY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                        if (!mChangePosition && !mChangeVolume && !mChangeBrightness) {
                            if (absDeltaX > THRESHOLD || absDeltaY > THRESHOLD) {
                                cancelProgressTimer();
                                if (absDeltaX >= THRESHOLD) {
                                    // 全屏模式下的CURRENT_STATE_ERROR状态下,不响应进度拖动事件.
                                    // 否则会因为mediaplayer的状态非法导致App Crash
                                    if (currentState != CURRENT_STATE_ERROR) {
                                        mChangePosition = true;
                                        mGestureDownPosition = getCurrentPositionWhenPlaying();
                                    }
                                } else {
                                    //如果y轴滑动距离超过设置的处理范围，那么进行滑动事件处理
                                    if (mDownX < mScreenWidth * 0.5f) {//左侧改变亮度
                                        mChangeBrightness = true;
                                        WindowManager.LayoutParams lp = XinQuUtils.getWindow(getContext()).getAttributes();
                                        if (lp.screenBrightness < 0) {
                                            try {
                                                mGestureDownBrightness = Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                                                Log.i(TAG, "current system brightness: " + mGestureDownBrightness);
                                            } catch (Settings.SettingNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            mGestureDownBrightness = lp.screenBrightness * 255;
                                            Log.i(TAG, "current activity brightness: " + mGestureDownBrightness);
                                        }
                                    } else {//右侧改变声音
                                        mChangeVolume = true;
                                        mGestureDownVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                                    }
                                }
                            }
                        }
                    }
                    if (mChangePosition) {
                        long totalTimeDuration = getDuration();
                        mSeekTimePosition = (int) (mGestureDownPosition + deltaX * totalTimeDuration / mScreenWidth);
                        if (mSeekTimePosition > totalTimeDuration)
                            mSeekTimePosition = totalTimeDuration;
                        String seekTime = XinQuUtils.stringForTime(mSeekTimePosition);
                        String totalTime = XinQuUtils.stringForTime(totalTimeDuration);

                        showProgressDialog(deltaX, seekTime, mSeekTimePosition, totalTime, totalTimeDuration);
                    }
                    if (mChangeVolume) {
                        deltaY = -deltaY;
                        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                        int deltaV = (int) (max * deltaY * 3 / mScreenHeight);
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mGestureDownVolume + deltaV, 0);
                        //dialog中显示百分比
                        int volumePercent = (int) (mGestureDownVolume * 100 / max + deltaY * 3 * 100 / mScreenHeight);
                        showVolumeDialog(-deltaY, volumePercent);
                    }

                    if (mChangeBrightness) {
                        deltaY = -deltaY;
                        int deltaV = (int) (255 * deltaY * 3 / mScreenHeight);
                        WindowManager.LayoutParams params = XinQuUtils.getWindow(getContext()).getAttributes();
                        if (((mGestureDownBrightness + deltaV) / 255) >= 1) {//这和声音有区别，必须自己过滤一下负值
                            params.screenBrightness = 1;
                        } else if (((mGestureDownBrightness + deltaV) / 255) <= 0) {
                            params.screenBrightness = 0.01f;
                        } else {
                            params.screenBrightness = (mGestureDownBrightness + deltaV) / 255;
                        }
                        XinQuUtils.getWindow(getContext()).setAttributes(params);
                        //dialog中显示百分比
                        int brightnessPercent = (int) (mGestureDownBrightness * 100 / 255 + deltaY * 3 * 100 / mScreenHeight);
                        showBrightnessDialog(brightnessPercent);
//                        mDownY = y;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i(TAG, "onTouch surfaceContainer actionUp [" + this.hashCode() + "] ");
                    mTouchingProgressBar = false;
                    dismissProgressDialog();
                    dismissVolumeDialog();
                    dismissBrightnessDialog();
                    if (mChangePosition) {
                        onEvent(XinQuUserAction.ON_TOUCH_SCREEN_SEEK_POSITION);
                        XinQuMediaManager.seekTo(mSeekTimePosition);
                        long duration = getDuration();
                        int progress = (int) (mSeekTimePosition * 100 / (duration == 0 ? 1 : duration));
                        progressBar.setProgress(progress);
                    }
                    if (mChangeVolume) {
                        onEvent(XinQuUserAction.ON_TOUCH_SCREEN_SEEK_VOLUME);
                    }
                    startProgressTimer();
                    break;
            }
        }
        return false;
    }

    public void startVideo() {
        XinQuVideoPlayerManager.completeAll();
        //非WIFI网络环境下，且未提示过，且有网络连接，提示，否则一律放行
        if(!XinQuUtils.WIFI_TIP_DIALOG_SHOWED&&!XinQuUtils.isWifiConnected(getContext())&&XinQuUtils.isCheckNetwork(getContext())){
            showWifiDialog(XinQuUserActionStandard.ON_CLICK_START_ICON);
            return;
        }
        initTextureView();
        addTextureView();
        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        XinQuUtils.scanForActivity(getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        XinQuMediaManager.setLoop(loop);
        XinQuMediaManager.setDataSource(dataSourceObjects);
        XinQuMediaManager.setCurrentDataSource(XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex));
        XinQuMediaManager.instance().positionInList = positionInList;
        onStatePreparing();
        XinQuVideoPlayerManager.setFirstFloor(this);
        if(null!=mOnPlayerCallBackListener){
            mOnPlayerCallBackListener.callBack();
        }
    }

    public void onPrepared() {
        Log.i(TAG, "onPrepared " + " [" + this.hashCode() + "] ");
        onStatePrepared();
        onStatePlaying();
    }

    public void showWifiDialog(int event) {
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
        XinQuMediaManager.setDataSource(dataSourceObjects);
        XinQuMediaManager.setCurrentDataSource(XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex));
        XinQuMediaManager.instance().prepare();
    }

    public void onStatePrepared() {//因为这个紧接着就会进入播放状态，所以不设置state
        if (seekToInAdvance != 0) {
            XinQuMediaManager.seekTo(seekToInAdvance);
            seekToInAdvance = 0;
        }
//        else {
//            long position = XinQuUtils.getSavedProgress(getContext(), XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex));
//            if (position != 0) {
//                XinQuMediaManager.seekTo(position);
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
        progressBar.setProgress(100);
        currentTimeTextView.setText(totalTimeTextView.getText());
    }

    public void onInfo(int what, int extra) {
        Log.d(TAG, "onInfo what - " + what + " extra - " + extra);
    }

    public void onError(int what, int extra) {
        Log.e(TAG, "onError " + what + " - " + extra + " [" + this.hashCode() + "] ");
        if (what != 38 && extra != -38 && what != -38 && extra != 38 && extra != -19) {
            onStateError();
            if (isCurrentPlay()) {
                XinQuMediaManager.instance().releaseMediaPlayer();
            }
        }
    }


    public int widthRatio = 0;
    public int heightRatio = 0;


    public int getHeightRatio() {
        return heightRatio;
    }

    public int getWidthRatio() {
        return widthRatio;
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
        onEvent(XinQuUserAction.ON_AUTO_COMPLETE);
        dismissVolumeDialog();
        dismissProgressDialog();
        dismissBrightnessDialog();
        cancelProgressTimer();
        onStateAutoComplete();

        if (currentScreen == SCREEN_WINDOW_FULLSCREEN || currentScreen == SCREEN_WINDOW_TINY) {
            backPress();
        }
        XinQuMediaManager.instance().releaseMediaPlayer();
        if(null!=mOnPlayCompletionListener){
            mOnPlayCompletionListener.onCompletion();
        }
    }

    public void onCompletion() {
        Log.i(TAG, "onCompletion " + " [" + this.hashCode() + "] ");
        cancelProgressTimer();
        onStateNormal();
        textureViewContainer.removeView(XinQuMediaManager.textureView);
        XinQuMediaManager.instance().currentVideoWidth = 0;
        XinQuMediaManager.instance().currentVideoHeight = 0;

        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        XinQuUtils.scanForActivity(getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        clearFullscreenLayout();
        XinQuUtils.setRequestedOrientation(getContext(), NORMAL_ORIENTATION);
        if (XinQuMediaManager.surface != null) XinQuMediaManager.surface.release();
        if (XinQuMediaManager.savedSurfaceTexture != null)
            XinQuMediaManager.savedSurfaceTexture.release();
        XinQuMediaManager.textureView = null;
        XinQuMediaManager.savedSurfaceTexture = null;
    }

    public void release() {
        if (XinQuUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex).equals(XinQuMediaManager.getCurrentDataSource()) &&
                (System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) > FULL_SCREEN_NORMAL_DELAY) {
            //在非全屏的情况下只能backPress()
            if (XinQuVideoPlayerManager.getSecondFloor() != null &&
                    XinQuVideoPlayerManager.getSecondFloor().currentScreen == SCREEN_WINDOW_FULLSCREEN) {//点击全屏
            } else if (XinQuVideoPlayerManager.getSecondFloor() == null && XinQuVideoPlayerManager.getFirstFloor() != null &&
                    XinQuVideoPlayerManager.getFirstFloor().currentScreen == SCREEN_WINDOW_FULLSCREEN) {//直接全屏
            } else {
                Log.d(TAG, "releaseMediaPlayer [" + this.hashCode() + "]");
                releaseAllVideos();
            }
        }
    }

    public void initTextureView() {
        removeTextureView();
        XinQuMediaManager.textureView = new XinQuResizeTextureView(getContext());
        XinQuMediaManager.textureView.setSurfaceTextureListener(XinQuMediaManager.instance());
    }

    public void addTextureView() {
        Log.d(TAG, "addTextureView [" + this.hashCode() + "] ");
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        textureViewContainer.addView(XinQuMediaManager.textureView, layoutParams);
    }

    public void removeTextureView() {
        XinQuMediaManager.savedSurfaceTexture = null;
        if (XinQuMediaManager.textureView != null && XinQuMediaManager.textureView.getParent() != null) {
            ((ViewGroup) XinQuMediaManager.textureView.getParent()).removeView(XinQuMediaManager.textureView);
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
//        showSupportActionBar(getContext());
    }

    public void clearFloatScreen() {
        Log.d(TAG,"clearFloatScreen"+NORMAL_ORIENTATION);
        XinQuUtils.setRequestedOrientation(getContext(), NORMAL_ORIENTATION);
//        showSupportActionBar(getContext());
        XinQuVideoPlayer currJzvd = XinQuVideoPlayerManager.getCurrentJzvd();
        currJzvd.textureViewContainer.removeView(XinQuMediaManager.textureView);
        ViewGroup vp = (ViewGroup) (XinQuUtils.scanForActivity(getContext())).getWindow().getDecorView();//.findViewById(Window.ID_ANDROID_CONTENT);
        vp.removeView(currJzvd);
        XinQuVideoPlayerManager.setSecondFloor(null);
    }

    public void onVideoSizeChanged() {
        Log.i(TAG, "onVideoSizeChanged " + " [" + this.hashCode() + "] ");
        if (XinQuMediaManager.textureView != null) {
            if (videoRotation != 0) {
                XinQuMediaManager.textureView.setRotation(videoRotation);
            }
            XinQuMediaManager.textureView.setVideoSize(XinQuMediaManager.instance().currentVideoWidth, XinQuMediaManager.instance().currentVideoHeight);
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
//        Log.d(TAG, "setProgressAndText: progress=" + progress + " position=" + position + " duration=" + duration);
        if (!mTouchingProgressBar) {
            if (progress != 0) progressBar.setProgress(progress);
        }
        if (position != 0) currentTimeTextView.setText(XinQuUtils.stringForTime(position));
        totalTimeTextView.setText(XinQuUtils.stringForTime(duration));
    }

    public void setBufferProgress(int bufferProgress) {
        if (bufferProgress != 0) progressBar.setSecondaryProgress(bufferProgress);
    }

    public void resetProgressAndTime() {
        progressBar.setProgress(0);
        progressBar.setSecondaryProgress(0);
        currentTimeTextView.setText(XinQuUtils.stringForTime(0));
        totalTimeTextView.setText(XinQuUtils.stringForTime(0));
    }

    public long getCurrentPositionWhenPlaying() {
        long position = 0;
        //TODO 这块的判断应该根据MediaPlayer来
//        if (XinQuMediaManager.instance().mediaPlayer == null)
//            return position;//这行代码不应该在这，如果代码和逻辑万无一失的话，心头之恨呐
        if (currentState == CURRENT_STATE_PLAYING ||
                currentState == CURRENT_STATE_PAUSE) {
            try {
                position = XinQuMediaManager.getCurrentPosition();
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
//        if (XinQuMediaManager.instance().mediaPlayer == null) return duration;
        try {
            duration = XinQuMediaManager.getDuration();
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
        onEvent(XinQuUserAction.ON_SEEK_POSITION);
        startProgressTimer();
        ViewParent vpup = getParent();
        while (vpup != null) {
            vpup.requestDisallowInterceptTouchEvent(false);
            vpup = vpup.getParent();
        }
        if (currentState != CURRENT_STATE_PLAYING &&
                currentState != CURRENT_STATE_PAUSE) return;
        long time = seekBar.getProgress() * getDuration() / 100;
        XinQuMediaManager.seekTo(time);
        Log.i(TAG, "seekTo " + time + " [" + this.hashCode() + "] ");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    public void startWindowFullscreen() {
        //这两句话决定了开始播放的时候默认方向属性
        Log.d(TAG,"startWindowFullscreen--widthRatio="+widthRatio+",heightRatio="+heightRatio);
        FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
        NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
        hideSupportActionBar(getContext());
        XinQuUtils.setRequestedOrientation(getContext(), FULLSCREEN_ORIENTATION);
        ViewGroup vp = (ViewGroup) (XinQuUtils.scanForActivity(getContext())).getWindow().getDecorView();//.findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(R.id.xinqu_fullscreen_id);
        if (old != null) {
            vp.removeView(old);
        }
        textureViewContainer.removeView(XinQuMediaManager.textureView);
        try {
            Constructor<XinQuVideoPlayer> constructor = (Constructor<XinQuVideoPlayer>) XinQuVideoPlayer.this.getClass().getConstructor(Context.class);
            XinQuVideoPlayer xinQuVideoPlayer = constructor.newInstance(getContext());
            xinQuVideoPlayer.setId(R.id.xinqu_fullscreen_id);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            vp.addView(xinQuVideoPlayer, lp);
            xinQuVideoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);
            xinQuVideoPlayer.setUp(dataSourceObjects, currentUrlMapIndex, SCREEN_WINDOW_FULLSCREEN,loop, objects);
            xinQuVideoPlayer.setState(currentState);
            xinQuVideoPlayer.addTextureView();
            XinQuVideoPlayerManager.setSecondFloor(xinQuVideoPlayer);
//            final Animation ra = AnimationUtils.loadAnimation(getContext(), R.anim.start_fullscreen);
//            jzVideoPlayer.setAnimation(ra);
            onStateNormal();
            xinQuVideoPlayer.progressBar.setSecondaryProgress(progressBar.getSecondaryProgress());
            xinQuVideoPlayer.startProgressTimer();
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startWindowTiny() {
        Log.i(TAG, "startWindowTiny " + " [" + this.hashCode() + "] ");
        onEvent(XinQuUserAction.ON_ENTER_TINYSCREEN);
        if (currentState == CURRENT_STATE_NORMAL || currentState == CURRENT_STATE_ERROR || currentState == CURRENT_STATE_AUTO_COMPLETE)
            return;
        ViewGroup vp = (ViewGroup) (XinQuUtils.scanForActivity(getContext())).getWindow().getDecorView();//.findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(R.id.xinqu_tiny_id);
        if (old != null) {
            vp.removeView(old);
        }
        textureViewContainer.removeView(XinQuMediaManager.textureView);

        try {
            Constructor<XinQuVideoPlayer> constructor = (Constructor<XinQuVideoPlayer>) XinQuVideoPlayer.this.getClass().getConstructor(Context.class);
            XinQuVideoPlayer xinQuVideoPlayer = constructor.newInstance(getContext());
            xinQuVideoPlayer.setId(R.id.xinqu_tiny_id);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(400, 400);
            lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            vp.addView(xinQuVideoPlayer, lp);
            xinQuVideoPlayer.setUp(dataSourceObjects, currentUrlMapIndex, SCREEN_WINDOW_TINY, loop,objects);
            xinQuVideoPlayer.setState(currentState);
            xinQuVideoPlayer.addTextureView();
            XinQuVideoPlayerManager.setSecondFloor(xinQuVideoPlayer);
            onStateNormal();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 根据视频的宽高开启小窗口播放
     */
    public void startWindowTiny(int videoRatio) {

        onEvent(XinQuUserAction.ON_ENTER_TINYSCREEN);
        if (currentState == CURRENT_STATE_NORMAL || currentState == CURRENT_STATE_ERROR || currentState == CURRENT_STATE_AUTO_COMPLETE)
            return;
        ViewGroup vp = (ViewGroup) (XinQuUtils.scanForActivity(getContext())).getWindow().getDecorView();//.findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(R.id.xinqu_tiny_id);
        if (old != null) {
            vp.removeView(old);
        }

        textureViewContainer.removeView(XinQuMediaManager.textureView);
        try {
            Constructor<XinQuVideoPlayer> constructor = (Constructor<XinQuVideoPlayer>) XinQuVideoPlayer.this.getClass().getConstructor(Context.class);
            XinQuVideoPlayer xinQuVideoPlayer = constructor.newInstance(getContext());
            xinQuVideoPlayer.setId(R.id.xinqu_tiny_id);
            int videoWidget=130;
            int videoHeight=130;
            switch (videoRatio) {
                //宽
                case 1:
                    videoWidget=160;
                    videoHeight=90;
                    break;
                //长
                case 2:
                    videoWidget=130;
                    videoHeight=190;
                    break;
                //正
                case 3:
                    videoWidget=130;
                    videoHeight=130;
                    break;
            }
            Log.d(TAG,"startWindowTiny");
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(XinQuUtils.dip2px(getContext(),videoWidget), XinQuUtils.dip2px(getContext(),videoHeight));//初始视频的宽高
            lp.gravity = Gravity.RIGHT | Gravity.NO_GRAVITY;
            lp.topMargin=XinQuUtils.dip2px(getContext(),80);
            lp.rightMargin=XinQuUtils.dip2px(getContext(),10);//距离顶部和右边各5个DP
            xinQuVideoPlayer.startAnimation(moveToViewTopLocation());
            vp.addView(xinQuVideoPlayer, lp);
            xinQuVideoPlayer.setUp(dataSourceObjects, currentUrlMapIndex, SCREEN_WINDOW_TINY,loop, objects);
            xinQuVideoPlayer.setState(currentState);
            xinQuVideoPlayer.addTextureView();
            XinQuVideoPlayerManager.setSecondFloor(xinQuVideoPlayer);
            onStateNormal();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 从控件的顶部移动到控件所在位置
     * 从上往下进场
     * @return
     */
    private TranslateAnimation moveToViewTopLocation() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(90);
        return mHiddenAction;
    }


    /**
     * 从控件所在位置移动到控件的顶部
     *  从下往下上出场
     * @return
     */
    private TranslateAnimation moveToViewTop() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        mHiddenAction.setDuration(70);
        return mHiddenAction;
    }


    public boolean isCurrentPlay() {
        return isCurrentJZVD()
                && XinQuUtils.dataSourceObjectsContainsUri(dataSourceObjects, XinQuMediaManager.getCurrentDataSource());//不仅正在播放的url不能一样，并且各个清晰度也不能一样
    }

    public boolean isCurrentJZVD() {
        return XinQuVideoPlayerManager.getCurrentJzvd() != null
                && XinQuVideoPlayerManager.getCurrentJzvd() == this;
    }

    //退出全屏和小窗的方法
    public void playOnThisJzvd() {
        Log.i(TAG, "playOnThisJzvd " + " [" + this.hashCode() + "] ");
        //1.清空全屏和小窗的jzvd
        currentState = XinQuVideoPlayerManager.getSecondFloor().currentState;
        currentUrlMapIndex = XinQuVideoPlayerManager.getSecondFloor().currentUrlMapIndex;
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
            onEvent(XinQuUserAction.ON_ENTER_FULLSCREEN);
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

    public static void setMediaInterface(XinQuMediaInterface mediaInterface) {
        XinQuMediaManager.instance().mXinQuMediaInterface = mediaInterface;
    }

    //TODO 是否有用
    public void onSeekComplete() {

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

    public static class XinQuAutoFullscreenListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {//可以得到传感器实时测量出来的变化值
            final float x = event.values[SensorManager.DATA_X];
            float y = event.values[SensorManager.DATA_Y];
            float z = event.values[SensorManager.DATA_Z];
            //过滤掉用力过猛会有一个反向的大数值
            if (((x > -15 && x < -10) || (x < 15 && x > 10)) && Math.abs(y) < 1.5) {
                if ((System.currentTimeMillis() - lastAutoFullscreenTime) > 2000) {
                    if (XinQuVideoPlayerManager.getCurrentJzvd() != null) {
                        XinQuVideoPlayerManager.getCurrentJzvd().autoFullscreen(x);
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
        void callBack();
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


}
