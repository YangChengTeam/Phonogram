package com.yc.phonogram.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.phonogram.R;

/**
 * Created by suns  on 2020/4/3 09:38.
 */
public class BaseUserView extends BaseView {
    Drawable mDrawable;
    String mTvName;
    private ImageView ivICon;
    private TextView tvName;

    public BaseUserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseUserView);

        try {
            mDrawable = ta.getDrawable(R.styleable.BaseUserView_icon);
            mTvName = ta.getString(R.styleable.BaseUserView_name);
            if (mDrawable != null) {
                ivICon.setImageDrawable(mDrawable);
            }
            if (!TextUtils.isEmpty(mTvName)) {
                tvName.setText(mTvName);
            }
        } finally {
            ta.recycle();
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.view_user;
    }

    @Override
    public void init() {
        ivICon = (ImageView) getView(R.id.iv_icon);
        tvName = (TextView) getView(R.id.tv_name);

    }


}
