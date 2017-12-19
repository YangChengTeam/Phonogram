package com.yc.phonogram.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import com.kk.utils.LogUtil;
import com.yc.phonogram.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

public class ReadItemFragment extends BaseFragment {

    public static int position;

    private ProgressBar mProgressBar;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_read_item;
    }

    @Override
    public void init() {
        mProgressBar = (ProgressBar) getView(R.id.progress_bar);
    }

    public static ReadItemFragment newInstance(int pos) {
        ReadItemFragment childContentFragment = new ReadItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        childContentFragment.setArguments(bundle);
        return childContentFragment;
    }

    /**
     * 取参
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (null != arguments) {
            position = arguments.getInt("pos");
            LogUtil.msg("onViewCreated position--->" + position);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        countDownRead();
    }

    public void countDownRead() {
        final int count = 3;
        //设置0延迟，每隔一秒发送一条数据
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count + 1) //设置循环11次
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return count - aLong; //
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                })

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        LogUtil.msg("--->" + aLong);
                            int temp = (int) ((double) aLong / (double) count * 100);
                            LogUtil.msg("progress--->" + temp);
                            mProgressBar.setProgress(temp);
                    }
                });
    }
}
