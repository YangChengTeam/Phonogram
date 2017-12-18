package com.yc.phonogram.ui.views;

import android.app.Fragment;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ScreenUtil;
import com.yc.phonogram.R;

import org.w3c.dom.Text;

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
    private FrameLayout mContextFrameLayout;

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
        mIndexRelativeLayout = (RelativeLayout) getView(R.id.rl_index);
        mInnerBgImageView = (ImageView) getView(R.id.iv_inner_bg);
        mLeftImageView = (ImageView) getView(R.id.iv_left);
        mRightImageView = (ImageView) getView(R.id.iv_right);
        mIndexTextView = (TextView) getView(R.id.tv_index);
        mContextFrameLayout = (FrameLayout) getView(R.id.fl_content);

        RxView.clicks(mLeftImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (indexListener != null) {
                    indexListener.leftClick(mLeftImageView);
                }
            }
        });

        RxView.clicks(mRightImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (indexListener != null) {
                    indexListener.rightClcik(mRightImageView);
                }
            }
        });
    }

    private int count;

    public void showIndex(int count) {
        this.count = count;
        mIndexRelativeLayout.setVisibility(View.VISIBLE);
    }

    public void hideLeft() {
        mLeftImageView.setVisibility(View.GONE);
    }

    public void showLeft() {
        mLeftImageView.setVisibility(View.VISIBLE);
    }

    public void hideRight() {
        mRightImageView.setVisibility(View.GONE);
    }

    public void showRight() {
        mRightImageView.setVisibility(View.VISIBLE);
    }

    public void setIndex(int index) {
        mIndexTextView.setText(index + "/" + count);
    }

    public void showInnerBg() {
        mInnerBgImageView.setVisibility(View.VISIBLE);
    }

    private IndexListener indexListener;

    public void setIndexListener(IndexListener indexListener) {
        this.indexListener = indexListener;
    }

    public interface IndexListener {
        void leftClick(View view);

        void rightClcik(View view);
    }
}
