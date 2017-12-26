package com.yc.phonogram.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.ExampleInfo;
import com.yc.phonogram.utils.LPUtils;
import java.util.List;

/**
 * TinyHung@Outlook.com
 * 2017/12/18.
 * 学音标音标适配器
 */

public class LPContentListAdapter extends BaseAdapter{

    private List<ExampleInfo> mData=null;
    private String mLearn=null;
    private final LayoutInflater mInflater;


    public LPContentListAdapter(Context context, List<ExampleInfo> list) {
        this.mData=list;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return null==mData?0:mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(null==convertView){
            convertView= mInflater.inflate(R.layout.lp_content_list_item, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        ExampleInfo data = mData.get(position);
        if(null!=data){
            viewHolder.tv_item_content.setText("");
            viewHolder.tv_item_content_lp.setText("");
            viewHolder.tv_item_content.setText(Html.fromHtml(LPUtils.getInstance().addWordLetterColor(data.getWord(),data.getLetter())));
            viewHolder.tv_item_content_lp.setText(Html.fromHtml(LPUtils.getInstance().addWordPhoneticLetterColor(data.getWordPhonetic(),null==mLearn?data.getPhonetic():mLearn)));
        }
        return convertView;
    }

    public void setNewData(List<ExampleInfo> data,String learn) {
        this.mData=data;
        this.mLearn=learn;
        this.notifyDataSetChanged();
    }

    public List<ExampleInfo> getData() {
        return mData;
    }


    private class ViewHolder {

        private TextView tv_item_content;
        private TextView tv_item_content_lp;

        public ViewHolder(View convertView) {
            tv_item_content=convertView.findViewById(R.id.tv_item_content);
            tv_item_content_lp=convertView.findViewById(R.id.tv_item_content_lp);
        }
    }
}
