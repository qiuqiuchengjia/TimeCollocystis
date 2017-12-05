package com.qiu.neteasemusic.fragment;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.qiu.neteasemusic.Adapter.FindListViewAdapter;
import com.qiu.neteasemusic.Base.AbstractBaseFragment;
import com.qiu.neteasemusic.Bean.FindListViewBean;
import com.qiu.neteasemusic.Interface.FindListViewListener;
import com.qiu.neteasemusic.R;
import com.qiu.neteasemusic.Utils.GlideImageLoader;
import com.qiu.neteasemusic.Utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiu on 2017/4/13.
 */

public class FindAnchorDJFragment extends AbstractBaseFragment
        implements FindListViewListener,View.OnClickListener {
    private Context context;
    private ListView mListView;
    private List<Integer> mImagesList;
    private FindListViewAdapter mAdapter;
    private List<FindListViewBean> mListdata;
    private FindListViewBean mBean =null;
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_find_ganchor_dj;
    }

    @Override
    protected void initView(View v) {
        context=getContext();
        mImagesList =new ArrayList<>();
        mListView = (ListView) v.findViewById(R.id.lv_find);
        mListdata =new ArrayList<>();
        mAdapter =new FindListViewAdapter(context, mListdata,this);
        mListView.setAdapter(mAdapter);
        mImagesList = new ArrayList<>();
        mImagesList.add(R.mipmap.second);
        mImagesList.add(R.mipmap.seven);
    }

    @Override
    protected void initData() {
        mBean = new FindListViewBean();
        mBean.setItemType(FindListViewBean.ITEM_TYPE_BANNER);//轮播图
        mListdata.add(mBean);

        mAdapter.refresh(mListdata);
    }

    @Override
    public void initGridViewsSetting(final View convertView, FindListViewAdapter.ViewHolder holder, int position) {
        FindListViewBean findListViewBean = mListdata.get(position);
        if(findListViewBean!=null){
            switch (findListViewBean.getItemType()){
                case FindListViewBean.ITEM_TYPE_BANNER://轮播图
                    initBannerViews(holder.banner,holder.tv_title);
                    holder.tv_title.setOnClickListener(this);
                    initBannerEvent(holder.banner,holder.tv_title);
                    break;
            }
        }
    }

    private void initBannerEvent(Banner banner, final TextView tv_title) {
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                ToastUtil.showToast(context,"点击了="+position);
            }
        });
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position==1){
                    tv_title.setText("电台");
                }else{
                    tv_title.setText("电台节目");
                }
            }
            @Override
            public void onPageSelected(int position) {

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initBannerViews(Banner banner, final TextView tv_title) {
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(mImagesList);
        banner.setDelayTime(4000);
        banner.start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.tv_banner_detail){
            ToastUtil.showToast(context,"独家专访");
        }
    }
}
