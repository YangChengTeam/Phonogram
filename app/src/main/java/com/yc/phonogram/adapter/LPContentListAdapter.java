package com.yc.phonogram.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.ExampleInfo;
import java.util.List;

/**
 * TinyHung@Outlook.com
 * 2017/12/18.
 * 学音标音标适配器
 */

public class LPContentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final Context mContext;
    private List<ExampleInfo> mData;

    public LPContentListAdapter(Context context, List<ExampleInfo> list) {
        this.mContext=context;
        this.mData=list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(mContext, R.layout.lp_content_list_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder= (ViewHolder) holder;
        ExampleInfo data = mData.get(position);
        if(null==data) return;
        SpannableString spannableString = new SpannableString(data.getWord());

        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFE0100")),0,data.getWord().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.tv_item_content.setText(spannableString);
        viewHolder.tv_item_content_lp.setText(data.getWordPhonetic());
    }

    @Override
    public int getItemCount() {
        return null==mData?0:mData.size();
    }

    public void setNewData(List<ExampleInfo> data) {
        this.mData=data;
        this.notifyDataSetChanged();
    }


    private class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_item_content;
        private TextView tv_item_content_lp;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_item_content=itemView.findViewById(R.id.tv_item_content);
            tv_item_content_lp=itemView.findViewById(R.id.tv_item_content_lp);
        }
    }
}
