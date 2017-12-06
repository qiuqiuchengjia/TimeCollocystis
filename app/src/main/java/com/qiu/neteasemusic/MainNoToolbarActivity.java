package com.qiu.neteasemusic;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.framework.greendroid.widget.MyFragmentTabHost;
import com.qiu.neteasemusic.fragment.FindFragment;
import com.qiu.neteasemusic.fragment.MyMainFragment;
import com.qiu.neteasemusic.fragment.StateFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by qiu on 2017-12-6.
 */

public class MainNoToolbarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        TabHost.OnTabChangeListener{
    private MyFragmentTabHost mTabHost = null;
    private LinearLayout mLinearLayout_richeng;
    private LinearLayout mLinearLayout_create;
    private LinearLayout mLinearLayout_work;
    private int curFragmentFlag;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qiu);
        initStauteBar();
        initView();

    }
    protected void initView() {
        mLinearLayout_richeng = (LinearLayout) findViewById(R.id.ll_richeng);
        mLinearLayout_create = (LinearLayout) findViewById(R.id.ll_create);
        mLinearLayout_work = (LinearLayout) findViewById(R.id.ll_work);
        mTabHost = initFragment(this, findViewById(R.id.parent_id),
                getSupportFragmentManager(),
                new MyFragmentTabHost.OnFragmentChangedListener() {
                    @Override
                    public void onChanaged(Fragment fragments) {
                    }
                });


        addCustomTab(this, "首页", "MyMainFragment", 0,
                MyMainFragment.class, mTabHost);
        addCustomTab(this, "活动", "FindFragment",0,
                FindFragment.class, mTabHost);
        addCustomTab(this, "活动", "StateFragment", 0,
                StateFragment.class, mTabHost);
        mTabHost.setOnTabChangedListener(this);
        curFragmentFlag=0;
        initTabClick();
    }
    private void initStauteBar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.RED);
        tintManager.setStatusBarTintColor(Color.RED);
    }
    private double getStatusBarHeight(Context context){
        double statusBarHeight = Math.ceil(25 * context.getResources().getDisplayMetrics().density);
        return statusBarHeight;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
    private void initTabClick() {
        mLinearLayout_richeng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTabHost.setCurrentTab(0);
                curFragmentFlag=0;
            }
        });
        mLinearLayout_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTabHost.setCurrentTab(1);
                curFragmentFlag=1;
            }
        });
        mLinearLayout_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTabHost.setCurrentTab(2);
                curFragmentFlag=1;
            }
        });
    }

    /**
     * @param mContext
     * @param parentView
     * @param frg
     * @param onFragmentChangedListener
     * @return
     */
    public MyFragmentTabHost initFragment(Context mContext,
                                          View parentView,
                                          FragmentManager frg,
                                          MyFragmentTabHost.OnFragmentChangedListener
                                                  onFragmentChangedListener) {
        MyFragmentTabHost mTabHost = (MyFragmentTabHost) parentView.findViewById(R.id.tabhost);
        mTabHost.setChanagedListener(onFragmentChangedListener);
        mTabHost.setup(mContext, frg,
                android.R.id.tabcontent);
        return mTabHost;
    }
    /**
     * @param context
     * @param tabHostTitle
     * @param tag
     * @param resId
     * @param c
     * @param fth
     */
    public void addCustomTab(Context context, String tabHostTitle, String tag,
                             int resId, Class<?> c, MyFragmentTabHost fth) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.tab_customtab, null);
        ImageView image = (ImageView) view.findViewById(R.id.tab_icon);
        TextView text = (TextView) view.findViewById(R.id.tabtitle);
        image.setBackgroundResource(resId);
        text.setText(tabHostTitle);
        TabHost.TabSpec spec = fth.newTabSpec(tag);
        spec.setIndicator(view);
        fth.addTab(spec, c, null);
    }

    @Override
    public void onTabChanged(String tabId) {

    }
}
