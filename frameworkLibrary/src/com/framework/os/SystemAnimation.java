package com.framework.os;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.framework.R;
import com.framework.greendroid.widget.CustomToast;
import com.framework.log.DebugLog;
import com.framework.log.LogUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 系统退出动画
 * 
 * @author sxn
 * 
 */
public class SystemAnimation {
	private static final String LOGTAG = LogUtil
			.makeLogTag(SystemAnimation.class);
	private static long mCurrentTime = 0;
	private static boolean isExitanimStart = false;

	public SystemAnimation() {

	}

	/**
	 * 退出系統
	 * 
	 * @param mContext
	 * @param parentView
	 *            parent view
	 * @param message
	 *            toast message
	 * @param listener
	 *            animation listener
	 */
	public static boolean systemExit(Context mContext, View parentView,
			String message, AnimationListener listener) {
		if ((System.currentTimeMillis() - mCurrentTime) > 2000) {
			// DebugLog.showToast(mContext, message);
			CustomToast.showCustomToast(mContext, message);
			mCurrentTime = System.currentTimeMillis();
			isExitanimStart = false;
		} else {
			if (!isExitanimStart) {
				Animation loAnimation = AnimationUtils.loadAnimation(mContext,
						R.anim.tv_off);
				loAnimation.setAnimationListener(listener);
				parentView.startAnimation(loAnimation);
				isExitanimStart = true;
			}
		}
		return isExitanimStart;
	}

	public static void fadeTransition(Context mContext) {
		((Activity) mContext).overridePendingTransition(R.anim.fade,
				R.anim.hold);
	}

