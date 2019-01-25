package com.yc.phonogram.ui.popupwindow;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.loading.LoadingDialog;
import com.kk.share.UMShareImpl;
import com.kk.utils.ToastUtil;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yc.phonogram.App;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.LoginDataInfo;
import com.yc.phonogram.domain.ShareInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/12/15.
 */

public class SharePopupWindow extends BasePopupWindow {

    private LoadingDialog loadingDialog;
    private ShareInfo mShareInfo;

    public SharePopupWindow(Activity context) {
        super(context);
        loadingDialog = new LoadingDialog(context);
        LoginDataInfo loginDataInfo = App.getApp().getLoginDataInfo();
        if (loginDataInfo != null)
            mShareInfo = loginDataInfo.getShareInfo();
    }

    @Override
    public int getLayoutId() {
        return R.layout.popwindow_share_view;
    }

    @Override
    public void init() {
        setAnimationStyle(R.style.popwindow_style);
        ImageButton mIbWx = (ImageButton) getView(R.id.ib_wx);
        ImageButton mIbQq = (ImageButton) getView(R.id.ib_qq);
        ImageButton mIbCircle = (ImageButton) getView(R.id.ib_circle);
        ImageButton mIbZone = (ImageButton) getView(R.id.ib_zone);
        TextView mTvCancel = (TextView) getView(R.id.tv_cancel);

        final List<View> textViewList = new ArrayList<>();
        textViewList.add(mIbWx);
        textViewList.add(mIbQq);
        textViewList.add(mIbCircle);
        textViewList.add(mIbZone);

        RxView.clicks(mTvCancel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
        for (int i = 0; i < textViewList.size(); i++) {
            final String tempI = i + "";
            RxView.clicks(textViewList.get(i)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    try {
                        shareInfo(tempI);
                    } catch (Exception e) {
                        Log.e("TAG", "call: " + e.getMessage());
                        ToastUtil.toast(mContext, "分享失败");
                    }

                }
            });
        }


    }


    private void shareInfo(String tag) {
        String title = "小学英语音标APP上线啦！随时随地想学就学";
        String url = "http://android.myapp.com/myapp/detail.htm?apkName=com.yc.phonogram";
        String desc = "小学英语自营首款APP学音标软件上线了，涵盖市面所有主流英语教材，配套各种版本教科书（完全免费），让你随时随地就能通过手机打开书本，进行学习，单词记忆。还有各种趣味方式助你学英语。";

        if (mShareInfo != null) {
            if (!TextUtils.isEmpty(mShareInfo.getTitle())) {
                title = mShareInfo.getTitle();
            }
            if (!TextUtils.isEmpty(mShareInfo.getDesc())) {
                desc = mShareInfo.getDesc();
            }
            if (!TextUtils.isEmpty(mShareInfo.getUrl())) {
                url = mShareInfo.getUrl();
            }
        }
        dismiss();
        UMShareImpl.get().setCallback(mContext, listener).shareUrl(title, url,
                desc, R.mipmap.icon, getShareMedia(tag + ""));

    }


    private SHARE_MEDIA getShareMedia(String tag) {
        if (tag.equals("0")) {
            return SHARE_MEDIA.WEIXIN;
        }
        if (tag.equals("1")) {
            return SHARE_MEDIA.QQ;
        }
        if (tag.equals("2")) {
            return SHARE_MEDIA.WEIXIN_CIRCLE;
        }
        if (tag.equals("3")) {
            return SHARE_MEDIA.QZONE;
        }
        return SHARE_MEDIA.WEIXIN;
    }

    private UMShareListener listener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            loadingDialog.show("正在分享");

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            ToastUtil.toast(mContext, "分享成功");
            loadingDialog.dismiss();

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            ToastUtil.toast(mContext, "分享失败");
            loadingDialog.dismiss();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            ToastUtil.toast(mContext, "取消分享");
            loadingDialog.dismiss();
        }
    };
}