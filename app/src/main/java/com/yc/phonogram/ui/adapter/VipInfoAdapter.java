package com.yc.phonogram.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.VipShowInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/12/18 17:04.
 */

public class VipInfoAdapter extends BaseQuickAdapter<VipShowInfo, BaseViewHolder> {

    public VipInfoAdapter(List<VipShowInfo> data) {
        super(R.layout.popwindow_vip_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VipShowInfo item) {
        helper.setText(R.id.vip_title, item.getVipTitle())
                .setText(R.id.vip_content, item.getVipContent());

    }
}
