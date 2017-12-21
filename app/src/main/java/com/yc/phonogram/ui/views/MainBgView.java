package com.yc.phonogram.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.phonogram.R;
import com.yc.phonogram.helper.SeekBarHelper;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/12/18.
 */

public class MainBgView extends BaseView {
    private ImageView mInnerBgImageView;
    private RelativeLayout mIndexRelativeLayout;
    private ImageView mLeftImageView;
    private ImageView mRightImageView;
    private TextView mIndexTextView;

    private SeekBarHelper seekBarHelper;

    public MainBgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainBgView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_main_bg;
    }

    @Override
    public void init() {
        seekBarHelper = new SeekBarHelper();
        mIndexRelativeLayout = (RelativeLayout) getView(R.id.rl_index);
        mInnerBgImageView = (ImageView) getView(R.id.iv_inner_bg);
        mLeftImageView = (ImageView) getView(R.id.iv_left);
        mRightImageView = (ImageView) getView(R.id.iv_right);
        mIndexTextView = (TextView) getView(R.id.tv_index);

        seekBarHelper.init(mLeftImageView, mRightImageView, mIndexTextView, mIndexRelativeLayout);

    }

    public void showInnerBg() {
        mInnerBgImageView.setVisibility(View.VISIBLE);
    }


    public void showIndex(int count) {
        seekBarHelper.showIndex(count);
    }

    public void setIndex(int index) {
        seekBarHelper.setIndex(index);
    }

    public int getIndex() {
        return seekBarHelper.getIndex();
    }

    public void setIndexListener(SeekBarHelper.IndexListener indexListener) {
        seekBarHelper.setIndexListener(indexListener);
    }

}