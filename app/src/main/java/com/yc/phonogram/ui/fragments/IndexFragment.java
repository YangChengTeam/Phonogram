package com.yc.phonogram.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.umeng.analytics.MobclickAgent;
import com.yc.phonogram.App;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.LoginDataInfo;
import com.yc.phonogram.ui.activitys.MainActivity;
import com.yc.phonogram.ui.popupwindow.PayPopupWindow;
import com.yc.phonogram.ui.popupwindow.VipPopupWindow;
import com.yc.phonogram.ui.views.MainBgView;
import com.yc.phonogram.utils.LPUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/12/15.
 */

public class IndexFragment extends BaseFragment {
    private MainBgView mainBgView;
    private ImageView ivGifShow;

    private ImageView ivDownEnglish;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    public void init() {

        FileDownloader.setup(getActivity());

        mainBgView = (MainBgView) getView(R.id.mainBgView);
        mainBgView.showInnerBg();
        ivGifShow = (ImageView) getView(R.id.iv_gif_show);
        ivDownEnglish = (ImageView) getView(R.id.iv_down_english);

        LoginDataInfo loginDataInfo = App.getApp().getLoginDataInfo();
        if (loginDataInfo != null && loginDataInfo.getStatusInfo() != null) {
            ((TextView) getView(R.id.tv_user)).setText("用户ID: SE" + loginDataInfo.getStatusInfo().getUid());
        }
        Glide.with(getActivity()).load(R.mipmap.vip_show).into(ivGifShow);

        RxView.clicks(ivGifShow).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                VipPopupWindow vipPopupWindow = new VipPopupWindow(getActivity());
                vipPopupWindow.show();
            }
        });
        RxView.clicks(ivDownEnglish).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                MobclickAgent.onEvent(getActivity(), "down_english", "说说英语下载");
                //downEnglishFile();
                toHuaWei();
            }
        });
    }

    @Override
    public void loadData() {

    }

    public void toHuaWei() {
        String appPkg = "com.yc.phonogram";
        String marketPkg = "com.huawei.appmarket";
        try {

            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downEnglishFile() {

        String downDir = LPUtils.getInstance().getSDPath();

        LogUtil.msg("down File path --->" + downDir);


        //如果SD卡已挂载并且可读写
        if (downDir != null) {
            final String filePath = downDir + "/english.apk";
            FileDownloader.getImpl().create("http://en.upkao.com/english1.8.2.apk")
                    .setPath(filePath)
                    .setListener(new FileDownloadListener() {
                        @Override
                        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            ToastUtil.toast(getActivity(), "正在下载说说英语");
                        }

                        @Override
                        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        }

                        @Override
                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        }

                        @Override
                        protected void blockComplete(BaseDownloadTask task) {
                        }

                        @Override
                        protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                        }

                        @Override
                        protected void completed(BaseDownloadTask task) {
                            ToastUtil.toast(getActivity(), "下载完成");
                            install(filePath);
                        }

                        @Override
                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        }

                        @Override
                        protected void error(BaseDownloadTask task, Throwable e) {
                        }

                        @Override
                        protected void warn(BaseDownloadTask task) {
                        }
                    }).start();
        }
    }

    private void install(String filePath) {

        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(
                    getActivity()
                    , "com.yc.phonogram.fileprovider"
                    , apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }

}
