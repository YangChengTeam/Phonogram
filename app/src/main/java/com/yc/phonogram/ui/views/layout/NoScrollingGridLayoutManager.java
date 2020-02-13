package com.yc.phonogram.ui.views.layout;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * TinyHung@Outlook.com
 * 2017/12/27.
 * 禁止横向/竖向滑动阴影
 */

public class NoScrollingGridLayoutManager extends GridLayoutManager {

    public NoScrollingGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context,spanCount,orientation,reverseLayout);
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
