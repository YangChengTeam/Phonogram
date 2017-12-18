package com.yc.phonogram.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.yc.phonogram.R;
import java.util.List;

/**
 * TinyHung@Outlook.com
 * 2017/12/18.
 * 音标适配器
 */

public class LPContentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final Context mContext;
    private final List<String> mData;

    public LPContentListAdapter(Context context, List<String> list) {
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
        viewHolder.tv_item_content.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return null==mData?0:mData.size();
    }


    private class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_item_content;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_item_content=itemView.findViewById(R.id.tv_item_content);
        }
    }
}
