package com.yc.phonogram.ui.fragments;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.PhonogramInfo;
import com.yc.phonogram.domain.PhonogramListInfo;
import com.yc.phonogram.ui.activitys.MainActivity;
import com.yc.phonogram.ui.pager.LearnVideoPager;
import com.yc.phonogram.ui.popupwindow.PayPopupWindow;
import com.yc.phonogram.ui.views.MainBgView;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangkai on 2017/12/15.
 * 学音标
 */
public class LearnPhonogramFragment extends BaseFragment  {

    private static final String TAG =LearnPhonogramFragment.class.getSimpleName() ;
    private MainBgView mMainBgView;
    private ViewPager mView_pager;
    private LearnPagerAdapter mLearnPagerAdapter=null;
    private List<PhonogramInfo> mPhonogramInfos=null;
    private Map<Integer,LearnVideoPager> mPagerMap=null;//方便调用View的伪生命周期方法
    private int cureenIndex=0;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_learn;
    }

    @Override
    public void init() {
        mPagerMap=new HashMap<>();
        mMainBgView= (MainBgView) getView(R.id.mainBgView);
        mView_pager = (ViewPager) getView(R.id.view_pager);
        initPagerAdapter();
    }

    private void initPagerAdapter() {
        mLearnPagerAdapter = new LearnPagerAdapter();
        mView_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                cureenIndex=position;
                XinQuVideoPlayer.releaseAllVideos();
                mMainBgView.setIndex(position);
                //如果用户没有购买章节
                if(position>=3&&!MainActivity.getMainActivity().isPhonogramVip()){
                    mMainBgView.setIndex(2);
                    mView_pager.setCurrentItem(2);
                    PayPopupWindow payPopupWindow=new PayPopupWindow(getActivity());
                    payPopupWindow.show(getActivity().getWindow().getDecorView(), Gravity.CENTER);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mView_pager.setOffscreenPageLimit(1);
        mView_pager.setAdapter(mLearnPagerAdapter);
        mMainBgView.setIndexListener(new MainBgView.IndexListener() {
            @Override
            public void leftClick(int position) {
                if(null!=mView_pager&&mView_pager.getChildCount()>0){
                    mView_pager.setCurrentItem(position);
                }
            }

            @Override
            public void rightClcik(int position) {
                if(null!=mView_pager&&mView_pager.getChildCount()>0){
                    mView_pager.setCurrentItem(position);
                }
            }
        });
    }

    @Override
    public void loadData() {
        PhonogramListInfo phonogramListInfo = MainActivity.getMainActivity().getPhonogramListInfo();
        if(null==phonogramListInfo||null==phonogramListInfo.getPhonogramInfos()) return;
        mPhonogramInfos = phonogramListInfo.getPhonogramInfos();
        if(null!= mPhonogramInfos && mPhonogramInfos.size()>0){
            mMainBgView.showIndex(mPhonogramInfos.size());
            mMainBgView.setIndex(0);
            if(null!=mLearnPagerAdapter){
                mLearnPagerAdapter.notifyDataSetChanged();
            }else{
                initPagerAdapter();
            }
        }
    }

    /**
     * 垂直列表适配器
     */
    private class LearnPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return null==mPhonogramInfos?0:mPhonogramInfos.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhonogramInfo phonogramInfo = mPhonogramInfos.get(position);
            if(null!=phonogramInfo){
                LearnVideoPager videoPager = new LearnVideoPager(getActivity(),phonogramInfo);
                View view = videoPager.getItemView();
                view.setId(position);
                Log.d(TAG,"添加了："+position);
                mPagerMap.put(position, videoPager);
                container.addView(view);
                return view;
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(container.findViewById(position));
            Log.d(TAG,"移除了："+position);
            mPagerMap.remove(position);
        }
    }

    /**
     * 伪生命周期 DestroyView
     * @param itemPoistion
     */
    private void onChildDestroyView(int itemPoistion) {
        if(-1!=itemPoistion&&null!=mPagerMap&&mPagerMap.size()>0){
            Iterator<Map.Entry<Integer, LearnVideoPager>> iterator = mPagerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, LearnVideoPager> next = iterator.next();
                if(itemPoistion==next.getKey()){
                    LearnVideoPager playerTempPager = next.getValue();
                    if(null!=playerTempPager){
                        playerTempPager.onDestroyView();
                    }
                }
            }
        }
    }

    /**
     * 伪生命周期 Resume
     * @param itemPoistion
     */
    private void onChilResume(int itemPoistion) {
        if(-1!=itemPoistion&&null!=mPagerMap&&mPagerMap.size()>0){
            Iterator<Map.Entry<Integer, LearnVideoPager>> iterator = mPagerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, LearnVideoPager> next = iterator.next();
                if(itemPoistion==next.getKey()){
                    LearnVideoPager playerTempPager = next.getValue();
                    if(null!=playerTempPager){
                        playerTempPager.onResume();
                    }
                }
            }
        }
    }

    /**
     * 伪生命周期 Pause
     * @param itemPoistion
     */
    private void onChildPause(int itemPoistion) {
        if(-1!=itemPoistion&&null!=mPagerMap&&mPagerMap.size()>0){
            Iterator<Map.Entry<Integer, LearnVideoPager>> iterator = mPagerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, LearnVideoPager> next = iterator.next();
                if(itemPoistion==next.getKey()){
                    LearnVideoPager playerTempPager = next.getValue();
                    if(null!=playerTempPager){
                        playerTempPager.onPause();
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onChilResume(cureenIndex);
    }

    @Override
    public void onPause() {
        super.onPause();
        XinQuVideoPlayer.goOnPlayOnPause();
        onChildPause(cureenIndex);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onChildDestroyView(cureenIndex);
    }
}
