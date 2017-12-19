package com.yc.phonogram.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.kk.utils.LogUtil;
import com.yc.phonogram.R;
import com.yc.phonogram.adapter.LPContentListAdapter;
import com.yc.phonogram.bean.LPContntInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * TinyHung@Outlook.com
 * 2017/12/18.
 */

public class LearnPhonogramChildContentFragment extends BaseFragment {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_learn_child_content;
    }

    @Override
    public void init() {
        RecyclerView recyclerView = (RecyclerView) getView(R.id.recyclerview_lp);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        List<LPContntInfo> lpContntInfos=new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            LPContntInfo lpContntInfo=new LPContntInfo();
            lpContntInfo.setLpName("English"+i);
            lpContntInfo.setLpContent("/bir/");
            lpContntInfo.setLpStart(2);
            lpContntInfo.setLpEnd(lpContntInfo.getLpName().length()-1);
            lpContntInfos.add(lpContntInfo);
        }
        LPContentListAdapter lpContentListAdapter=new LPContentListAdapter(getActivity(),lpContntInfos);
        recyclerView.setAdapter(lpContentListAdapter);
    }

    /**
     * 入参
     * @param lpId
     * @return
     */
    public static LearnPhonogramChildContentFragment newInstance(String lpId) {
        LearnPhonogramChildContentFragment childContentFragment=new LearnPhonogramChildContentFragment();
        Bundle bundle=new Bundle();
        bundle.putString("lp_id",lpId);
        childContentFragment.setArguments(bundle);
        return childContentFragment;
    }

    /**
     * 取参
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if(null!=arguments){
            String lpId = arguments.getString("lp_id");
            LogUtil.msg(lpId);
        }
    }
}
