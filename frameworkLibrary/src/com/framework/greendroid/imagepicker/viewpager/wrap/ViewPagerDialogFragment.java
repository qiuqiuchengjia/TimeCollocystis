/*
 * Copyright 2014 Habzy Huang
 */
package com.framework.greendroid.imagepicker.viewpager.wrap;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.framework.R;
import com.framework.greendroid.imagepicker.jazzyviewpager.JazzyViewPager;
import com.framework.greendroid.imagepicker.jazzyviewpager.PhotoViewListener;
import com.framework.greendroid.imagepicker.models.ItemModel;
import com.framework.greendroid.imagepicker.models.ViewParams;
import com.framework.greendroid.imagepicker.models.ViewParams.ShownStyle;
import com.framework.log.DebugLog;
import com.framework.log.LogUtil;

@SuppressLint("ValidFragment")
public class ViewPagerDialogFragment extends DialogFragment {
	private static final String LOGTAG = LogUtil
			.makeLogTag(ViewPagerDialogFragment.class);
	private static final String TAG = ViewPagerDialogFragment.class
			.getSimpleName();
	private JazzyViewPager mJazzy;
	private int mCurrentItem;
	private ArrayList<ItemModel> mModelsList = new ArrayList<ItemModel>();
	private ViewPagerListener mViewPagerEventListener;
	private RelativeLayout mPagerTitleBar;
	private RelativeLayout mPagerBottomBar;
	private TextView mBtnBack;
	private TextView mTitle;
	private TextView mBtnDone;
	private ImageView mBottonIcon;
	private ViewParams mParams;
	private boolean isFullScreen = false;

	public ViewPagerDialogFragment(ArrayList<ItemModel> modelsList,
			ViewParams params, int currentItem) {
		mModelsList.addAll(modelsList);
		mCurrentItem = currentItem;
		removeFunctionItems();
		mParams = params;
	}

	private void removeFunctionItems() {
		for (int i = 0; i < mModelsList.size(); i++) {
			if (mModelsList.get(i).isFunctionItem) {
				if (mCurrentItem > i) {
					mCurrentItem = mCurrentItem - 1;
				}
				mModelsList.remove(i);
				i--;
			}
		}
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		View view_pager = inflater.inflate(R.layout.picker_view_pager, null);
		mJazzy = (JazzyViewPager) view_pager.findViewById(R.id.jazzy_pager);
		mPagerTitleBar = (RelativeLayout) view_pager
				.findViewById(R.id.pager_title_bar);
		mPagerBottomBar = (RelativeLayout) view_pager
				.findViewById(R.id.pager_bottom_bar);

		initViews();

		return view_pager;
	}

	private void initViews() {
		mJazzy.setImagePath(mModelsList, mParams);
		mJazzy.setCurrentItem(mCurrentItem);
		mJazzy.setPageMargin(0);

		mBtnBack = (TextView) mPagerTitleBar.findViewById(R.id.picker_back);
		mBtnDone = (TextView) mPagerTitleBar.findViewById(R.id.picker_done);
		mTitle = (TextView) (mPagerTitleBar.findViewById(R.id.picker_title));
		mBottonIcon = (ImageView) mPagerBottomBar
				.findViewById(R.id.bottom_icon);

		mJazzy.setOnPageChangeListener(mOnPageChangeListener);
		mJazzy.setPhotoViewListener(mPhotoViewListener);
		mBtnBack.setOnClickListener(mOnBackClickListener);
		mBtnDone.setOnClickListener(mOnDoneClickListener);

		mJazzy.setTransitionEffect(mParams.getTransitionEffect());
		if (-1 != mParams.getBarBgColorClarity()) {
			mPagerTitleBar.setBackgroundColor(mParams.getBarBgColorClarity());
			mPagerBottomBar.setBackgroundColor(mParams.getBarBgColorClarity());
		} else {
			mPagerTitleBar.setBackgroundResource(R.color.bg_bar_clarity);
			mPagerBottomBar.setBackgroundResource(R.color.bg_bar_clarity);
		}

		updateTitle();

		if (null != mParams.getBtnBackDrawable()) {
			mBtnBack.setBackgroundDrawable(mParams.getBtnBackDrawable());
		} else {
			mBtnBack.setBackgroundResource(R.drawable.picker_icon_back);
		}
		if (null != mParams.getBtnDoneBgDrawable()) {
			mBtnDone.setBackgroundDrawable(mParams.getBtnDoneBgDrawable());
		} else {
			mBtnDone.setBackgroundResource(R.color.clarity);
		}
		// if (mParams.getmBarTitleColor() != -1)

		switch (mParams.getShownStyle()) {
		case Pick_Multiple:
			mBottonIcon.setSelected(mModelsList.get(mCurrentItem).isSeleted);
			mBottonIcon.setOnClickListener(mOnCheckBoxClickedListener);
			if (mParams.getCheckBoxDrawable() != null) {
				mBottonIcon.setImageDrawable(mParams.getCheckBoxDrawable());
			}
			mPagerBottomBar.setVisibility(View.VISIBLE);
			mTitle.setTextColor(getActivity().getResources().getColor(
					R.color.while_text_color));
			break;
		case Pick_Single:
			mPagerBottomBar.setVisibility(View.GONE);
			mTitle.setTextColor(getActivity().getResources().getColor(
					R.color.while_text_color));
			break;
		case ViewOnly:
			mBtnDone.setVisibility(View.GONE);
			mPagerBottomBar.setVisibility(View.GONE);
			mTitle.setTextColor(getActivity().getResources().getColor(
					R.color.while_text_color));
			break;
		case ViewAndDelete:
			mBtnDone.setVisibility(View.GONE);
			mBottonIcon.setImageResource(R.drawable.icon_delete);
			mBottonIcon.setOnClickListener(mOnDeleteClickedListener);
			if (mParams.getDeleteItemDrawable() != null) {
				mBottonIcon.setImageDrawable(mParams.getDeleteItemDrawable());
			}
			mPagerBottomBar.setVisibility(View.VISIBLE);
			mTitle.setTextColor(getActivity().getResources().getColor(
					R.color.while_text_color));
			break;
		default:
			break;
		}
	}

