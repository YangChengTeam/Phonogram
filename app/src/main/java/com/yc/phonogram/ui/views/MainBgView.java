package com.yc.phonogram.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yc.phonogram.R;
import com.yc.phonogram.listener.OnItemClickListener;
import com.yc.phonogram.listener.PerfectClickListener;

/**
 * Created by zhangkai on 2017/12/18.
 */

public class MainBgView extends BaseView {

    private static final String TAG = MainBgView.class.getSimpleName();
    private ImageView mInnerBgImageView;
    private RelativeLayout mIndexRelativeLayout;
    private ImageView mLeftImageView;
    private ImageView mRightImageView;
    private TextView mIndexTextView;
    public int mCurrenIndex;
    private OnItemClickListener onItemClickListener;

    public MainBgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainBgView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_main_bg;
    }


    @Override
    public void init() {
        mIndexRelativeLayout = (RelativeLayout) getView(R.id.rl_index);
        mInnerBgImageView = (ImageView) getView(R.id.iv_inner_bg);
        mLeftImageView = (ImageView) getView(R.id.iv_left);
        mRightImageView = (ImageView) getView(R.id.iv_right);
        mIndexTextView = (TextView) getView(R.id.tv_index);
        //不允许用户疯狂点击
        mLeftImageView.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onClickView(View v) {
                setIndex(mCurrenIndex-1);
                if(null!=onItemClickListener){
                    onItemClickListener.onItemClick(mCurrenIndex);
                }
            }
        });
        mRightImageView.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onClickView(View v) {
                setIndex(mCurrenIndex+1);
                if(null!=onItemClickListener){
                    onItemClickListener.onItemClick(mCurrenIndex);
                }
            }
        });
    }

    private int count;

    public void showIndex(int count) {
        this.count = count;
        mIndexRelativeLayout.setVisibility(View.VISIBLE);
    }

    public void hideLeft() {
        mLeftImageView.setVisibility(View.GONE);
    }

    public void showLeft() {
        mLeftImageView.setVisibility(View.VISIBLE);
    }

    public void hideRight() {
        mRightImageView.setVisibility(View.GONE);
    }

    public void showRight() {
        mRightImageView.setVisibility(View.VISIBLE);
    }

    public void setIndex(int index) {
        if(index<0){
            index=0;
        }else if(index>count){
            index=count;
        }
        if(0==index){
            if(null!=mLeftImageView)mLeftImageView.setVisibility(View.GONE);
        }else if(index==count-1){
            if(null!=mRightImageView)mRightImageView.setVisibility(View.GONE);
        }else{
            if(null!=mRightImageView&&mRightImageView.getVisibility()!=View.VISIBLE){
                mRightImageView.setVisibility(View.VISIBLE);
            }
            if(null!=mLeftImageView&&mLeftImageView.getVisibility()!=View.VISIBLE){
                mLeftImageView.setVisibility(View.VISIBLE);
            }
        }
        mIndexTextView.setText((index+ 1)+"/" + count);
        this.mCurrenIndex=index;
    }

    public void showInnerBg() {
        mInnerBgImageView.setVisibility(View.VISIBLE);
    }

    public void setChangerListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener=onItemClickListener;
    }
}
