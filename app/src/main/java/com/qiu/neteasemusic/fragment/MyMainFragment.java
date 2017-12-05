package com.qiu.neteasemusic.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.qiu.neteasemusic.Adapter.MyMainFragmentAdapter;
import com.qiu.neteasemusic.Base.AbstractBaseFragment;
import com.qiu.neteasemusic.Bean.MyMainFragmentBean;
import com.qiu.neteasemusic.R;
import com.qiu.neteasemusic.Utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiu on 2017/4/11.
 * 我的
 */

public class MyMainFragment  extends AbstractBaseFragment {
    private ListView listView=null;
    private MyMainFragmentAdapter mAdapter=null;
    private List<MyMainFragmentBean> listdata=null;
    private MyMainFragmentBean mBean=null;
    private SwipeRefreshLayout mySwipeRefreshLayout=null;
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_my_main;
    }

    @Override
    protected void initView(View v) {
        listView = (ListView) v.findViewById(R.id.fragment_my_pullListView);
        mySwipeRefreshLayout= (SwipeRefreshLayout) v.findViewById(R.id.srl_main);
        listdata=new ArrayList<>();
        mAdapter=new MyMainFragmentAdapter(getContext(),listdata);
        listView.setAdapter(mAdapter);
        initSwipeRefreshLayout(mySwipeRefreshLayout);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.showToast(getContext(),position+"");
            }
        });
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // 刷新动画开始后回调到此方法
                        ToastUtil.showToast(getContext(),"刷新");
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

    }

    @Override
    protected void initData() {

        mBean=new MyMainFragmentBean();
        mBean.setItemType(mBean.ITEM_TYPE_LOCAL_MUSIC);
        mBean.setItemIco(R.mipmap.local_music);
        mBean.setItemName("本地音乐");
        mBean.setItemNumName("(0)");
        listdata.add(mBean);

        mBean=new MyMainFragmentBean();
        mBean.setItemType(mBean.ITEM_TYPE_LATELY_PLAY);
        mBean.setItemIco(R.mipmap.actionbar_discover_normal);
        mBean.setItemName("最近播放");
        mBean.setItemNumName("(0)");
        listdata.add(mBean);

        mBean=new MyMainFragmentBean();
        mBean.setItemType(mBean.ITEM_TYPE_DOWNLOAD);
        mBean.setItemIco(R.mipmap.music_icn_dld);
        mBean.setItemName("下载管理");
        mBean.setItemNumName("(0)");
        listdata.add(mBean);

        mBean=new MyMainFragmentBean();
        mBean.setItemType(mBean.ITEM_TYPE_DJ);
        mBean.setItemIco(R.mipmap.music_icn_dj);
        mBean.setItemName("我的电台");
        mBean.setItemNumName("(0)");
        listdata.add(mBean);

        mBean=new MyMainFragmentBean();
        mBean.setItemType(mBean.ITEM_TYPE_COLLECT);
        mBean.setItemIco(R.mipmap.actionbar_friends_normal);
        mBean.setItemName("我的收藏");
        mBean.setItemNumName("(0)");
        listdata.add(mBean);

        mAdapter.refresh(listdata);
    }
}
