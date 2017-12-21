package com.yc.phonogram.ui.fragments;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.PhonogramInfo;
import com.yc.phonogram.domain.PhonogramListInfo;
import com.yc.phonogram.helper.SeekBarHelper;
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
 * 首页学音标
 */
public class LearnPhonogramFragment extends BaseFragment  {

    private static final String TAG =LearnPhonogramFragment.class.getSimpleName() ;
    private MainBgView mMainBgView;
    private ViewPager mViewPager;
    private LearnPagerAdapter mLearnPagerAdapter=null;
    private List<PhonogramInfo> mPhonogramInfos=null;
    private Map<Integer,LearnVideoPager> mPagerMap=null;//方便调用View的伪生命周期方法
    private int cureenIndex=0;
    private int oldCureenIndex=0;//过去显示到第几个Poistion 了

    @Override
    public int getLayoutId() {
        return R.layout.fragment_learn;
    }

    @Override
    public void init() {
        mPagerMap=new HashMap<>();
        mMainBgView= (MainBgView) getView(R.id.mainBgView);
        mViewPager = (ViewPager) getView(R.id.view_pager);
        initPagerAdapter();
    }

    private void initPagerAdapter() {
        mLearnPagerAdapter = new LearnPagerAdapter();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                cureenIndex=position;
                XinQuVideoPlayer.releaseAllVideos();
                mMainBgView.setIndex(position);
                //如果用户没有购买章节
                if(cureenIndex>=3&&!MainActivity.getMainActivity().isPhonogramVip()){
                    mMainBgView.setIndex(oldCureenIndex);
                    mViewPager.setCurrentItem(oldCureenIndex,false);
                    PayPopupWindow payPopupWindow=new PayPopupWindow(getActivity());
                    payPopupWindow.show(getActivity().getWindow().getDecorView(), Gravity.CENTER);
                    return;
                }
                oldCureenIndex=cureenIndex;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mLearnPagerAdapter);
        mMainBgView.setIndexListener(new SeekBarHelper.IndexListener() {
            @Override
            public void leftClick(int position) {
                if(null!=mViewPager&&mViewPager.getChildCount()>0){
                    mViewPager.setCurrentItem(position);
                }
            }

            @Override
            public void rightClcik(int position) {
                if(null!=mViewPager&&mViewPager.getChildCount()>0){
                    mViewPager.setCurrentItem(position);
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
                mPagerMap.put(position, videoPager);
                container.addView(view);
                return view;
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(container.findViewById(position));
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

    public void setCurrentItem(int index){
        if(null!=mViewPager&&mViewPager.getChildCount()>0){
            mViewPager.setCurrentItem(index);
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
        if(null!=mPagerMap){
            mPagerMap.clear();
        }
    }
}
