package com.yc.phonogram.ui.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yc.phonogram.R;
import com.yc.phonogram.adapter.LPContentListAdapter;
import com.yc.phonogram.domain.PhonogramInfo;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * TinyHung@Outlook.com
 * 2017/12/18.
 */

public class LearnPhonogramChildContentFragment extends BaseFragment {

    private PhonogramInfo data;
    private LPContentListAdapter mLpContentListAdapter;
    private JZVideoPlayerStandard mVidepPlayer;
    private ImageView mIv_lp_logo;
    private TextView mTv_lp_tips_content;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_learn_child_content;
    }

    @Override
    public void init() {
        //封面
        mIv_lp_logo = (ImageView) getView(R.id.iv_lp_logo);
        mTv_lp_tips_content = (TextView) getView(R.id.tv_lp_tips_content);
        mVidepPlayer = (JZVideoPlayerStandard) getView(R.id.video_player);
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

        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.main_bg);
        options.error(R.mipmap.main_bg);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);//缓存源资源和转换后的资源
        options.skipMemoryCache(true);//跳过内存缓存
        Glide.with(this).load(data.getImg()).apply(options).thumbnail(0.1f).into(mIv_lp_logo);
        Glide.with(this).load(data.getCover()).apply(options).thumbnail(0.1f).into(mVidepPlayer.thumbImageView);

        mVidepPlayer.setUp("http://video.nq6.com/user-dir/WerHjD25xK.mp4", JZVideoPlayer.SCREEN_WINDOW_LIST,"");
        mTv_lp_tips_content.setText(data.getDesp());

        if(null!=mLpContentListAdapter&&null!=data&&null!=data.getExampleInfos()){
            mLpContentListAdapter.setNewData(data.getExampleInfos());
        }
    }
}
