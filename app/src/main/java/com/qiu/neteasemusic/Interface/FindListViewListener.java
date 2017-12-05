package com.qiu.neteasemusic.Interface;

import android.view.View;

import com.qiu.neteasemusic.Adapter.FindListViewAdapter;

/**
 * Created by qiu on 2017/4/13.
 */

public interface FindListViewListener {
    /**
     * 用来初始化界面设置
     * */
    void initGridViewsSetting(View convertView, FindListViewAdapter.ViewHolder holder, int position);
}
