package com.framework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.framework.log.DebugLog;
import com.framework.log.LogUtil;

/**
 * read message code
 * 
 * @author shaxiaoning
 *
 */
public class CurrorReadVerifyCode {
	private static final String LOGTAG = LogUtil
			.makeLogTag(CurrorReadVerifyCode.class);
	private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";
	private MyBroadcastReceiver myReceiver = null;
	private OnMessageLoader onMessageLoader = null;
	private String pattern;

	public CurrorReadVerifyCode(String pattern, OnMessageLoader onMessageLoader) {
		this.pattern = pattern;
		this.onMessageLoader = onMessageLoader;

	}

	public void regiestReceiver(Context mContext) {
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		myReceiver = new MyBroadcastReceiver();
		mContext.registerReceiver(myReceiver, filter);
	}

	public void unRegiestreceiver(Context mContext) {
		if (myReceiver != null) {
			mContext.unregisterReceiver(myReceiver);
			myReceiver = null;
		}
	}

	/**
	 * 
	 * @author shaxiaoning
	 *
	 */
	class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {
				byte[] pdu = (byte[]) obj;
				SmsMessage sms = SmsMessage.createFromPdu(pdu);

				String message = sms.getMessageBody();
				DebugLog.d(LOGTAG, "message:" + message);

				String from = sms.getOriginatingAddress();
				DebugLog.d(LOGTAG, "from:" + from);

				if (!TextUtils.isEmpty(from)) {
					String code = patternCode(message, pattern);
					if (!TextUtils.isEmpty(code)) {
						onMessageLoader.onMessage(code);
					}
				}
			}
		}

	}

	/**
	 * 匹配短信中间的6个数字（验证码等）
	 * 
	 * @param patternContent
	 * @param pattern
	 *            正则匹配
	 * @return
	 */
	private String patternCode(String patternContent, String pattern) {
		if (TextUtils.isEmpty(patternContent)) {
			return null;
		} else if (patternContent.contains(pattern)) {
			Pattern p = Pattern.compile(patternCoder);
			Matcher matcher = p.matcher(patternContent);
			if (matcher.find()) {
				return matcher.group();
			}
		}

		return null;
	}

	/**
	 * 
	 * @author shaxiaoning
	 *
	 */

	public interface OnMessageLoader {
		/**
		 * 短信码
		 * 
		 * @param code
		 */
		public void onMessage(String code);

		// public void onRetry();
	}
}
