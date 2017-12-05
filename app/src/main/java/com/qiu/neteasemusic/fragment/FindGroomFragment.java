package com.qiu.neteasemusic.fragment;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.qiu.neteasemusic.Adapter.FindListViewAdapter;
import com.qiu.neteasemusic.Adapter.GridAdapter;
import com.qiu.neteasemusic.Base.AbstractBaseFragment;
import com.qiu.neteasemusic.Bean.FindListViewBean;
import com.qiu.neteasemusic.Bean.GridViewListViewBean;
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

public class FindGroomFragment extends AbstractBaseFragment
        implements FindListViewListener, View.OnClickListener {
    private Context context;
    private ListView mListView;
    private List<Integer> mImagesList;
    private FindListViewAdapter mAdapter;
    private List<FindListViewBean> mListdata;
    private FindListViewBean mBean = null;
    private GridViewListViewBean mGridBean=null;
    private List<GridViewListViewBean> listGrid;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_find_person_groom;
    }

    @Override
    protected void initView(View v) {
        context = getContext();
        mImagesList = new ArrayList<>();
        mListView = (ListView) v.findViewById(R.id.lv_find);
        mListdata = new ArrayList<>();
        mAdapter = new FindListViewAdapter(context, mListdata, this);
        mListView.setAdapter(mAdapter);
        mImagesList = new ArrayList<>();
        mImagesList.add(R.mipmap.first);
        mImagesList.add(R.mipmap.second);
        mImagesList.add(R.mipmap.third);
        mImagesList.add(R.mipmap.fourth);
        mImagesList.add(R.mipmap.five);
        mImagesList.add(R.mipmap.six);
        mImagesList.add(R.mipmap.seven);
    }

    @Override
    protected void initData() {
        mBean = new FindListViewBean();
        mBean.setItemType(FindListViewBean.ITEM_TYPE_BANNER);//轮播图
        mListdata.add(mBean);


        mBean = new FindListViewBean();
        mBean.setItemType(FindListViewBean.ITEM_TYPE_PRIVATE_GROOM);//个人推荐
        mListdata.add(mBean);

        mBean = new FindListViewBean();
        mBean.setItemType(FindListViewBean.ITEM_TYPE_SPLIT_LINE);//分割线
        mListdata.add(mBean);

        mBean = new FindListViewBean();
        mBean.setItemType(FindListViewBean.ITEM_TYPE_GRID_GROOM);//推荐歌单
        mListdata.add(mBean);


        mAdapter.refresh(mListdata);

    }

    @Override
    public void initGridViewsSetting(final View convertView, FindListViewAdapter.ViewHolder holder, int position) {
        FindListViewBean findListViewBean = mListdata.get(position);
        if (findListViewBean != null) {
            switch (findListViewBean.getItemType()) {
                case FindListViewBean.ITEM_TYPE_BANNER://轮播图
                    initBannerViews(holder.banner, holder.tv_title);
                    holder.tv_title.setOnClickListener(this);
                    initBannerEvent(holder.banner, holder.tv_title);
                    break;
                case FindListViewBean.ITEM_TYPE_PRIVATE_GROOM://私人推荐
                    holder.tv_title.setText("15");
                    holder.ll_fm.setOnClickListener(this);
                    holder.ll_day.setOnClickListener(this);
                    holder.ll_hot.setOnClickListener(this);
                    break;
                case FindListViewBean.ITEM_TYPE_GRID_GROOM://推荐歌单
                    initGridGroom(convertView,holder,position);
                    break;
            }
        }
    }


    private void initGridGroom(View convertView, FindListViewAdapter.ViewHolder holder, int position) {


        listGrid = new ArrayList<>();

        mGridBean = new GridViewListViewBean();
        mGridBean.setTitle("粤语歌里的百年孤独");
        mGridBean.setIco(R.drawable.title_page_1);
        mGridBean.setDescribe("155万");
        listGrid.add(mGridBean);

        mGridBean = new GridViewListViewBean();
        mGridBean.setTitle("粤语歌里的百年孤独");
        mGridBean.setIco(R.drawable.title_page_2);
        mGridBean.setDescribe("155万");
        listGrid.add(mGridBean);

        mGridBean = new GridViewListViewBean();
        mGridBean.setTitle("有没有一首歌，让你瞬间热泪盈眶,让你瞬间热泪盈眶,让你瞬间热泪盈眶,让你瞬间热泪盈眶");
        mGridBean.setIco(R.drawable.title_page_3);
        mGridBean.setDescribe("155万");
        listGrid.add(mGridBean);

        mGridBean = new GridViewListViewBean();
        mGridBean.setTitle("粤语歌里的百年孤独");
        mGridBean.setIco(R.drawable.title_page_4);
        mGridBean.setDescribe("155万");
        listGrid.add(mGridBean);

        mGridBean = new GridViewListViewBean();
        mGridBean.setTitle("粤语歌里的百年孤独");
        mGridBean.setIco(R.drawable.title_page_5);
        mGridBean.setDescribe("155万");
        listGrid.add(mGridBean);

        mGridBean = new GridViewListViewBean();
        mGridBean.setTitle("粤语歌里的百年孤独");
        mGridBean.setIco(R.drawable.title_page_6);
        mGridBean.setDescribe("155万");
        listGrid.add(mGridBean);

        GridAdapter gridAdapter = new GridAdapter(context,listGrid,this);
        holder.gv_find.setAdapter(gridAdapter);
        gridAdapter.refresh(listGrid);

        holder.gv_find.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.showToast(context,""+position);
            }
        });
        holder.ll_fm.setOnClickListener(this);


    }


    private void initBannerEvent(Banner banner, final TextView tv_title) {
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                ToastUtil.showToast(context, "点击了=" + position);
            }
        });
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 1) {
                    tv_title.setText("电台");
                } else {
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
        if (id == R.id.tv_banner_detail) {
            ToastUtil.showToast(context, "独家专访");
        } else if (id == R.id.ll_find_fm) {
            ToastUtil.showToast(context, "私人FM");
        } else if (id == R.id.ll_find_day_song) {
            ToastUtil.showToast(context, "每日歌曲推荐");
        } else if (id == R.id.ll_find_hot_song) {
            ToastUtil.showToast(context, "云音乐热歌榜");
        }else if(id==R.id.ll_title_item){
            ToastUtil.showToast(context,"推荐歌单");
        }
    }
}
