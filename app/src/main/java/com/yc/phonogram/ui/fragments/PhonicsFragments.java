package com.yc.phonogram.ui.fragments;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import com.kk.securityhttp.domain.ResultInfo;
import com.orhanobut.logger.Logger;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.MClassInfo;
import com.yc.phonogram.domain.MClassListInfo;
import com.yc.phonogram.engin.MClassEngin;
import com.yc.phonogram.ui.pager.PhonicsVideoPager;
import com.yc.phonogram.ui.views.PhoniceSeekBarView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import static android.content.ContentValues.TAG;

/**
 * Created by zhangkai on 2017/12/15.
 * 自然拼读
 */

public class PhonicsFragments extends BaseFragment {

    private Map<Integer,PhonicsVideoPager> playerViews;
    private List<MClassInfo> mMClassInfos;
    private PhoniceVideoPlayerPagerAdapter mPlayerPagerAdapter;
    private PhoniceSeekBarView mPhonice_view;
    private ViewPager mView_pager;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_phonics;
    }

    @Override
    public void init() {
        playerViews=new HashMap<>();
        mView_pager = (ViewPager) getView(R.id.view_pager);
        mPhonice_view = (PhoniceSeekBarView) getView(R.id.phonice_view);
        mPlayerPagerAdapter = new PhoniceVideoPlayerPagerAdapter();
        mView_pager.setAdapter(mPlayerPagerAdapter);
        mView_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPhonice_view.setIndex(position);
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

    @Override
    public void loadData() {
       MClassEngin mClassEngin=new MClassEngin(getActivity());
       mClassEngin.getMClassList().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<MClassListInfo>>() {
           @Override
           public void call(ResultInfo<MClassListInfo> mClassListInfoResultInfo) {
               if(null!=mClassListInfoResultInfo&&1==mClassListInfoResultInfo.code&&null!=mClassListInfoResultInfo.data&&null!=mClassListInfoResultInfo.data.getMClassInfos()){
                   mMClassInfos = mClassListInfoResultInfo.data.getMClassInfos();
                   if(null!=mPlayerPagerAdapter){
                       mPlayerPagerAdapter.notifyDataSetChanged();
                   }
                   mPhonice_view.showIndex(mMClassInfos.size());
                   mPhonice_view.setIndex(0);
               }else{

               }
           }
       });
    }

    /**
     * 垂直列表适配器
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
                View view = videoPager.getView();
                view.setId(position);
                Logger.d(TAG,"添加了："+position);
                playerViews.put(position, videoPager);
                container.addView(view);
                return view;
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(container.findViewById(position));
            Logger.d(TAG,"移除了："+position);
            playerViews.remove(position);
        }
    }
}
