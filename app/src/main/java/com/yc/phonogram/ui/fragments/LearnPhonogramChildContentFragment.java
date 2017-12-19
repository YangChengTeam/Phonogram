package com.yc.phonogram.ui.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.xinqu.videoplayer.XinQuVideoPlayerStandard;
import com.yc.phonogram.R;
import com.yc.phonogram.adapter.LPContentListAdapter;
import com.yc.phonogram.domain.PhonogramInfo;

/**
 * TinyHung@Outlook.com
 * 2017/12/18.
 */

public class LearnPhonogramChildContentFragment extends BaseFragment {

    private static final String TAG = LearnPhonogramChildContentFragment.class.getSimpleName();
    private PhonogramInfo data;
    private LPContentListAdapter mLpContentListAdapter;
    private XinQuVideoPlayerStandard mVidepPlayer;
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
        mVidepPlayer = (XinQuVideoPlayerStandard) getView(R.id.video_player);
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

        mVidepPlayer.setUp("http://video.nq6.com/user-dir/WerHjD25xK.mp4", XinQuVideoPlayer.SCREEN_WINDOW_LIST,true,"测试");

        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.main_bg);
        options.error(R.mipmap.main_bg);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);//缓存源资源和转换后的资源
        options.skipMemoryCache(true);//跳过内存缓存
        Log.d(TAG,"data.getImg()"+data.getImg());
        Glide.with(this).load(data.getImg()).apply(options).thumbnail(0.1f).into(mIv_lp_logo);
//        Glide.with(this).load(data.getCover()).apply(options).thumbnail(0.1f).into(mVidepPlayer.thumbImageView);
        mTv_lp_tips_content.setText(data.getDesp());
        if(null!=mLpContentListAdapter&&null!=data&&null!=data.getExampleInfos()){
            mLpContentListAdapter.setNewData(data.getExampleInfos());
        }
    }
}
