package com.yc.phonogram.base;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.yc.phonogram.R;
import com.yc.phonogram.listener.PerfectClickListener;

/**
 * TinyHung@Outlook.com
 * 2017/12/20.
 * 配合PagerAdapter使用
 */

public abstract class BasePager  {

    protected final Activity mContext;
    protected View mRootView;
    private AnimationDrawable mAnimationDrawable;
    private View mLlLoadingView;
    private View mLlLoadingError;

    public BasePager(Activity context){
        this.mContext=context;
    }


    public void setContentView(int layoutID){
        if(null==mContext) return;
        mRootView= mContext.getLayoutInflater().inflate(R.layout.base_pager,null);//父View
        View childRootView = View.inflate(mContext, layoutID, (ViewGroup) mRootView.getParent());//子View
        RelativeLayout groupContentView = (RelativeLayout) mRootView.findViewById(R.id.view_content);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        childRootView.setLayoutParams(layoutParams);
        groupContentView.addView(childRootView);//将子View添加到自身
        mLlLoadingView = mRootView.findViewById(R.id.ll_loading_view);
        mLlLoadingError = mRootView.findViewById(R.id.ll_loading_error);
        ImageView iv_loading_icon = mRootView.findViewById(R.id.iv_loading_icon);
        mAnimationDrawable = (AnimationDrawable) iv_loading_icon.getDrawable();
        mLlLoadingError.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onClickView(View v) {
                onRefresh();
            }
        });
        initViews();
        loadData();
    }

    /**
     * 返回View
     * @return
     */
    public View getItemView() {
        return mRootView;
    }


    public View getView(int id){
        return null==mRootView?null:mRootView.findViewById(id);
    }



    protected abstract void initViews();
    protected abstract void loadData();

    /**
     * 刷新,重试,子类若需要需继承
     */
    protected  void onRefresh(){

    }
    //伪生命周期方法
    public void onResume(){

    }

    public void onPause(){

    }

    public void onDestroyView(){

    }

    /**
     * 显示加载状态
     */
    protected void showLoadingView() {
        if(mRootView.getVisibility()!=View.GONE){
            mRootView.setVisibility(View.GONE);
        }
        if(mLlLoadingError.getVisibility()!=View.GONE){
            mLlLoadingError.setVisibility(View.GONE);
        }
        if(mLlLoadingView.getVisibility()!=View.VISIBLE){
            mLlLoadingView.setVisibility(View.VISIBLE);
        }
        if(null!=mAnimationDrawable&&!mAnimationDrawable.isRunning()){
            mAnimationDrawable.start();
        }
    }

    /**
     * 需要调用此方法显示界面
     */
    protected void showContentView() {
        if(mLlLoadingError.getVisibility()!=View.GONE){
            mLlLoadingError.setVisibility(View.GONE);
        }
        if(mLlLoadingView.getVisibility()!=View.GONE){
            mLlLoadingView.setVisibility(View.GONE);
        }

        if(mRootView.getVisibility()!=View.VISIBLE){
            mRootView.setVisibility(View.VISIBLE);
        }

        if(null!=mAnimationDrawable&&mAnimationDrawable.isRunning()){
            mAnimationDrawable.stop();
        }

    }

    /**
     * 显示加载失败
     */
    protected void showLoadErrorView() {
        if(mLlLoadingView.getVisibility()!=View.GONE){
            mLlLoadingView.setVisibility(View.GONE);
        }
        if(mRootView.getVisibility()!=View.GONE){
            mRootView.setVisibility(View.GONE);
        }

        if(mLlLoadingError.getVisibility()!=View.VISIBLE){
            mLlLoadingError.setVisibility(View.VISIBLE);
        }

        if(null!=mAnimationDrawable&&mAnimationDrawable.isRunning()){
            mAnimationDrawable.stop();
        }
    }
}
