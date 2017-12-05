package com.qiu.neteasemusic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.framework.greendroid.viewpageindicator.SlidingTabLayout;
import com.qiu.neteasemusic.Base.AbstractBaseFragment;
import com.qiu.neteasemusic.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiu on 2017/4/11.
 * 发现
 */

public class FindFragment extends AbstractBaseFragment {
    private String CONTENT[] ;
    private ViewPager viewPager = null;
    private TabTitleFragmentAdapter fragmentAdapter = null;
    private Context context;
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initView(View v) {
        context=getContext();
        CONTENT = new String[]{
                context.getResources().getString(R.string.text_find_person_groom),
                context.getResources().getString(R.string.text_find_song_list),
                context.getResources().getString(R.string.text_find_anchor_dj),
                context.getResources().getString(R.string.text_find_ranking_list)
        };
        fragmentAdapter = new TabTitleFragmentAdapter(getActivity().getSupportFragmentManager());
        viewPager = (ViewPager) v.findViewById(R.id.fragment_find_view_pager);
        viewPager.setAdapter(fragmentAdapter);
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) v.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        slidingTabLayout.setViewPager(viewPager);
        slidingTabLayout.setSelectedIndicatorColors(
                getResources().getColor(R.color.base_color_red_D23A2F));
        slidingTabLayout.setDistributeEvenly(true);
    }

    @Override
    protected void initData() {

    }
    class TabTitleFragmentAdapter extends FragmentPagerAdapter {
        private Map<Integer, String> mFragmentTags;
        private FragmentManager mFragmentManager;

        public TabTitleFragmentAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
            mFragmentTags = new HashMap<Integer, String>();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            Bundle bundle = null;
            switch (position) {
                case 0:
                    // f = new TaskTecRecommendFragment(getActivity(), 0);
                    bundle = new Bundle();
                    bundle.putInt("type", 0);
                    f = Fragment.instantiate(getActivity(), FindGroomFragment.class.getName(), bundle);
                    break;
                case 1:
                    bundle = new Bundle();
                    bundle.putInt("status", 1);
                    // f = new TaskTecRecommendFragment(getActivity(), 1);
                    f = Fragment.instantiate(getActivity(), FindSongFragment.class.getName(), bundle);
                    break;
                case 2:
                    bundle = new Bundle();
                    bundle.putInt("status", 2);
                    // f = new TaskTecRecommendFragment(getActivity(), 1);
                    f = Fragment.instantiate(getActivity(), FindAnchorDJFragment.class.getName(), bundle);
                    break;
                case 3:
                    bundle = new Bundle();
                    bundle.putInt("status", 3);
                    // f = new TaskTecRecommendFragment(getActivity(), 1);
                    f = Fragment.instantiate(getActivity(), FindRankingListFragment.class.getName(), bundle);
                    break;

            }
            return f;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                // record the fragment tag here.
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        public Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);
            if (tag == null)
                return null;
            return mFragmentManager.findFragmentByTag(tag);
        }
    }

}
