package com.qiu.neteasemusic;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.framework.greendroid.widget.MyFragmentTabHost;
import com.qiu.neteasemusic.Base.BaseToolbarActivity;
import com.qiu.neteasemusic.Utils.ToastUtil;
import com.qiu.neteasemusic.fragment.FindFragment;
import com.qiu.neteasemusic.fragment.MyMainFragment;
import com.qiu.neteasemusic.fragment.StateFragment;

public class MainActivity extends BaseToolbarActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        TabHost.OnTabChangeListener{

    private MyFragmentTabHost mTabHost = null;
    private LinearLayout mLinearLayout_richeng;
    private LinearLayout mLinearLayout_create;
    private LinearLayout mLinearLayout_work;
    private int curFragmentFlag;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mLinearLayout_richeng = (LinearLayout) findViewById(R.id.ll_richeng);
        mLinearLayout_create = (LinearLayout) findViewById(R.id.ll_create);
        mLinearLayout_work = (LinearLayout) findViewById(R.id.ll_work);

        setToolbarHide(false);
        setStatusBarHide(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
                curFragmentFlag=2;
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    protected String getToolbarTitle() {
        return getResources().getString(R.string.text_find);
    }

    @Override
    protected String getToolbarLeftTitle() {
        return getResources().getString(R.string.text_my);
    }

    @Override
    protected String getToolbarRightTitle() {
        return getResources().getString(R.string.text_state);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_seach) {
            ToastUtil.showToast(MainActivity.this,"点击搜索");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id =item.getItemId();

        if (id == R.id.item_msg) {
           ToastUtil.showToast(MainActivity.this,"我的消息");
        } else if (id == R.id.item_store) {
            ToastUtil.showToast(MainActivity.this,"积分商城");
        } else if (id == R.id.item_member) {
            ToastUtil.showToast(MainActivity.this,"付费音乐包");
        }else if (id == R.id.item_free) {
            ToastUtil.showToast(MainActivity.this,"在线听歌免流量");
        }else if (id == R.id.item_identify) {
            ToastUtil.showToast(MainActivity.this,"听歌识曲");
        }else if (id == R.id.item_skin) {
            ToastUtil.showToast(MainActivity.this,"主题换肤");
        }else if (id == R.id.item_night) {
            ToastUtil.showToast(MainActivity.this,"夜间模式");
        }else if (id == R.id.item_time) {
            ToastUtil.showToast(MainActivity.this,"定时停止播放");
        }else if (id == R.id.item_clock) {
            ToastUtil.showToast(MainActivity.this,"音乐闹钟");
        }else if (id == R.id.item_cloud) {
            ToastUtil.showToast(MainActivity.this,"我的音乐云盘");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected boolean isDisplayHomeAsUpEnabled() {
        return false;
    }

    @Override
    protected void clickTitle(int id) {
        if(id==1){//我的
            mTabHost.setCurrentTab(0);
            curFragmentFlag=0;
        }else if(id==2){//发现
            mTabHost.setCurrentTab(1);
            curFragmentFlag=1;
        }else if(id==3){//动态
            mTabHost.setCurrentTab(2);
            curFragmentFlag=1;
        }
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
