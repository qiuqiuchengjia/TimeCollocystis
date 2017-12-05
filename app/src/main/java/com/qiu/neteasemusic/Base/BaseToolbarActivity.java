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

import static com.qiu.neteasemusic.R.color.text_color_white_99FFFFFF;
import static com.qiu.neteasemusic.R.color.text_color_white_FFFFFF;

/**
 * Created by qiu on 2017/4/11.
 * activity的基类
 */

public abstract class BaseToolbarActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private TextView toolbarTitle,toolbarLeftTitle,toolbarRightTitle;
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
        toolbarTitle = (TextView) toolbar.findViewById(R.id.nav_toolbar_title);
        toolbarLeftTitle = (TextView) toolbar.findViewById(R.id.nav_toolbar_title_left);
        toolbarRightTitle = (TextView) toolbar.findViewById(R.id.nav_toolbar_title_right);
        toolbarTitle.setText(getToolbarTitle());
        toolbarLeftTitle.setText(getToolbarLeftTitle());
        toolbarRightTitle.setText(getToolbarRightTitle());
        toolbarTitle.setOnClickListener(this);
        toolbarLeftTitle.setOnClickListener(this);
        toolbarRightTitle.setOnClickListener(this);
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
    abstract protected void clickTitle(int id);

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
    abstract protected String getToolbarLeftTitle();
    abstract protected String getToolbarRightTitle();
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.nav_toolbar_title){
            clickTitle(2);
            setToolbarTitleState(toolbarTitle,toolbarLeftTitle,toolbarRightTitle);
        }else if(id==R.id.nav_toolbar_title_left){
            clickTitle(1);
            setToolbarTitleState(toolbarLeftTitle,toolbarTitle,toolbarRightTitle);
        }else if(id==R.id.nav_toolbar_title_right){
            clickTitle(3);
            setToolbarTitleState(toolbarRightTitle,toolbarLeftTitle,toolbarTitle);
        }
    }
    /**
     * 第一个参数代表点击了此text，所以此text字体变大，颜色变白，其他的字体变小
     * 颜色变暗
     * */
    private void setToolbarTitleState(TextView t1,TextView t2,TextView t3){
        t1.setTextColor(getResources().getColor(text_color_white_FFFFFF));
        t2.setTextColor(getResources().getColor(text_color_white_99FFFFFF));
        t3.setTextColor(getResources().getColor(text_color_white_99FFFFFF));
        //这里不知道为啥不能用dimen
        t1.setTextSize(18);
        t2.setTextSize(16);
        t3.setTextSize(16);
    }
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
