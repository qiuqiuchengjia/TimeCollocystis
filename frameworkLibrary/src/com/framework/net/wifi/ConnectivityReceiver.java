/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.framework.net.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.framework.log.DebugLog;
import com.framework.log.LogUtil;

/**
 * A broadcast receiver to handle the changes in network connectiion states.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class ConnectivityReceiver extends BroadcastReceiver {

	private static final String LOGTAG = LogUtil.makeLogTag(ConnectivityReceiver.class);

	NetConnectListener netConnectListener=null;
	
	public ConnectivityReceiver(NetConnectListener netConnectListener) {
		this.netConnectListener = netConnectListener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		DebugLog.i(LOGTAG, "ConnectivityReceiver.onReceive()...");
		String action = intent.getAction();
		DebugLog.i(LOGTAG, "action=" + action);
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if(action.equals(android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION)){//wifi
			
		}else if(action.equals(android.net.ConnectivityManager.CONNECTIVITY_ACTION)){ //net connect
			if (networkInfo != null) {
				DebugLog.i(LOGTAG, "Network Type  = " + networkInfo.getTypeName());
				DebugLog.i(LOGTAG, "Network State = " + networkInfo.getState());
				if (networkInfo.isConnected()) {//
					DebugLog.i(LOGTAG, "Network connected");
					netConnectListener.connected();
				}
			} else {//
				DebugLog.e(LOGTAG, "Network unavailable");
				netConnectListener.unavailable();
			}
		}
	}

}
