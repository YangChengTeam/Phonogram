package com.yc.phonogram.ui.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.GoodInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/12/18 17:04.
 */

public class PayWayInfoAdapter extends BaseQuickAdapter<GoodInfo, BaseViewHolder> {
    public PayWayInfoAdapter(List<GoodInfo> data) {
        super(R.layout.popwindow_good_info_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodInfo item) {
        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_sub_title, item.getSub_title()).setText(R.id.tv_origin_price, String.format(mContext.getString(R.string.origin_price), item.getPrice()))
                .setText(R.id.tv_current_price, String.format(mContext.getString(R.string.current_price), item.getReal_price()))
                .setVisible(R.id.tv_sub_title, !TextUtils.isEmpty(item.getSub_title()));
        Glide.with(mContext).load(item.getIcon()).into((ImageView) helper.getView(R.id.iv_num));
    }
}
