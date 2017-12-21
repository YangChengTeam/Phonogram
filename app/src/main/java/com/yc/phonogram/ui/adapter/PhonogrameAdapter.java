package com.yc.phonogram.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.PhonogramInfo;

import java.util.List;

/**
 * Created by zhangkai on 2017/12/21.
 */

public class PhonogrameAdapter extends BaseQuickAdapter<PhonogramInfo, BaseViewHolder> {

    public PhonogrameAdapter(List<PhonogramInfo> data) {
        super(R.layout.item_phonograme, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PhonogramInfo item) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_player_error);
        options.error(R.mipmap.ic_player_error);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.skipMemoryCache(true);

        Glide.with(mContext).load(item.getImg()).apply(options).thumbnail(0.1f).into((ImageView) helper.getView(R.id
                .iv_phonograme));
    }
}