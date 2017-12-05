package com.framework.os;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class AppExit {
	public AppExit() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 在应用要退出界面的onkeyDown事件中回调
	 * 
	 * @param mContext
	 * @param parentView
	 * @param des
	 *            "再按一次退出"
	 */
	public static boolean onkeyDown(final Context mContext, View parentView,
			String des) {
		return SystemAnimation.systemExit(mContext, parentView, des,
				new AnimationListener() {

					@Override
					public void onAnimationStart(Animation anima) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation anima) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation anima) {
						// DebugLog.d(LOGTAG, "onAnimationEnd。。。。");
						((Activity) mContext).finish();

					}
				});
	}

	/**
	 * 在退出页面的onDestroy方法中回调
	 */
	public static void onDestroy() {
		ActivityStack.getActivityManage().removeAllActivity();
		System.gc();
		Runtime.getRuntime().gc();
		System.exit(0);
	}

}
