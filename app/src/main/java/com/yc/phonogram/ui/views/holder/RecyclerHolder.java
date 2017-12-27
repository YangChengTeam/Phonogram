package com.yc.phonogram.ui.views.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yc.phonogram.R;

/**
 * TinyHung@Outlook.com
 * 2017/12/27.
 * 音标的Adapter
 */

public class RecyclerHolder extends RecyclerView.ViewHolder {

    public TextView tv_item_content;
    public TextView tv_item_content_lp;
    public ImageView progressLoad;
    public LinearLayout ll_item;

    public RecyclerHolder(View convertView) {
        super(convertView);

        tv_item_content=convertView.findViewById(R.id.tv_item_content);
        tv_item_content_lp=convertView.findViewById(R.id.tv_item_content_lp);
        progressLoad=convertView.findViewById(R.id.progress_load);
        ll_item=convertView.findViewById(R.id.ll_item);
    }
}
