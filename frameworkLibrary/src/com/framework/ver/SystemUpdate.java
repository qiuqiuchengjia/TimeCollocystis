package com.framework.ver;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;

public class SystemUpdate {
	public SystemUpdate() {
		// TODO Auto-generated constructor stub
	}

	public void launch(Context mContext) {
		// Intent intent = new Intent(Intent.ACTION_VIEW);
		// intent.setDataAndType(Uri.fromFile(new File(Environment
		// .getExternalStorageDirectory(), Config.UPDATE_SAVENAME)),
		// "application/vnd.android.package-archive");
		// mContext.startActivity(intent);
	}

	/**
	 * Show Notification
	 * 
	 * @param info
	 * 
	 * @see Notification#show(android.content.Context, int)
	 */
	public static void showNotification(Context mContext, String content, String apkUrl) {
		android.app.Notification noti;
		Intent myIntent = new Intent(mContext, DownloadService.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		myIntent.putExtra(Constants.APK_DOWNLOAD_URL, apkUrl);
		mContext.startService(myIntent);
//		PendingIntent pendingIntent = PendingIntent.getService(mContext, 0,
//				myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//		int smallIcon = mContext.getApplicationInfo().icon;
//		noti = new NotificationCompat.Builder(mContext)
//				.setTicker(mContext.getString(R.string.newUpdateAvailable))
//				.setContentTitle(
//						mContext.getString(R.string.newUpdateAvailable))
//				.setContentText(content).setSmallIcon(smallIcon)
//				.setContentIntent(pendingIntent).build();
//
//		noti.flags = android.app.Notification.FLAG_AUTO_CANCEL;
//		NotificationManager notificationManager = (NotificationManager) mContext
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//		notificationManager.notify(0, noti);
	}
}
