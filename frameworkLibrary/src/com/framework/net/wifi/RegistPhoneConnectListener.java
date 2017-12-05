package com.framework.net.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.framework.log.DebugLog;
import com.framework.log.LogUtil;

/**
 * 注册侦听
 * 
 * @author sxn
 *
 */
public class RegistPhoneConnectListener {
	private static final String LOGTAG = LogUtil
			.makeLogTag(RegistPhoneConnectListener.class);
	private TelephonyManager telephonyManager;
	private PhoneStateListener phoneStateListener;
	private BroadcastReceiver connectivityReceiver;
	private WifiManager wifiManager;
	private ConnectivityManager connectivityManager;

	public RegistPhoneConnectListener(NetConnectListener netListener) {
		connectivityReceiver = new ConnectivityReceiver(netListener);
		phoneStateListener = new PhoneStateChangeListener(netListener);
	}

	public void create(Context mContext) {
		telephonyManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		wifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		registerConnectivityReceiver(mContext);
	}

	public void destroy(Context mContext) {
		unregisterConnectivityReceiver(mContext);
	}

	private void registerConnectivityReceiver(Context mContext) {
		DebugLog.i(LOGTAG, "registerConnectivityReceiver()...");
		telephonyManager.listen(phoneStateListener,
				PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
		IntentFilter filter = new IntentFilter();
		filter.addAction(android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
		mContext.registerReceiver(connectivityReceiver, filter);
	}

	private void unregisterConnectivityReceiver(Context mContext) {
		DebugLog.i(LOGTAG, "unregisterConnectivityReceiver()...");
		telephonyManager.listen(phoneStateListener,
				PhoneStateListener.LISTEN_NONE);
		mContext.unregisterReceiver(connectivityReceiver);
	}

}
