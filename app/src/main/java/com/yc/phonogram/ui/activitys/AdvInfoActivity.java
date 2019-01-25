package com.yc.phonogram.ui.activitys;

import android.os.Handler;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.AdvInfo;
import com.yc.phonogram.domain.AdvInfoWrapper;
import com.yc.phonogram.engin.AdvEngine;
import com.yc.phonogram.ui.widget.CommonWebView;

import rx.Subscriber;

/**
 * Created by wanglin  on 2019/1/25 10:05.
 */
public class AdvInfoActivity extends BaseActivity {
    private ImageView ivBack;

    private ProgressBar progressBar;
    private CommonWebView wvMain;
    private Handler mHandler;


    @Override
    public int getLayoutId() {
        return R.layout.activity_adv;
    }

    @Override
    public void init() {
        ivBack = findViewById(R.id.iv_back);
        progressBar = findViewById(R.id.progress_bar);
        wvMain = findViewById(R.id.commonWebView);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getAdvInfo();
    }

    private void initWebview(AdvInfo advInfo) {

        mHandler = new Handler();


        if (null != advInfo) {
            String url = advInfo.getUrl();

//        url += "?time=" + System.currentTimeMillis();


//        wvMain.addJavascriptInterface(new JavascriptInterface(), "study");


//        wvMain.loadUrl("file:///android_asset/m/ssyy.html");
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

    }

    public void getAdvInfo() {
        progressBar.setMax(100);
        AdvEngine advEngine = new AdvEngine(this);
        advEngine.getAdvInfo().subscribe(new Subscriber<ResultInfo<AdvInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<AdvInfoWrapper> advInfoWrapperResultInfo) {
                if (advInfoWrapperResultInfo != null && advInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && advInfoWrapperResultInfo.data != null) {
                    initWebview(advInfoWrapperResultInfo.data.getH5page());
                }
            }
        });
    }
}
