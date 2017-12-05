package com.qiu.neteasemusic.Base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.qiu.neteasemusic.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by qiu on 2017/4/11.
 * activity的基类
 */

public abstract class BaseToolbarActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private TextView mTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        initStauteBar();
        initBase();
        initView();
        initData();

    }

    private void initBase() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //是否显示toolbar 自带title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(isDisplayHomeAsUpEnabled()){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.base_nav_back_icon);
        }
        mTitle = (TextView) toolbar.findViewById(R.id.tv_title);
        mTitle.setText(getToolbarTitle());
        //返回
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 是否显示返回键
     *
     * @return
     */
    abstract protected boolean isDisplayHomeAsUpEnabled();

    protected Toolbar getToolbar(){
        return toolbar;
    }
    protected abstract int getLayoutResId();

    private void initStauteBar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.TRANSPARENT);
        tintManager.setStatusBarTintColor(Color.TRANSPARENT);
    }
    abstract protected void initView();
    abstract protected void initData();
    abstract protected String getToolbarTitle();
    protected void setStatusBarHide(boolean isHide){
        if(isHide){
            //取消状态栏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            //取消状态栏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }
    }
    protected void setToolbarHide(boolean isHide) {
        if(isHide){
            getSupportActionBar().hide();
        }else{
            getSupportActionBar().show();
        }
    }

}
