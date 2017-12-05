package com.framework.greendroid.imagepicker.jazzyviewpager;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.framework.R;
import com.framework.greendroid.imagepicker.models.ItemModel;
import com.framework.greendroid.imagepicker.models.ViewParams;
import com.framework.io.ImageLoaderManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class JazzyPagerAdapter extends PagerAdapter {

	// private ImageLoader mImageLoader;
	ArrayList<ItemModel> mModelList;
	private JazzyViewPager mJazzy;
	private PhotoViewListener mPhotoViewListener;
	private final ViewParams mParams;
	private DisplayImageOptions options;

	public JazzyPagerAdapter(JazzyViewPager jazzy, ViewParams params) {
		// mImageLoader = ImageTools.getImageLoaderInstance(jazzy.getContext());
		mJazzy = jazzy;
		mParams = params;
		options = ImageLoaderManager.initDisplayImageOptions(
				R.drawable.loading_big_bg, R.drawable.loading_big_bg,
				R.drawable.loading_big_bg);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		final PhotoView photoView = new PhotoView(container.getContext());
		photoView.setClickable(true);
		photoView.setOnPhotoTapListener(mOnPhotoTapListener);

		ImageLoader.getInstance().displayImage(mModelList.get(position).mPath,
				photoView, options, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						if (null != mParams.getLoadingImageDrawable()) {
							photoView.setImageDrawable(mParams
									.getLoadingImageDrawable());
						} else {
							photoView.setImageResource(R.drawable.no_media);
						}
						super.onLoadingStarted(imageUri, view);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						if (null != mParams.getLoadingFailedDrawable()) {
							photoView.setImageDrawable(mParams
									.getLoadingFailedDrawable());
						} else {
							photoView.setImageResource(R.drawable.failed);
						}
						super.onLoadingFailed(imageUri, view, failReason);
					}
				});

		container.addView(photoView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mJazzy.setObjectForPosition(photoView, position);
		return photoView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object obj) {
		container.removeView(mJazzy.findViewFromObject(position));
	}

	@Override
	public int getCount() {
		return mModelList.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		if (view instanceof OutlineContainer) {
			return ((OutlineContainer) view).getChildAt(0) == obj;
		} else {
			return view == obj;
		}
	}

	public void setImagePath(ArrayList<ItemModel> galleryPhotos) {
		mModelList = galleryPhotos;
	}

	/**
	 * @param photoViewListener
	 *            the mPhotoViewListener to set
	 */
	public void setPhotoViewListener(PhotoViewListener photoViewListener) {
		this.mPhotoViewListener = photoViewListener;
	}

	private OnPhotoTapListener mOnPhotoTapListener = new OnPhotoTapListener() {

		@Override
		public void onPhotoTap(View arg0, float arg1, float arg2) {
			if (null != mPhotoViewListener) {
				mPhotoViewListener.onPhotoClicked();
			}
		}
	};
}
