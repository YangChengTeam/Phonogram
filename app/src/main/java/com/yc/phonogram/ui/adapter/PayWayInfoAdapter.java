package com.yc.phonogram.ui.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.GoodItemInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/12/18 17:04.
 */

public class PayWayInfoAdapter extends BaseQuickAdapter<GoodItemInfo, BaseViewHolder> {
    public PayWayInfoAdapter(List<GoodItemInfo> data) {
        super(R.layout.popwindow_good_info_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodItemInfo item) {
        helper.setImageResource(R.id.iv_num, item.getIconId()).setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_sub_title, item.getSubTitle()).setText(R.id.tv_origin_price, item.getOriginPrice())
                .setText(R.id.tv_current_price, item.getCurrentPrice())
                .setVisible(R.id.tv_sub_title, !TextUtils.isEmpty(item.getSubTitle()));
    }
}
