package com.framework.os;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;

import com.framework.greendroid.widget.CustomToast;
import com.framework.log.LogUtil;

/**
 *      在application onCreate中加入以下代码进行注册
 *		SystemProcessUncaughtExcep uncaughtExcep = new SystemProcessUncaughtExcep(
 *				this, LoaddingActivity.class);
 *		Thread.setDefaultUncaughtExceptionHandler(uncaughtExcep);
 */
/**
 * 系统进程异常
 * 
 * @author sxn
 * 
 */
public class SystemProcessUncaughtExcep implements UncaughtExceptionHandler {
	private static final String LOGTAG = LogUtil
			.makeLogTag(SystemProcessUncaughtExcep.class);
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private Application application;
	private Class<?> cl;

	/**
	 * 
	 * @param application
	 *            application
	 * @param cl
	 *            要启动的activity
	 * 
	 */
	public SystemProcessUncaughtExcep(Application application, Class<?> cl) {
		this.application = application;
		this.cl = cl;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
			
		} else {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			
			}
			Intent intent = new Intent(application.getApplicationContext(), cl);
			PendingIntent restartIntent = PendingIntent.getActivity(
					application.getApplicationContext(), 0, intent,
					Intent.FLAG_ACTIVITY_NEW_TASK);
			// 退出程序
			AlarmManager mgr = (AlarmManager) application
					.getSystemService(Context.ALARM_SERVICE);
//			mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000,
//					restartIntent); // 3秒钟后重启应用
			ActivityStack.getActivityManage().removeAllActivity();
			android.os.Process.killProcess(android.os.Process.myPid());
		}

	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				CustomToast.showCustomErrorToast(
						application.getApplicationContext(), "程序开小差了");
				Looper.loop();
			}
		}.start();
		return true;
	}
}
