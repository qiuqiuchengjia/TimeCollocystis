package com.framework.util;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

/**
 * 自动获取短信验证码以及短信1分钟后可重发
 * 
 *
 * 
 */
public class AutoGainVerifyCodeUtils {
	private BroadcastReceiver smsReceiver;
	private IntentFilter filter2;
	private Handler handler;
	private String strContent;
	private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";
	private Handler btnHandler;
	private Timer timer;

	public AutoGainVerifyCodeUtils() {

	}

	/**
	 * 自动获取短信验证码
	 * 
	 * @param context
	 * @param editText
	 */
	public void gainVerifyCode(Context context, final EditText editText) {
		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				editText.setText(strContent);
			};
		};
		filter2 = new IntentFilter();
		filter2.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter2.setPriority(Integer.MAX_VALUE);
		smsReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Object[] objs = (Object[]) intent.getExtras().get("pdus");
				for (Object obj : objs) {
					byte[] pdu = (byte[]) obj;
					SmsMessage sms = SmsMessage.createFromPdu(pdu);
					// 短信的内容
					String message = sms.getMessageBody();
					// String message = new String(sms.getMessageBody(),
					// "UTF-8");
					Log.d("logo", "message     " + message);
					// 短息的手机号。。+86开头？
					String from = sms.getOriginatingAddress();
					Log.d("logo", "from     " + from);
					// Time time = new Time();
					// time.set(sms.getTimestampMillis());
					// String time2 = time.format3339(true);
					// Log.d("logo", from + "   " + message + "  " + time2);
					// strContent = from + "   " + message;
					// handler.sendEmptyMessage(1);
					if (!TextUtils.isEmpty(from)) {
						String code = patternCode(message);
						if (!TextUtils.isEmpty(code)) {
							strContent = code;
							handler.sendEmptyMessage(1);
						}
					}
				}
			}
		};
		context.registerReceiver(smsReceiver, filter2);
	}

	/**
	 * 短信1分钟后可重发 Handler处理
	 * 
	 * @param btnVerificationCode
	 * @param context
	 */
	public void resendVerifyCodeHandler(final Button btnVerificationCode,
			final Context context) {
		btnHandler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what > 0) {
//					btnVerificationCode
//							.setBackgroundColor(context.getResources()
//									.getColor(R.color.light_gray_btn_bg));
					btnVerificationCode.setText(msg.what + "秒后可重发");
				} else {
					btnVerificationCode.setEnabled(true);
//					btnVerificationCode.setBackgroundColor(context
//							.getResources().getColor(
//									R.color.gray_edit_hint_color));
//					btnVerificationCode
//							.setText(R.string.string_btn_text_verification_code_again);

					timer.cancel();
				}
			};
		};

	}

	public void resendVerifyCodeTimer(Button btnVerificationCode) {
		// 短信1分钟后可重发
		btnVerificationCode.setEnabled(false);
		timer = new Timer();
		timer.schedule(new TimerTask() {
			// 自己修改成需要的时间即可
			int time = 60;

			public void run() {
				Message msg = new Message();
				msg.what = time--;
				btnHandler.sendMessage(msg);
			}
		}, 100, 1000);
	}

	public BroadcastReceiver returnBroadcastReceiver() {
		return smsReceiver;
	}

	/**
	 * 匹配短信中间的6个数字（验证码等）
	 * 
	 * @param patternContent
	 * @return
	 */
	private String patternCode(String patternContent) {
		if (TextUtils.isEmpty(patternContent)) {
			return null;
		} else if (patternContent.contains("车之宝")) {
			Pattern p = Pattern.compile(patternCoder);
			Matcher matcher = p.matcher(patternContent);
			if (matcher.find()) {
				return matcher.group();
			}
		}

		return null;
	}
}
