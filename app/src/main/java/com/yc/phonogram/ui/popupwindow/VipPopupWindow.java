package com.yc.phonogram.ui.popupwindow;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.VipShowInfo;
import com.yc.phonogram.ui.adapter.VipInfoAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import rx.functions.Action1;
import yc.com.rthttplibrary.util.ScreenUtil;

/**
 * Created by zhangkai on 2017/12/15.
 */

public class VipPopupWindow extends BasePopupWindow {

    private VipInfoAdapter vipInfoAdapter;
    private ImageView preImagView;
    private RecyclerView recyclerView;

    public VipPopupWindow(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.popwindow_vip_view;
    }

    @Override
    public void init() {

        setAnimationStyle(R.style.popwindow_style);
        List<VipShowInfo> list = new ArrayList<>();
        VipShowInfo vipShowInfo = new VipShowInfo();
        vipShowInfo.setVipTitle("音标点读");
        vipShowInfo.setVipContent("1.  外教双机位示范发音，点击视频或音标图片即可学习标准发音；\n" +
                "2. 点击右下方单词获取读音，边学发音边记单词；\n" +
                "3. 点击“跟我读”,将你的声音与外教发音原声进行对比,找出不足,提高发音的准确性。");

        VipShowInfo vipShowInfo1 = new VipShowInfo();
        vipShowInfo1.setVipTitle("微课学习");
        vipShowInfo1.setVipContent("1.  12节原创音标课利用趣味的英语三字经、绕口令郎朗上口的韵律，连贯的语义帮助记忆及巩固音标发音，同时帮助记忆单词拼写及意思。\n"+
        "2.  12节自然拼读课程系统讲解字母发音规则，拼读规则，让你做到见词会读、听音会写！");

//
//        VipShowInfo vipShowInfo2 = new VipShowInfo();
//        vipShowInfo2.setVipTitle("小班辅导纠音");
//        vipShowInfo2.setVipContent("1.  4-6人精品小班；\n" +
//                "2. 每周2次在线辅导纠音教学；\n" +
//                "3. 课程内容包括音标纠音、单词拼读。");


        VipShowInfo vipShowInfo3 = new VipShowInfo();
        vipShowInfo3.setVipTitle("1对1纠音");
        vipShowInfo3.setVipContent("1. 专业老师根据单个学生的具体情况制定个性化音标学习方案；\n" +
                "2. 每周3次在线辅导纠音教学；\n" +
                "3. 课程内容包括音标纠音、单词拼读、字母组合发音规则。");

        list.add(vipShowInfo);
        list.add(vipShowInfo1);

        //list.add(vipShowInfo2);

//        list.add(vipShowInfo3);

        recyclerView = (RecyclerView) getView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        vipInfoAdapter = new VipInfoAdapter(list);
        recyclerView.setAdapter(vipInfoAdapter);
        recyclerView.addItemDecoration(new MyItemDecoration());
        initListener();
    }

    private void initListener() {

        RxView.clicks(getView(R.id.iv_pay_close)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
    }

    private class MyItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, ScreenUtil.dip2px(mContext, 9));
        }
    }
}
