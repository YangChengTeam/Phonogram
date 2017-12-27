package com.yc.phonogram.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.ExampleInfo;
import com.yc.phonogram.ui.views.holder.RecyclerHolder;
import com.yc.phonogram.utils.LPUtils;
import java.util.List;

/**
 * TinyHung@Outlook.com
 * 2017/12/18.
 * 学音标音标适配器
 */

public class RecyclerViewLPContentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ExampleInfo> mData=null;
    private String mLearn=null;
    private final LayoutInflater mInflater;


    public RecyclerViewLPContentListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerHolder(mInflater.inflate(R.layout.lp_content_list_item,null));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final RecyclerHolder viewHolder= (RecyclerHolder) holder;
        ExampleInfo data = mData.get(position);
        if(null!=data){
            viewHolder.tv_item_content.setText("");
            viewHolder.tv_item_content_lp.setText("");
            viewHolder.tv_item_content.setText(Html.fromHtml(LPUtils.getInstance().addWordLetterColor(data.getWord(),data.getLetter())));
            viewHolder.tv_item_content_lp.setText(Html.fromHtml(LPUtils.getInstance().addWordPhoneticLetterColor(data.getWordPhonetic(),null==mLearn?data.getPhonetic():mLearn)));
            viewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null!=mOnItemClickListener){
                        mOnItemClickListener.onItemClick(viewHolder,position);
                    }
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return null==mData?0:mData.size();
    }


    public void setNewData(List<ExampleInfo> data,String learn) {
        this.mData=data;
        this.mLearn=learn;
        this.notifyDataSetChanged();
    }

    public List<ExampleInfo> getData() {
        return mData;
    }

    public interface OnItemClickListener{
        void onItemClick(RecyclerHolder holder,int position);
    }
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
