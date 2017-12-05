package com.framework.os;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络管理类
 * 
 * @author shaxiaoning
 *
 */
public class NetworkManager {

	public NetworkManager() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 是否有网络
	 * 
	 * @param context
	 * @return
	 */
	public boolean isNetworkAvailable(Context context) {
		if (context == null)
			return false;
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}
		return true;
	}
}
