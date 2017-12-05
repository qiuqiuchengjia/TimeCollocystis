/*
 * Copyright 2014 Habzy Huang
 */
package com.framework.greendroid.imagepicker.picker;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.R;
import com.framework.greendroid.imagepicker.models.ItemModel;
import com.framework.greendroid.imagepicker.models.ViewParams;
import com.framework.greendroid.imagepicker.models.ViewParams.ShownStyle;
import com.framework.greendroid.imagepicker.picker.GalleryAdapter.AdpterEventListener;
import com.framework.greendroid.imagepicker.tools.ImageTools;
import com.framework.greendroid.imagepicker.viewpager.wrap.ViewPagerDialogFragment;
import com.framework.greendroid.imagepicker.viewpager.wrap.ViewPagerListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class CopyOfGridViewPicker {

	protected static final String TAG = CopyOfGridViewPicker.class.getName();
	private LayoutInflater mInfalter;
	private ViewGroup mTitleBar;

	private View mImagePicker;
	private CustGridView mGridGallery;
	private GalleryAdapter mAdapter;
	private ImageView mImgNoMedia;

	private ViewParams mParams;

	private Handler mHandler;
	private Context mContext;

	private TextView mBtnDone;
	private LinearLayout mParentLayout;
	private ViewPickerListener mListener;
	private TextView mBtnBack;

	private FragmentManager mFragmentManager; // Required
	// private ImageLoader mImageLoader;
	private ArrayList<ItemModel> mModelsList;

	private OnTouchListener mOnTouchListener;

	/**
	 * @param parentView
	 */
	public CopyOfGridViewPicker(LinearLayout parentView, ViewParams params,
			ViewPickerListener listener) {
		mParentLayout = parentView;
		mParams = params;
		mListener = listener;
		mContext = parentView.getContext();
		mHandler = new Handler();
	}

	@SuppressLint("InflateParams")
	public void initialize(FragmentManager manager) {
		mFragmentManager = manager;

		mInfalter = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mTitleBar = (ViewGroup) mInfalter.inflate(R.layout.picker_title_bar,
				null);
		mImagePicker = (View) mInfalter.inflate(R.layout.image_picker, null);

		init();
		mParentLayout.addView(mTitleBar);
		mParentLayout.addView(mImagePicker);
		updateViews();
	}

	public void setOnTouchListener(OnTouchListener listener) {
		mOnTouchListener = listener;
	}

	public void setImagePath(final ArrayList<ItemModel> modelsList) {
		mModelsList = modelsList;
		new Thread() {
			// TODO Move to thread pools.
			@Override
			public void run() {
				Looper.prepare();
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// if (checkImageStatus()) {
						// mAdapter.clear();
						// } else {
						// }
						mAdapter.addAll(mModelsList);
					}
				});
				Looper.loop();
			};

		}.start();
	}

	private void init() {
		OnTouchListener touchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mOnTouchListener != null) {
					mOnTouchListener.onTouch(v, event);
				}
				return false;
			}
		};

		mGridGallery = (CustGridView) mImagePicker
				.findViewById(R.id.gridGallery);
		mGridGallery.setOnTouchListener(touchListener);
		mImgNoMedia = (ImageView) mImagePicker.findViewById(R.id.imgNoMedia);
		mImgNoMedia.setOnTouchListener(touchListener);

		mBtnDone = (TextView) mTitleBar.findViewById(R.id.picker_done);
		mBtnDone.setOnClickListener(mDoneClickListener);

		mBtnBack = (TextView) mTitleBar.findViewById(R.id.picker_back);
		mBtnBack.setOnClickListener(mBackClickListener);
	}

	private void updateViews() {
		// mImageLoader = ImageTools.getImageLoaderInstance(mContext);

//		mAdapter = new GalleryAdapter(mContext, ImageLoader.getInstance(),
//				mParams, mAdapterEventListener);
		PauseOnScrollListener listener = new PauseOnScrollListener(
				ImageLoader.getInstance(), true, true);
		mGridGallery.setOnScrollListener(listener);
		mGridGallery.setOnItemClickListener(mItemClickListener);
		mGridGallery.setNumColumns(mParams.getNumClumns());

		mGridGallery.setSrollable(mParams.isGridViewScrollEnable());

		switch (mParams.getShownStyle()) {
		case Pick_Single:
			mBtnDone.setVisibility(View.GONE);
			mGridGallery.setFastScrollEnabled(true);
			// Go on whit multiple setting.
		case Pick_Multiple:
			mTitleBar.setVisibility(View.VISIBLE);
			if (null != mParams.getTitleSt()) {
				TextView title = (TextView) (mTitleBar
						.findViewById(R.id.picker_title));
				title.setText(mParams.getTitleSt());
			}
			if (-1 != mParams.getBarBgColorOpacity()) {
				mTitleBar.setBackgroundColor(mParams.getBarBgColorOpacity());
			} else {
				mTitleBar.setBackgroundResource(R.color.bg_bar_opacity);
			}
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
			updateDoneString();
			break;
		default:
			mGridGallery.setFastScrollEnabled(true);
			mTitleBar.setVisibility(View.GONE);
			break;
		}
		mGridGallery.setAdapter(mAdapter);
	}

	private void updateDoneString() {
		String doneSt = null;
		if (null != mParams.getDoneSt()) {
			doneSt = mParams.getDoneSt();
		} else {
			doneSt = mParentLayout.getResources().getString(R.string.done);
		}
		switch (mParams.getShownStyle()) {
		case Pick_Multiple:
			mBtnDone.setText(doneSt + "(" + mAdapter.getSelected().size() + "/"
					+ mParams.getMaxPickSize() + ")");
			break;
		default:
			mBtnDone.setText(doneSt);
			break;
		}
	}

	private boolean checkImageStatus() {
		boolean result = false;
		switch (mModelsList.size()) {
		case 1:
			if (ShownStyle.ViewOnly == mParams.getShownStyle()) {
				result = true;
				mImgNoMedia.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(
						mModelsList.get(0).mPath, mImgNoMedia,
						new SimpleImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
								if (null != mParams.getLoadingImageDrawable()) {
									mImgNoMedia.setImageDrawable(mParams
											.getLoadingImageDrawable());
								} else {
									mImgNoMedia
											.setImageResource(R.drawable.no_media);
								}
								super.onLoadingStarted(imageUri, view);
							}
						});
				mImgNoMedia.setClickable(true);
				mImgNoMedia.setOnClickListener(mOnSingleImageClickListener);
			}
			break;
		case 0:
			if (ShownStyle.ViewAndDelete != mParams.getShownStyle()) {
				result = true;
				mImgNoMedia.setVisibility(View.VISIBLE);
				mImgNoMedia.setImageResource(R.drawable.no_media);
				mImgNoMedia.setClickable(false);
			}
			break;
		default:
			break;
		}
		if (!result) {
			mImgNoMedia.setVisibility(View.GONE);
		}
		return result;
	}

	View.OnClickListener mDoneClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			ArrayList<ItemModel> selected = mAdapter.getSelected();

			String[] paths = new String[selected.size()];
			for (int i = 0; i < paths.length; i++) {
				paths[i] = selected.get(i).mPath;
			}
			mListener.onDone(paths);

		}
	};

	View.OnClickListener mBackClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			mListener.onCanceled();
		}
	};

	AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> l, View v, int position, long id) {
			if (mModelsList.get(position).isFunctionItem) {
				mListener.onFunctionItemClicked(mModelsList.get(position));
				return;
			}
			showPagerView(position);
		}
	};

	private OnClickListener mOnSingleImageClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			// showPagerView(mModelsList.size());
		}
	};

	private void showPagerView(int position) {
		ViewPagerDialogFragment fragment = new ViewPagerDialogFragment(
				mModelsList, mParams, position);
		fragment.setStyle(DialogFragment.STYLE_NORMAL,
				android.R.style.Theme_Holo_DialogWhenLarge_NoActionBar);
		// fragment.setStyle(DialogFragment.STYLE_NO_TITLE,
		// android.R.style.Theme_Holo_NoActionBar_Fullscreen);
		fragment.setOnEventListener(mViewPagerListener);
		fragment.show(mFragmentManager, "viewpager");
	}

	ViewPagerListener mViewPagerListener = new ViewPagerListener() {

		@Override
		public void onDone(int currentPosition) {
			ArrayList<ItemModel> selected = mAdapter.getSelected();

			String[] paths = new String[selected.size()];
			for (int i = 0; i < paths.length; i++) {
				paths[i] = selected.get(i).mPath;
			}
			mListener.onDone(paths);
		}

		@Override
		public void onDismiss() {
			mListener.onCanceled();
			updateDoneString();
			switch (mParams.getShownStyle()) {
			case Pick_Multiple:
				mAdapter.notifyDataSetChanged();
				break;
			case ViewAndDelete:
				removeDeletedItems();
				if (mModelsList.size() != mAdapter.getCount()) {
					mAdapter.addAll(mModelsList);
					mListener.onImageDataChanged();
				}
				break;
			default:
				break;
			}
		}

	};

	private void removeDeletedItems() {
		for (int i = 0; i < mModelsList.size(); i++) {
			if (mModelsList.get(i).isSeleted) {
				mModelsList.remove(i);
				i--;
			}
		}

	}

	public void notifyDataChanged() {
		mAdapter.notifyDataSetChanged();
	}

	private AdpterEventListener mAdapterEventListener = new AdpterEventListener() {

		@Override
		public void onItemSelectedStatusChange(int position, boolean isSelected) {
			updateDoneString();
		}
	};

	public void setBackground(int color) {
		mImagePicker.setBackgroundColor(color);
	}

}
