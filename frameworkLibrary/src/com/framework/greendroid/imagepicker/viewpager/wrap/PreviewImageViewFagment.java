package com.framework.greendroid.imagepicker.viewpager.wrap;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.framework.R;
import com.framework.greendroid.imagepicker.models.ItemModel;
import com.framework.greendroid.imagepicker.models.ViewParams;
import com.framework.greendroid.imagepicker.models.ViewParams.ShownStyle;
import com.framework.greendroid.imagepicker.models.ViewParams.TransitionEffect;

/**
 * 预览图片
 * 
 * @author shaxiaoning
 *
 */
public class PreviewImageViewFagment {
	private ViewParams mParams;
	private FragmentManager fragmentManager;

	public PreviewImageViewFagment(Context mContext,
			FragmentManager fragmentManager) {
		this.fragmentManager = fragmentManager;
		initImageLauche(mContext);
	}

	/**
	 * 实例婚礼图片
	 * 
	 * @param parentView
	 * @param wedImg
	 */
	private void initImageLauche(Context mContext) {
		mParams = new ViewParams(mContext.getResources().getDisplayMetrics());
		initDeltableParams(mContext, mParams);
	}

	/**
	 * 初始化照片选择工具
	 * 
	 * @param params
	 */
	private void initDeltableParams(Context mContext, ViewParams params) {
		ArrayList<TransitionEffect> transitionEffects = new ArrayList<ViewParams.TransitionEffect>();
		transitionEffects.add(ViewParams.TransitionEffect.Stack);
//		transitionEffects.add(ViewParams.TransitionEffect.FlipHorizontal);
//		transitionEffects.add(ViewParams.TransitionEffect.FlipVertical);

		params.setTransitionEffects(transitionEffects);
		params.setShownStyle(ShownStyle.ViewOnly);
		params.setGridViewScrollEnable(false);
		params.setNumClumns(4);
		params.setLoadingImageDrawable(mContext.getResources().getDrawable(
				R.drawable.image_view_loading_default));
		params.setBtnBackDrawable(mContext.getResources().getDrawable(
				R.drawable.btn_back_while_bg));
		// params.setDeleteItemDrawable(mContext.getResources().getDrawable(
		// R.drawable.icon_1_delete));
		params.setBarBgColorClarity(mContext.getResources().getColor(
				R.color.image_picker_tarb_color));

	}

	/**
	 * 
	 * @param mModelsList
	 * @param position
	 */
	public void showImageLauche(ArrayList<ItemModel> mModelsList, int position) {

		final ViewPagerDialogFragment fragment = new ViewPagerDialogFragment(
				mModelsList, mParams, position);
		fragment.setStyle(DialogFragment.STYLE_NORMAL,
				android.R.style.Theme_Holo_DialogWhenLarge_NoActionBar);
		fragment.setOnEventListener(new ViewPagerListener() {

			@Override
			public void onDone(int currentPosition) {

			}

			@Override
			public void onDismiss() {
				fragment.dismiss();

			}
		});
		fragment.show(fragmentManager, "viewpager");

	}

	/**
	 * 
	 * @param mModelsList
	 * @param position
	 */
	public void showImageHasTextLauche(ArrayList<ItemModel> mModelsList,
			int position) {

		final ViewPagerDialogHasTextFragment fragment = new ViewPagerDialogHasTextFragment(
				mModelsList, mParams, position);
		fragment.setStyle(DialogFragment.STYLE_NORMAL,
				android.R.style.Theme_Holo_DialogWhenLarge_NoActionBar);
		fragment.setOnEventListener(new ViewPagerListener() {

			@Override
			public void onDone(int currentPosition) {

			}

			@Override
			public void onDismiss() {
				fragment.dismiss();

			}
		});
		fragment.show(fragmentManager, "viewpager");

	}

}
