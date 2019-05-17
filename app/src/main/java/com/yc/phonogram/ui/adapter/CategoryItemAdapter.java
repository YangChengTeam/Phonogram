package com.yc.phonogram.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.CategoryInfo;
import com.yc.phonogram.ui.activitys.MainActivity;

import java.util.List;

/**
 * Created by wanglin  on 2019/5/15 09:48.
 */
public class CategoryItemAdapter extends BaseQuickAdapter<CategoryInfo, BaseViewHolder> {
    public CategoryItemAdapter(List<CategoryInfo> data) {
        super(R.layout.category_item_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryInfo item) {
        if (MainActivity.getMainActivity().isPhonicsVip()){
            helper.setVisible(R.id.iv_category_vip,false);
        }else {
            helper.setVisible(R.id.iv_category_vip, item.getIs_free() == 1);
        }


        Glide.with(mContext).load(item.getIcon()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA).skipMemoryCache(false))
                .thumbnail(0.1f).into((ImageView) helper.getView(R.id.iv_category_icon));
    }
}
