package com.framework.os;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * activity  基类
 * 
 * @author shaxiaoning
 *
 */
public abstract class AbstractBaseActivity extends Activity {
	/**
	 * setContentView
	 */
	protected abstract void setContentView();

	/**
	 * 实例化控件
	 */
	protected abstract void initView();

	/**
	 * 实例化数据
	 */
	protected abstract void initData();

	/**
	 * 析构
	 */
	protected abstract void dealloc();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityStack.getActivityManage().addActivity(this);
		setContentView();
		initView();
		initData();
	}

	/**
	 * 隐藏键盘事件
	 */
	protected OnTouchListener ontouch = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			//KeyboardEvent.hideSoftInput(v.getContext());
			return false;
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		//StatService.onResume(this);
	};

	@Override
	protected void onPause() {
		super.onPause();
		/**
		 * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		//StatService.onPause(this);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityStack.getActivityManage().removeActivity(this);
		dealloc();
	}
}
