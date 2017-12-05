package com.qiu.neteasemusic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiu.neteasemusic.Bean.FindListViewBean;
import com.qiu.neteasemusic.Interface.FindListViewListener;
import com.qiu.neteasemusic.R;
import com.youth.banner.Banner;

import java.util.List;

/**
 * Created by qiu on 2017/4/13.
 */

public class FindListViewAdapter extends BaseAdapter {
    private Context context;
    private List<FindListViewBean> listdata;
    private FindListViewListener listener;
    public FindListViewAdapter(Context context, List<FindListViewBean> list,
                               FindListViewListener listener){
        this.context=context;
        this.listdata=list;
        this.listener=listener;
    }
    @Override
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        return listdata.get(position);
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
            FindListViewBean findListViewBean = listdata.get(position);
            if(findListViewBean!=null){
                switch (findListViewBean.getItemType()){
                    case FindListViewBean.ITEM_TYPE_SPLIT_LINE://分割线
                        convertView = LayoutInflater.from(context).inflate(R.layout.list_view_item_split_line, null);
                        break;
                    case FindListViewBean.ITEM_TYPE_BANNER://轮播图
                        convertView = LayoutInflater.from(context).inflate(R.layout.list_view_item_banner, null);
                        holder.banner = (Banner) convertView.findViewById(R.id.banner_find);
                        holder.tv_title= (TextView) convertView.findViewById(R.id.tv_banner_detail);
                        break;
                    case FindListViewBean.ITEM_TYPE_PRIVATE_GROOM://私人推荐
                        convertView = LayoutInflater.from(context).inflate(R.layout.list_view_item_find_person_groom, null);
                        holder.ll_fm= (LinearLayout) convertView.findViewById(R.id.ll_find_fm);
                        holder.ll_day= (LinearLayout) convertView.findViewById(R.id.ll_find_day_song);
                        holder.ll_hot= (LinearLayout) convertView.findViewById(R.id.ll_find_hot_song);
                        holder.tv_title= (TextView) convertView.findViewById(R.id.tv_find_date);
                        break;
                    case FindListViewBean.ITEM_TYPE_GRID_GROOM://推荐歌单
                        convertView = LayoutInflater.from(context).inflate(R.layout.list_view_item_grid, null);
                        holder.gv_find= (GridView) convertView.findViewById(R.id.gv_find);
                        holder.ll_fm= (LinearLayout) convertView.findViewById(R.id.ll_title_item);
                        break;


                }

            }
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        initData(convertView,holder,position);
        return convertView;
    }

    private void initData(View convertView, ViewHolder holder, int position) {
        FindListViewBean findListViewBean = listdata.get(position);
        if(findListViewBean!=null){
            switch (findListViewBean.getItemType()){
                case FindListViewBean.ITEM_TYPE_BANNER://轮播图
                    listener.initGridViewsSetting(convertView,holder,position);
                    break;
                case FindListViewBean.ITEM_TYPE_PRIVATE_GROOM://私人推荐
                    listener.initGridViewsSetting(convertView,holder,position);
                    break;
                case FindListViewBean.ITEM_TYPE_GRID_GROOM://推荐歌单
                    listener.initGridViewsSetting(convertView,holder,position);
                    break;
            }
        }
    }

    public final class ViewHolder {
         public Banner banner;
         public TextView tv_title;
         public LinearLayout ll_fm,ll_day,ll_hot;
         public GridView gv_find;


    }
    public void refresh(List<FindListViewBean> list){
        listdata=list;
        notifyDataSetChanged();
    }
}
