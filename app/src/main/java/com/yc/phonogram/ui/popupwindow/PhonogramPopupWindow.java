package com.yc.phonogram.ui.popupwindow;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.PhonogramListInfo;
import com.yc.phonogram.ui.activitys.MainActivity;
import com.yc.phonogram.ui.adapter.PhonogrameAdapter;

import java.util.concurrent.TimeUnit;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/12/21.
 */

public class PhonogramPopupWindow extends BasePopupWindow {

    PhonogrameAdapter adapter;
    RecyclerView mRecyclerView;

    public PhonogramPopupWindow(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ppw_phonogram;
    }

    @Override
    public void init() {
        mRecyclerView = (RecyclerView) getView(R.id.recyclerView);

        RxView.clicks(getView(R.id.iv_close)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });

        PhonogramListInfo phonogramListInfo = MainActivity.getMainActivity().getPhonogramListInfo();
        if (null == phonogramListInfo || null == phonogramListInfo.getPhonogramInfos()) return;
        adapter = new PhonogrameAdapter(phonogramListInfo.getPhonogramInfos());
        mRecyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                dismiss();
                MainActivity.getMainActivity().goToPage(position);
            }
        });
    }
}
