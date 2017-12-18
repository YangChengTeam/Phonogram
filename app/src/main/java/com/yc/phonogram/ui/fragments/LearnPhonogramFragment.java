package com.yc.phonogram.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yc.phonogram.R;
import com.yc.phonogram.adapter.LPContentListAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangkai on 2017/12/15.
 * 学音标
 */

public class LearnPhonogramFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fragment_learn_phonogram,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerview_lp);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        List<String> list=new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("音标"+i);
        }
        LPContentListAdapter lpContentListAdapter=new LPContentListAdapter(getActivity(),list);
        recyclerView.setAdapter(lpContentListAdapter);
    }
}
