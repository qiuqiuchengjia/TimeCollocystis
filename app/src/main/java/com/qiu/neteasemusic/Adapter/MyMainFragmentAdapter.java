package com.qiu.neteasemusic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiu.neteasemusic.Bean.MyMainFragmentBean;
import com.qiu.neteasemusic.R;

import java.util.List;

/**
 * Created by qiu on 2017/4/11.
 */

public class MyMainFragmentAdapter extends BaseAdapter {
    private Context context;
    private List<MyMainFragmentBean> listData;
    public MyMainFragmentAdapter(Context context, List<MyMainFragmentBean> list){
        this.context=context;
        this.listData=list;
    }
    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.my_main_fragment_list_item, null);
            holder.iv_ico= (ImageView) convertView.findViewById(R.id.iv_ico);
            holder.tv_title= (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_num= (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_split_line= (TextView) convertView.findViewById(R.id.tv_split_line);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        initData(convertView,holder,position);
        return convertView;
    }

    private void initData(View convertView, ViewHolder holder, int position) {
        MyMainFragmentBean bean = listData.get(position);
        if(bean!=null){
            holder.tv_title.setText(bean.getItemName());
            holder.tv_num.setText(bean.getItemNumName());
            holder.iv_ico.setImageResource(bean.getItemIco());

        }
        if(listData.size()-1==position){
            holder.tv_split_line.setVisibility(View.GONE);
        }
    }

    public final class ViewHolder {
        private ImageView iv_ico;
        private TextView tv_title,tv_num,tv_split_line;

    }
    public void refresh(List<MyMainFragmentBean> list){
        listData=list;
        notifyDataSetChanged();
    }
}
