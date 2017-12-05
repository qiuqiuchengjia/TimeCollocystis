package com.framework.greendroid.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.framework.R;
import com.framework.log.LogUtil;

/**
 * custom toast
 * 
 * @author sxn
 * 
 */
public class CustomToast {
	private static String LOGTAG = LogUtil.makeLogTag(CustomToast.class);

	public CustomToast() {

	}

	/**
	 * bottom toast
	 * 
	 * @param mContext
	 * @param des
	 */
	@Deprecated
	public static void showBottomToast(Context mContext, String des) {
		Toast toast = new Toast(mContext);
		// Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 100);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.toast_layout, null);
		toast.setView(view);
		TextView txtDes = (TextView) view
				.findViewById(R.id.custom_toast_text_des);
		txtDes.setText(des);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
	}

	/**
	 * error toast
	 * 
	 * @param mContext
	 * @param des
	 */
	@Deprecated
	public static void showErrorTopToast(Context mContext, String des) {
		if (mContext == null)
			return;
		Toast toast = new Toast(mContext);
		// Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 20);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.toast_error_layout, null);
		toast.setView(view);
		TextView txtDes = (TextView) view
				.findViewById(R.id.custom_toast_text_des);
		txtDes.setText(des);
		toast.setDuration(Toast.LENGTH_SHORT);
		// DebugLog.i(LOGTAG, "toast.getDuration():"+toast.getDuration());
		toast.show();
	}

	/**
	 * 正确toast top toast
	 * 
	 * @param mContext
	 * @param des
	 */
	@Deprecated
	public static void showOccectTopToast(Context mContext, String des) {
		Toast toast = new Toast(mContext);
		// Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 20);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.toast_correct_layout, null);
		toast.setView(view);
		TextView txtDes = (TextView) view
				.findViewById(R.id.custom_toast_text_des);
		txtDes.setText(des);
		toast.setDuration(Toast.LENGTH_SHORT);
		// DebugLog.i(LOGTAG, "toast.getDuration():"+toast.getDuration());
		toast.show();
	}

	/**
	 * hint toast
	 * 
	 * @param mContext
	 * @param des
	 */
	@Deprecated
	public static void showHintTopToast(Context mContext, String des,
			int duration) {
		if (mContext == null)
			return;
		Toast toast = new Toast(mContext);
		// Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 20);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.toast_error_layout, null);
		toast.setView(view);
		TextView txtDes = (TextView) view
				.findViewById(R.id.custom_toast_text_des);
		txtDes.setText(des);
		toast.setDuration(duration);
		toast.show();
	}

	/**
	 * 自定义toast 黑色背景
	 * 
	 * @param mContext
	 * @param des
	 */
	public static void showCustomToast(Context mContext, String des) {
		try {
			Toast toast = new Toast(mContext);
			// Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 100);
			LayoutInflater inflater = LayoutInflater.from(mContext);
			View view = inflater.inflate(R.layout.custom_toast_layout, null);
			toast.setView(view);
			TextView txtDes = (TextView) view
					.findViewById(R.id.custom_toast_text_des);
			txtDes.setText(des);
			toast.setDuration(Toast.LENGTH_SHORT);
			// DebugLog.i(LOGTAG, "toast.getDuration():"+toast.getDuration());
			toast.show();

		} catch (Exception e) {

		}
	}

	/**
	 * 错误消息提示
	 * 
	 * @param mContext
	 * @param des
	 */
	public static void showCustomErrorToast(Context mContext, String des) {
		try {

			Toast toast = new Toast(mContext);
			// Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 100);
			LayoutInflater inflater = LayoutInflater.from(mContext);
			View view = inflater.inflate(R.layout.custom_toast_error_layout,
					null);
			toast.setView(view);
			TextView txtDes = (TextView) view
					.findViewById(R.id.custom_toast_text_des);
			txtDes.setText(des);
			toast.setDuration(Toast.LENGTH_SHORT);
			// DebugLog.i(LOGTAG, "toast.getDuration():"+toast.getDuration());
			toast.show();
		} catch (Exception e) {

		}
	}
}
