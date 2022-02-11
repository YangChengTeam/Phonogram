package com.yc.phonogram.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.umeng.analytics.MobclickAgent;
import com.yc.phonogram.App;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.Config;
import com.yc.phonogram.domain.LoginDataInfo;
import com.yc.phonogram.helper.SharePreferenceUtils;
import com.yc.phonogram.helper.UserInfoHelper;
import com.yc.phonogram.ui.activitys.AdvInfoActivity;
import com.yc.phonogram.ui.popupwindow.VipPopupWindow;
import com.yc.phonogram.ui.views.MainBgView;
import com.yc.phonogram.utils.LPUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import androidx.core.content.FileProvider;
import rx.functions.Action1;
import yc.com.rthttplibrary.util.LogUtil;
import yc.com.rthttplibrary.util.ToastUtil;

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

        if (!SharePreferenceUtils.getInstance().getBoolean(Config.index_dialog)) {
            IndexDialogFragment indexDialogFragment = new IndexDialogFragment();
            indexDialogFragment.show(getChildFragmentManager(), "");

        }
        mainBgView = (MainBgView) getView(R.id.mainBgView);
        mainBgView.showInnerBg();
        ivGifShow = (ImageView) getView(R.id.iv_gif_show);
        ivDownEnglish = (ImageView) getView(R.id.iv_down_english);

        LoginDataInfo loginDataInfo = App.getApp().getLoginDataInfo();

        if (loginDataInfo != null && loginDataInfo.getStatusInfo() != null) {
            String uid = UserInfoHelper.getUid();
            if (TextUtils.isEmpty(uid)) uid = loginDataInfo.getStatusInfo().getUid();
            ((TextView) getView(R.id.tv_user)).setText("用户ID: SE" + uid);
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
//                downEnglishFile();
//                toMarket("");
                toWebView();
//                openWebsite();
            }
        });
    }

    @Override
    public void loadData() {

    }

    private void toWebView() {
        Intent intent = new Intent(getActivity(), AdvInfoActivity.class);
        startActivity(intent);
    }


    /**
     * @param market 华为 com.huawei.appmarket
     *               小米 http://app.mi.com/
     */
    public void toMarket(String market) {
        String appPkg = "com.yc.english";
//        String marketPkg = "com.huawei.appmarket";
        try {

            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(market)) {
                intent.setPackage(market);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
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
