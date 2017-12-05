package com.qiu.neteasemusic.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiu.neteasemusic.R;

/**
 * Created by qiu on 2017/4/11.
 */

public abstract class AbstractBaseFragment extends Fragment {

    protected abstract int getLayoutResId();
    protected abstract void initView(View v);
    protected abstract void initData();
    protected View mView;
    public AbstractBaseFragment() {
        setRetainInstance(true);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            if (getLayoutResId() == 0)
                return super.onCreateView(inflater, container, savedInstanceState);
            mView = inflater.inflate(getLayoutResId(), container, false);
            initView(mView);
            initData();

        }
        return mView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    /**
     * 下拉刷新属性设置
     * */
    protected void initSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout){
        //设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
        swipeRefreshLayout.setProgressViewOffset(true, 0, 100);

        //设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        swipeRefreshLayout.setColorSchemeResources(R.color.base_color_red_D23A2F);
        /**
         * mySwipeRefreshLayout.setColorSchemeResources(
         android.R.color.holo_blue_bright,
         android.R.color.holo_green_light,
         android.R.color.holo_orange_light,
         android.R.color.holo_red_light);
         * */

        // 设定下拉圆圈的背景
        //mySwipeRefreshLayout.setProgressBackgroundColor(R.color.bg_color_44FFFFFF);
    }

}