	/**
	 * view隐藏动画
	 * 
	 * @param view
	 */
	public static final void viewVisiableAnimation(Context context, View view) {
		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.popup_bottom_in);
		view.setAnimation(animation);
		view.setVisibility(View.VISIBLE);
	}

	/**
	 * view 显示动画
	 * 
	 * @param view
	 */
	public static final void viewInVisiableAnimation(Context context, View view) {
		Animation animation1 = AnimationUtils.loadAnimation(context,
				R.anim.popup_bottom_out);
		view.setAnimation(animation1);
		view.setVisibility(View.INVISIBLE);

	}

	/**
	 * view 显示动画
	 * 
	 * @param view
	 */
	public static final void viewGoneAnimation(Context context, View view) {
		Animation animation1 = AnimationUtils.loadAnimation(context,
				R.anim.popup_bottom_out);
		view.setAnimation(animation1);
		view.setVisibility(View.GONE);

	}

	// Hold a reference to the current animator,
	// so that it can be canceled mid-way.
	private Animator mCurrentAnimator;

	// The system "short" animation time duration, in milliseconds. This
	// duration is ideal for subtle animations or animations that occur
	// very frequently.
	private int mShortAnimationDuration;

	/**
	 * 
	 * @param parentView
	 * @param thumbView
	 *            thumb view
	 * @param zoomView
	 *            display view
	 * @param imageResId
	 */
	public void zoomImageFromThumb(View parentView, final View thumbView,
			final ImageView zoomView, String url, DisplayImageOptions options) {
		// If there's an animation in progress, cancel it
		// immediately and proceed with this one.
		if (mCurrentAnimator != null) {
			mCurrentAnimator.cancel();
		}
		ImageLoader.getInstance().displayImage(url, zoomView, options,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingComplete(String arg0, View view,
							Bitmap arg2) {
						PhotoViewAttacher pAttacher = new PhotoViewAttacher(
								(ImageView) view);

						pAttacher
								.setOnPhotoTapListener(new OnPhotoTapListener() {

									@Override
									public void onPhotoTap(View view, float x,
											float y) {
										thumbView.setAlpha(1f);
										mCurrentAnimator = null;
										zoomView.setVisibility(View.GONE);
									}
								});

					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub

					}
				});

		// Load the high-resolution "zoomed-in" image.
		// final ImageView expandedImageView = (ImageView) findViewById(
		// resID);

		// Calculate the starting and ending bounds for the zoomed-in image.
		// This step involves lots of math. Yay, math.
		final Rect startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();

		// The start bounds are the global visible rectangle of the thumbnail,
		// and the final bounds are the global visible rectangle of the
		// container
		// view. Also set the container view's offset as the origin for the
		// bounds, since that's the origin for the positioning animation
		// properties (X, Y).
		thumbView.getGlobalVisibleRect(startBounds);
		parentView.getGlobalVisibleRect(finalBounds, globalOffset);
		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);

		// Adjust the start bounds to be the same aspect ratio as the final
		// bounds using the "center crop" technique. This prevents undesirable
		// stretching during the animation. Also calculate the start scaling
		// factor (the end scaling factor is always 1.0).
		float startScale;
		if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds
				.width() / startBounds.height()) {
			// Extend start bounds horizontally
			startScale = (float) startBounds.height() / finalBounds.height();
			float startWidth = startScale * finalBounds.width();
			float deltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= deltaWidth;
			startBounds.right += deltaWidth;
		} else {
			// Extend start bounds vertically
			startScale = (float) startBounds.width() / finalBounds.width();
			float startHeight = startScale * finalBounds.height();
			float deltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= deltaHeight;
			startBounds.bottom += deltaHeight;
		}

		// Hide the thumbnail and show the zoomed-in view. When the animation
		// begins, it will position the zoomed-in view in the place of the
		// thumbnail.
		thumbView.setAlpha(0f);
		zoomView.setVisibility(View.VISIBLE);

		// Set the pivot point for SCALE_X and SCALE_Y transformations
		// to the top-left corner of the zoomed-in view (the default
		// is the center of the view).
		zoomView.setPivotX(0f);
		zoomView.setPivotY(0f);

		// Construct and run the parallel animation of the four translation and
		// scale properties (X, Y, SCALE_X, and SCALE_Y).
		AnimatorSet set = new AnimatorSet();
		set.play(
				ObjectAnimator.ofFloat(zoomView, View.X, startBounds.left,
						finalBounds.left))
				.with(ObjectAnimator.ofFloat(zoomView, View.Y, startBounds.top,
						finalBounds.top))
				.with(ObjectAnimator.ofFloat(zoomView, View.SCALE_X,
						startScale, 1f))
				.with(ObjectAnimator.ofFloat(zoomView, View.SCALE_Y,
						startScale, 1f));
		set.setDuration(mShortAnimationDuration);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mCurrentAnimator = null;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				mCurrentAnimator = null;
			}
		});
		set.start();
		mCurrentAnimator = set;

		// Upon clicking the zoomed-in image, it should zoom back down
		// to the original bounds and show the thumbnail instead of
		// the expanded image.
		final float startScaleFinal = startScale;
		zoomView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mCurrentAnimator != null) {
					mCurrentAnimator.cancel();
				}

				// Animate the four positioning/sizing properties in parallel,
				// back to their original values.
				AnimatorSet set = new AnimatorSet();
				set.play(
						ObjectAnimator.ofFloat(zoomView, View.X,
								startBounds.left))
						.with(ObjectAnimator.ofFloat(zoomView, View.Y,
								startBounds.top))
						.with(ObjectAnimator.ofFloat(zoomView, View.SCALE_X,
								startScaleFinal))
						.with(ObjectAnimator.ofFloat(zoomView, View.SCALE_Y,
								startScaleFinal));
				set.setDuration(mShortAnimationDuration);
				set.setInterpolator(new DecelerateInterpolator());
				set.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						// thumbView.setAlpha(1f);
						// zoomView.setVisibility(View.GONE);
						mCurrentAnimator = null;
					}

					@Override
					public void onAnimationCancel(Animator animation) {
						// thumbView.setAlpha(1f);
						// zoomView.setVisibility(View.GONE);
						mCurrentAnimator = null;
					}
				});
				set.start();
				mCurrentAnimator = set;
			}
		});
	}
}
