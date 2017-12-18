package com.yc.phonogram.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;


/**
 * FragmentPager适配器
 */

public class LPFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private List<Fragment> mFragment;
    private List<String> mTitleList;

    /**
     * 普通，主页使用
     */
    public LPFragmentPagerAdapter(FragmentManager fm, List<Fragment> mFragment) {
        super(fm);
        this.mFragment = mFragment;
    }

    /**
     * 接收首页传递的标题
     */
    public LPFragmentPagerAdapter(FragmentManager fm, List<Fragment> mFragment, List<String> mTitleList) {
        super(fm);
        this.mFragment = mFragment;
        this.mTitleList = mTitleList;
    }

    @Override
    public Fragment getItem(int position) {
        return null==mFragment?null:(Fragment) mFragment.get(position);
    }

    @Override
    public int getCount() {
        return null==mFragment?0:mFragment.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    /**
     * 首页显示title，每日推荐等..
     * 若有问题，移到对应单独页面
     */
    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitleList != null&&mTitleList.size()>0) {
            return mTitleList.get(position);
        } else {
            return "";
        }
    }

    /**
     * 添加单个Fragment
     * @param fragment
     * @param title
     */
    public void addFragment(Fragment fragment, String title){
        if(null==mFragment){
            mFragment=new ArrayList<>();
        }
        mFragment.add(0,fragment);
    }

    /**
     * 设置全新的Fragment
     * @param fragment
     */
    public void setNewFragments(List<Fragment> fragment, List<String> titleList) {
        if(null!=mFragment) mFragment.clear();
        this.mFragment = fragment;
        if(null!=mTitleList) mTitleList.clear();
        this.mTitleList = titleList;
        notifyDataSetChanged();
    }

    /**
     * 追加多个Fragmnet
     */
    public void addFragments(List<Fragment> fragments, List<String> titles) {
        for (Fragment fragment : fragments) {
            mFragment.add(fragment);
        }
        for (String title : titles) {
            mTitleList.add(title);
        }
        notifyDataSetChanged();
    }

}
