package com.qiu.neteasemusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.qiu.neteasemusic.Base.BaseToolbarActivity;

/**
 * Created by qiu on 2017/4/11.
 */

public class WelcomeActivity extends BaseToolbarActivity {
    @Override
    protected boolean isDisplayHomeAsUpEnabled() {
        return false;
    }

    @Override
    protected void clickTitle(int id) {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        setToolbarHide(true);
        setStatusBarHide(true);

    }

    @Override
    protected void initData() {
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                callback();
            }
        }, 3000);
    }
    private void callback() {
        onIntent(MainActivity.class);
    }
    private void onIntent(Class<? extends Activity> cla) {
        Intent intent = new Intent(this, cla);
        startActivity(intent);
        this.finish();
    }
    @Override
    protected String getToolbarTitle() {
        return null;
    }

    @Override
    protected String getToolbarLeftTitle() {
        return null;
    }

    @Override
    protected String getToolbarRightTitle() {
        return null;
    }
}