	private void updateTitle() {

		// mTitle.setText(""
		// + (mCurrentItem == 1 ? mCurrentItem : (mCurrentItem + 1) )
		// + "/" + mModelsList.size());
		if (mModelsList != null && mModelsList.size() == 1) {
			if (mCurrentItem == 0)
				mTitle.setText("" + (mCurrentItem + 1) + "/"
						+ mModelsList.size());
		} else {

			mTitle.setText("" + (mCurrentItem + 1) + "/" + mModelsList.size());
		}
		String doneSt = null;
		if (null != mParams.getDoneSt()) {
			doneSt = mParams.getDoneSt();
		} else {
			doneSt = mJazzy.getResources().getString(R.string.done);
		}
		switch (mParams.getShownStyle()) {
		case Pick_Multiple:
			mBtnDone.setText(doneSt + "(" + getSelectedSize() + "/"
					+ mParams.getMaxPickSize() + ")");
			break;
		default:
			mBtnDone.setText(doneSt);
			break;
		}
	}

	private void fullScreen() {
		isFullScreen = !isFullScreen;
		if (isFullScreen) {
			mPagerTitleBar.setVisibility(View.GONE);
			mPagerBottomBar.setVisibility(View.GONE);
		} else {
			mPagerTitleBar.setVisibility(View.VISIBLE);
			switch (mParams.getShownStyle()) {
			case Pick_Multiple:
				mPagerBottomBar.setVisibility(View.VISIBLE);
				break;
			case Pick_Single:
				mPagerBottomBar.setVisibility(View.GONE);
				break;
			case ViewOnly:
				mPagerBottomBar.setVisibility(View.GONE);
				break;
			case ViewAndDelete:
				mPagerBottomBar.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}
	}

	private OnClickListener mOnBackClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mViewPagerEventListener.onDismiss();
			ViewPagerDialogFragment.this.dismiss();
		}
	};

	private OnClickListener mOnDoneClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (ShownStyle.Pick_Single == mParams.getShownStyle()) {
				mModelsList.get(mCurrentItem).isSeleted = true;
			}
			mViewPagerEventListener.onDone(mCurrentItem);
			ViewPagerDialogFragment.this.dismiss();
		}
	};

	private OnClickListener mOnCheckBoxClickedListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int position = mJazzy.getCurrentItem();
			boolean isSelected = mModelsList.get(position).isSeleted;
			if (!isSelected && getSelectedSize() >= mParams.getMaxPickSize()) {
				if (null != mParams.getToastForReachingMax()) {
					Toast.makeText(v.getContext(),
							mParams.getToastForReachingMax(),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(v.getContext(), R.string.reached_max_size,
							Toast.LENGTH_SHORT).show();
				}
				return;
			}
			mBottonIcon.setSelected(!isSelected);
			mModelsList.get(position).isSeleted = !isSelected;
			updateTitle();
		}

	};

	private int getSelectedSize() {
		int size = 0;
		for (int i = 0; i < mModelsList.size(); i++) {
			if (mModelsList.get(i).isSeleted) {
				size = size + 1;
			}
		}
		return size;
	}

	private OnClickListener mOnDeleteClickedListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int position = mJazzy.getCurrentItem();
			mModelsList.get(position).isSeleted = true;
			if (mModelsList.size() == 1) {
				mModelsList.remove(position);
				mBtnBack.performClick();
			} else {
				mModelsList.remove(position);
				mJazzy.notifyChange();
				updateTitle();
			}
		}

	};

	private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			DebugLog.i(LOGTAG, "position:" + position);
			mCurrentItem = position;
			boolean isSelected = mModelsList.get(position).isSeleted;
			mBottonIcon.setSelected(isSelected);
			updateTitle();
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	private PhotoViewListener mPhotoViewListener = new PhotoViewListener() {

		@Override
		public void onPhotoClicked() {
			Log.d(TAG, "=======FullScreen");
			fullScreen();
		}

	};

	public void setOnEventListener(ViewPagerListener viewPagerEventListener) {
		mViewPagerEventListener = viewPagerEventListener;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		mViewPagerEventListener.onDismiss();
		super.onDismiss(dialog);
	}

}
