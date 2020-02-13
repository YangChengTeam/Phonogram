package com.yc.phonogram.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.PhonogramInfo;
import com.yc.phonogram.utils.EmptyUtils;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;


/**
 * FragmentPager适配器
 */

public class ReadItemPagerAdapter extends PagerAdapter {

    private List<PhonogramInfo> phonogramInfos;

    private Activity mContext;

    public ReadItemPagerAdapter(Activity context, List<PhonogramInfo> lists) {
        this.mContext = context;
        this.phonogramInfos = lists;
    }

    public void setPhonogramInfos(List<PhonogramInfo> phonogramInfos) {
        this.phonogramInfos = phonogramInfos;
    }

    @Override
    public int getCount() {
        return null == phonogramInfos ? 0 : phonogramInfos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhonogramInfo phonogramInfo = phonogramInfos.get(position);
        if (null != phonogramInfo) {
            View rootView = mContext.getLayoutInflater().inflate(R.layout.read_item_page, null);
            ImageView piImageView = (ImageView) rootView.findViewById(R.id.iv_phonetic);
            if (!EmptyUtils.isEmpty(phonogramInfo.getImg())) {
                Glide.with(mContext).load(phonogramInfo.getImg()).into(piImageView);
            }
            container.addView(rootView);
            return rootView;
        }
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(container.findViewById(position));
    }

}
