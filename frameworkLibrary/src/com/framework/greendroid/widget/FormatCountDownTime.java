package com.framework.greendroid.widget;

import com.framework.log.DebugLog;
import com.framework.log.LogUtil;

import android.os.CountDownTimer;

/**
 * 格式格时间
 * 
 * @author shaxiaoning
 *
 */
public class FormatCountDownTime {
	private static final String LOGTAG = LogUtil
			.makeLogTag(FormatCountDownTime.class);
	CountDownTimer countDownTimer = null;

	public FormatCountDownTime() {

	}

	public String formatCountDownTime(final long time) {
		final long currtentTime = time - System.currentTimeMillis() / 1000;
		try {

			String formatTime = null;
			if (currtentTime > 0) {

				formatTime = updateTime(currtentTime);
			} else {
				formatTime = String.format("%ld天%ld时%ld分", 0, 0, 0);
			}
			return formatTime;
		} catch (Exception e) {
			return "";
		}

	}

	private String updateTime(long elapsedSeconds) throws Exception {

		long days = 0;
		long hours = 0;
		long minutes = 0;
		long seconds = 0;

		if (elapsedSeconds >= 86400) {
			days = elapsedSeconds / 86400;
			elapsedSeconds -= days * 86400;
		}
		if (elapsedSeconds >= 3600) {
			hours = elapsedSeconds / 3600;
			elapsedSeconds -= hours * 3600;
		}
		if (elapsedSeconds >= 60) {
			minutes = elapsedSeconds / 60;
			elapsedSeconds -= minutes * 60;
		}
		seconds = elapsedSeconds;
		DebugLog.i(LOGTAG, "days:" + days);
		DebugLog.i(LOGTAG, "hours:" + hours);
		DebugLog.i(LOGTAG, "minutes:" + minutes);
		DebugLog.i(LOGTAG, "seconds:" + seconds);
		String strFormat = String.format("%d天%d时%d分", days, hours, minutes);
		return strFormat;
	}

}
