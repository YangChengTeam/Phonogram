package com.yc.phonogram.helper;

import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/12/21.
 */

public class SeekBarHelper {

    private View mLeftImageView;
    private View mRightImageView;
    private TextView mIndexTextView;
    private View mIndexRelativeLayout;

    public void init(View mLeftImageView, View mRightImageView, TextView mIndexTextView, View mIndexRelativeLayout) {
        this.mLeftImageView = mLeftImageView;
        this.mRightImageView = mRightImageView;
        this.mIndexTextView = mIndexTextView;
        this.mIndexRelativeLayout = mIndexRelativeLayout;

        RxView.clicks(mLeftImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (indexListener != null && index >= 0) {
                    index = index - 1;
                    indexListener.leftClick(index);
                }
            }
        });

        RxView.clicks(mRightImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (indexListener != null && index < count - 1) {
                    index = index + 1;
                    indexListener.rightClcik(index);
                }
            }
        });
    }

    private int count;
    private int index;

    private void hideLeft() {
        mLeftImageView.setVisibility(View.INVISIBLE);
    }

    private void showLeft() {
        if (mLeftImageView.getVisibility() != View.VISIBLE)
            mLeftImageView.setVisibility(View.VISIBLE);
    }

    private void hideRight() {
        mRightImageView.setVisibility(View.INVISIBLE);
    }

    private void showRight() {
        if (mRightImageView.getVisibility() != View.VISIBLE)
            mRightImageView.setVisibility(View.VISIBLE);
    }

    public int getIndex() {
        return index;
    }

    public void showIndex(int count) {
        index = 0;
        this.count = count;
        if (mIndexRelativeLayout != null) {
            mIndexRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setIndex(int index) {
        if (index == 0) {
            hideLeft();
        } else if (index == count - 1) {
            hideRight();
        } else {
            showLeft();
            showRight();
        }
        this.index = index;
        mIndexTextView.setText((index + 1) + "/" + count);
    }

    private IndexListener indexListener;

    public void setIndexListener(IndexListener indexListener) {
        this.indexListener = indexListener;
    }

    public interface IndexListener {
        void leftClick(int position);

        void rightClcik(int position);
    }
}
