package com.yc.phonogram.ui.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kk.utils.ToastUtil;
import com.yc.phonogram.R;
import com.yc.phonogram.ui.widget.CommonWebView;

/**
 * Created by wanglin  on 2019/1/25 10:05.
 */
public class AdvInfoActivity extends AppCompatActivity {
    private ImageView ivBack;

    private ProgressBar progressBar;
    private CommonWebView wvMain;
    private Handler mHandler;
    private String url;
    private TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv);
        init();
    }


    public void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        ivBack = findViewById(R.id.iv_back);
        progressBar = findViewById(R.id.progress_bar);
        tvTitle = findViewById(R.id.tv_title);
        wvMain = findViewById(R.id.commonWebView);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initWebview();

    }

    private void initWebview() {

        mHandler = new Handler();

        if (getIntent() != null) {
            url = getIntent().getStringExtra("url");
            String title = getIntent().getStringExtra("title");
            tvTitle.setText(title);
        }


//        url += "?time=" + System.currentTimeMillis();


        wvMain.addJavascriptInterface(new JavaInterface(), "html");


//        wvMain.loadUrl("file:///android_asset/m/ssyy.html");
        progressBar.setMax(100);
        wvMain.loadUrl(url);
        wvMain.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

            }

        });
        wvMain.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                final int progress = newProgress;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (progressBar.getVisibility() == ProgressBar.GONE) {
                            progressBar.setVisibility(ProgressBar.VISIBLE);
                        }
                        progressBar.setProgress(progress);
                        progressBar.postInvalidate();
                        if (progress == 100) {
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });


            }
        });
    }


    public class JavaInterface {

        @JavascriptInterface
        public void gotoMarket() {
            try {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.toast2(AdvInfoActivity.this, "你手机安装的应用市场没有上线该应用，请前往其他应用市场进行点评");
            }
        }
    }


}
