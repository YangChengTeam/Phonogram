package com.yc.phonogram.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.phonogram.R;
import java.util.concurrent.TimeUnit;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/12/18.
 */

public class PhoniceSeekBarView extends BaseView {

    private ImageView mLeftImageView;
    private ImageView mRightImageView;
    private TextView mIndexTextView;

    public PhoniceSeekBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhoniceSeekBarView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_phonice_seekbar;
    }

    @Override
    public void init() {
        mLeftImageView = (ImageView) getView(R.id.iv_left);
        mRightImageView = (ImageView) getView(R.id.iv_right);
        mIndexTextView = (TextView) getView(R.id.tv_index);

        RxView.clicks(mLeftImageView).throttleFirst(300, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if(count<=0) return;
                if (indexListener != null && index >= 0) {
                    index = index - 1;
                    indexListener.leftClick(index);
                }
            }
        });

        RxView.clicks(mRightImageView).throttleFirst(300, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if(count<=0) return;
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
        if(count<=0) return;
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