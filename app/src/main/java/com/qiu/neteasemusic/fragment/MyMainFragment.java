package com.qiu.neteasemusic.fragment;

import android.view.View;

import com.qiu.neteasemusic.Base.AbstractBaseFragment;
import com.qiu.neteasemusic.Interface.ToolbarTitleInteface;
import com.qiu.neteasemusic.R;

/**
 * Created by qiu on 2017/4/11.
 * 我的
 */

public class MyMainFragment  extends AbstractBaseFragment  implements ToolbarTitleInteface {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_my_main;
    }

    @Override
    protected void initView(View v) {
    }

    @Override
    protected void initData() {
    }


    @Override
    public String getToolbarTitle() {
        return "main";
    }
}
