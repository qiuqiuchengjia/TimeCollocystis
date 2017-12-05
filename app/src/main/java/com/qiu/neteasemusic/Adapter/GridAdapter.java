package com.qiu.neteasemusic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.qiu.neteasemusic.Bean.GridViewListViewBean;
import com.qiu.neteasemusic.Interface.FindListViewListener;
import com.qiu.neteasemusic.R;

import java.util.List;

/**
 * Created by qiu on 2017/4/14.
 */

public class GridAdapter extends BaseAdapter {
    private Context context;
    private List<GridViewListViewBean> listdata;
    private FindListViewListener listener;
    public GridAdapter(Context context,List<GridViewListViewBean> list,
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
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, null);
            holder.iv_head= (ImageView) convertView.findViewById(R.id.iv_head);
            holder.tv_title= (TextView) convertView.findViewById(R.id.tv_title);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        initData(convertView,holder,position);
        return convertView;

    }

    private void initData(View convertView, ViewHolder holder, int position) {
        GridViewListViewBean bean =listdata.get(position);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        if(bean!=null){
            // 这里只是模拟，实际开发可能需要加载网络图片，可以使用ImageLoader这样的图片加载框架来异步加载图片
            imageLoader.displayImage("drawable://" + bean.getIco(), holder.iv_head);
            holder.tv_title.setText(bean.getTitle());
        }
    }

    public void refresh(List<GridViewListViewBean> list){
        listdata=list;
        notifyDataSetChanged();
    }
    public final class ViewHolder {
        public ImageView iv_head;
        public TextView tv_title;
    }
}
