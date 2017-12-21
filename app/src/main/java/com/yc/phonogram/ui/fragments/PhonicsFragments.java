package com.yc.phonogram.ui.fragments;

import android.graphics.Paint;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kk.securityhttp.domain.ResultInfo;
import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.GoodInfo;
import com.yc.phonogram.domain.MClassInfo;
import com.yc.phonogram.domain.MClassListInfo;
import com.yc.phonogram.engin.MClassEngin;
import com.yc.phonogram.ui.pager.PhonicsVideoPager;
import com.yc.phonogram.ui.views.PhoniceSeekBarView;
import com.yc.phonogram.ui.widget.StrokeTextView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/12/15.
 * 首页自然拼读
 */

public class PhonicsFragments extends BaseFragment {

    private Map<Integer,PhonicsVideoPager> playerViews;
    private List<MClassInfo> mMClassInfos;
    private PhoniceVideoPlayerPagerAdapter mPlayerPagerAdapter;
    private PhoniceSeekBarView mPhonice_view;
    private ViewPager mView_pager;
    private TextView mTvOriPrice;
    private TextView mTvNewPrice;
    private TextView mTvPhDesp;
    private StrokeTextView mStrokeTitle;
    private GoodInfo mGoodInfo;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_phonics;
    }

    @Override
    public void init() {
        playerViews=new HashMap<>();
        mView_pager = (ViewPager) getView(R.id.view_pager);
        mPhonice_view = (PhoniceSeekBarView) getView(R.id.phonice_view);
        initPagerAdapter();
        mTvOriPrice = (TextView) getView(R.id.tv_ori_price);
        mTvNewPrice = (TextView) getView(R.id.tv_new_price);
        mTvPhDesp = (TextView) getView(R.id.tv_ph_desp);
        mStrokeTitle = (StrokeTextView) getView(R.id.stroke_title);
    }

    private void initPagerAdapter() {
        mPlayerPagerAdapter = new PhoniceVideoPlayerPagerAdapter();
        mView_pager.setAdapter(mPlayerPagerAdapter);
        mView_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                XinQuVideoPlayer.releaseAllVideos();
                mPhonice_view.setIndex(position);
                updataPhoniceContent(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPhonice_view.setIndexListener(new PhoniceSeekBarView.IndexListener() {
            @Override
            public void leftClick(int position) {
                mView_pager.setCurrentItem(position);
            }

            @Override
            public void rightClcik(int position) {
                mView_pager.setCurrentItem(position);
            }
        });
    }

    /**
     * 刷新右边的拼读介绍
     * @param position
     */
    private void updataPhoniceContent(int position) {
        if(null!=mMClassInfos&&mMClassInfos.size()>0){
            MClassInfo mClassInfo = mMClassInfos.get(position);
            if(null!=mClassInfo&&null!=mGoodInfo){
                mStrokeTitle.setText(mClassInfo.getTitle());
                mTvOriPrice.setText("原价"+mGoodInfo.getPrice()+"元");
                mTvOriPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
                mTvNewPrice.setText(Html.fromHtml("迎新年特价<font color='#FD0000'><big><big>"+mGoodInfo.getReal_price()+"</big></big></font>元"));
                mTvPhDesp.setText(mClassInfo.getDesp());
            }
        }
    }

    @Override
    public void loadData() {
       MClassEngin mClassEngin=new MClassEngin(getActivity());
       mClassEngin.getMClassList().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<MClassListInfo>>() {

           @Override
           public void call(ResultInfo<MClassListInfo> mClassListInfoResultInfo) {
               if(null!=mClassListInfoResultInfo&&1==mClassListInfoResultInfo.code&&null!=mClassListInfoResultInfo.data&&null!=mClassListInfoResultInfo.data.getMClassInfos()){
                   mMClassInfos = mClassListInfoResultInfo.data.getMClassInfos();
                   mGoodInfo = mClassListInfoResultInfo.data.getInfo();
                   if(null!=mPlayerPagerAdapter){
                       mPlayerPagerAdapter.notifyDataSetChanged();
                   }else{
                       initPagerAdapter();
                   }
                   updataPhoniceContent(0);
                   mPhonice_view.showIndex(mMClassInfos.size());
                   mPhonice_view.setIndex(0);
               }
           }
       });
    }

    @Override
    public void onPause() {
        super.onPause();
        XinQuVideoPlayer.goOnPlayOnPause();
    }

    /**
     * 视频播放列表
     */
    private class PhoniceVideoPlayerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return null==mMClassInfos?0:mMClassInfos.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            MClassInfo data=mMClassInfos.get(position);
            if(null!=data){
                PhonicsVideoPager videoPager = new PhonicsVideoPager(getActivity(),data);
                View view = videoPager.getItemView();
                view.setId(position);
                playerViews.put(position, videoPager);
                container.addView(view);
                return view;
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(container.findViewById(position));
            playerViews.remove(position);
        }
    }
}
