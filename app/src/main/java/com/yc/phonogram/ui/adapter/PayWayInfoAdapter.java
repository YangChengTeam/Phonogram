package com.yc.phonogram.ui.adapter;

import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.GoodInfo;
import com.yc.phonogram.ui.activitys.MainActivity;

import java.util.List;

/**
 * Created by wanglin  on 2017/12/18 17:04.
 */

public class PayWayInfoAdapter extends BaseQuickAdapter<GoodInfo, BaseViewHolder> {

    private SparseArray<ImageView> sparseArray;


    public PayWayInfoAdapter(List<GoodInfo> data) {
        super(R.layout.popwindow_good_info_item, data);
        sparseArray = new SparseArray<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodInfo item) {
        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_sub_title, item.getSub_title()).setText(R.id.tv_origin_price, String.format(mContext.getString(R.string.origin_price), item.getPrice()))
                .setText(R.id.tv_current_price, String.format(mContext.getString(R.string.current_price), item.getReal_price()))
                .setVisible(R.id.tv_sub_title, !TextUtils.isEmpty(item.getSub_title()));
        Glide.with(mContext).load(item.getIcon()).into((ImageView) helper.getView(R.id.iv_num));

        final ImageView imageView = helper.getView(R.id.iv_select);
        int position = mData.indexOf(item);
        sparseArray.put(position, imageView);
        setIvState(imageView, position);
    }


    public ImageView getIv(int position) {
        return sparseArray.get(position);
    }


    private void setIvState(ImageView imageView, int position) {
        if (MainActivity.getMainActivity().isSuperVip()) {
            imageView.setImageResource(R.mipmap.pay_selected);
            imageView.setTag(true);
            return;
        }

        if (MainActivity.getMainActivity().isPhonogramOrPhonicsVip() || (MainActivity.getMainActivity().isPhonicsVip() && MainActivity.getMainActivity().isPhonogramVip())) {
            imageView.setImageResource(R.mipmap.pay_selected);
            imageView.setTag(true);
            if (position == mData.size() - 1) {
                imageView.setImageResource(R.mipmap.pay_select_press);
                imageView.setTag(false);
            }
            return;
        }

        if (MainActivity.getMainActivity().isPhonogramVip()) {
            imageView.setImageResource(R.mipmap.pay_select_normal);
            if (position == 1) {
                imageView.setImageResource(R.mipmap.pay_select_press);
            }
            imageView.setTag(false);
            if (position == 0) {
                imageView.setImageResource(R.mipmap.pay_selected);
                imageView.setTag(true);
            }
            return;

        }

        if (MainActivity.getMainActivity().isPhonicsVip()) {
            imageView.setImageResource(R.mipmap.pay_select_normal);
            if (position == 0) {
                imageView.setImageResource(R.mipmap.pay_select_press);
            }
            imageView.setTag(false);
            if (position == 1) {
                imageView.setImageResource(R.mipmap.pay_selected);
                imageView.setTag(true);
            }
            return;
        }

        if (position == 0) {
            imageView.setImageResource(R.mipmap.pay_select_press);
        } else {
            imageView.setImageResource(R.mipmap.pay_select_normal);
        }
        imageView.setTag(false);

    }

}
