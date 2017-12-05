package com.framework.util;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;

import com.framework.R;

/**
 * 工具
 * 
 * @author shaxiaoning
 *
 */
public class UtilTools {
	public UtilTools() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 设置EditText hint text size
	 * 
	 * @param mContext
	 * @param hintText
	 * @return
	 */
	public static SpannedString setHintSize(Context mContext, String hintText) {
		SpannableString ss = new SpannableString(hintText);

		AbsoluteSizeSpan ass = new AbsoluteSizeSpan((int) mContext
				.getResources().getDimension(R.dimen.global_hint_text_size),
				true);
		ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return new SpannedString(ss);
	}

	/**
	 * 锁定view
	 * 
	 * @param isLock
	 *            true 为锁定 false 为解锁
	 * @param view
	 */
	public static void lockView(boolean isLock, View... views) {
		if (isLock) {// 锁定view
			for (View v : views) {
				v.setEnabled(false);
			}
		} else {// 解锁
			for (View v : views) {
				v.setEnabled(true);
			}
		}
		

	}

}
