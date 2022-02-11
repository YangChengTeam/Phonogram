package com.yc.phonogram.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.loading.AnimationUtil;
import com.yc.phonogram.R;


import yc.com.rthttplibrary.view.BaseLoadingView;

/**
 * Created by suns  on 2020/7/28 15:07.
 */
public class LoadingView extends Dialog implements BaseLoadingView {
    ImageView ivCircle;
    TextView tvMsg;

    public LoadingView(Context context) {
        super(context, R.style.customDialog);
        View view = LayoutInflater.from(context).inflate(
                getLayoutID(), null);
        ivCircle = view.findViewById(R.id.iv_circle);
        tvMsg = view.findViewById(R.id.tv_msg);
        this.setContentView(view);
        this.setCancelable(true);
    }

    public void show(String msg) {
        super.show();
        ivCircle.startAnimation(AnimationUtil.rotaAnimation());
        tvMsg.setText(msg);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        ivCircle.clearAnimation();
    }

    public int getLayoutID() {
        return R.layout.dialog_loading;
    }

    @Override
    public void showLoading() {
        ivCircle.startAnimation(AnimationUtil.rotaAnimation());
        super.show();
    }

    @Override
    public void hideLoading() {
        ivCircle.clearAnimation();
        dismiss();
    }

    public void setText(String mess) {

        tvMsg.setText(mess);
    }
}
