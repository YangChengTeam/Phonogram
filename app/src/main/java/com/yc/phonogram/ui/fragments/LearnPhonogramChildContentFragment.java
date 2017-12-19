package com.yc.phonogram.ui.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.yc.phonogram.R;
import com.yc.phonogram.adapter.LPContentListAdapter;
import com.yc.phonogram.domain.PhonogramInfo;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * TinyHung@Outlook.com
 * 2017/12/18.
 */

public class LearnPhonogramChildContentFragment extends BaseFragment {

    private PhonogramInfo data;
    private LPContentListAdapter mLpContentListAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_learn_child_content;
    }

    @Override
    public void init() {
        ImageView iv_lp_logo = (ImageView) getView(R.id.iv_lp_logo);
        JZVideoPlayerStandard videoPlayerStandard = (JZVideoPlayerStandard) getView(R.id.video_player);
        RecyclerView recyclerView = (RecyclerView) getView(R.id.recyclerview_lp);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        mLpContentListAdapter = new LPContentListAdapter(getActivity(),null);
        recyclerView.setAdapter(mLpContentListAdapter);
    }

    /**
     * 入参
     * @param data
     * @return
     */
    public static LearnPhonogramChildContentFragment newInstance(PhonogramInfo data) {
        LearnPhonogramChildContentFragment childContentFragment=new LearnPhonogramChildContentFragment();
        childContentFragment.data = data;
        return childContentFragment;
    }

    @Override
    public void loadData() {
        super.loadData();
        if(null==data)return;

    }
}
