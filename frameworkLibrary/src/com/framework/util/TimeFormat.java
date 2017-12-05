package com.framework.util;
import java.util.Formatter;
import java.util.IllegalFormatException;
import java.util.Locale;
/**
 * 格式化时间
 * @author sxn
 *
 */
public class TimeFormat {
	private String mFormat;
	private Formatter mFormatter;
	private Locale mFormatterLocale;
	private Object[] mFormatterArgs = new Object[1];
	private StringBuilder mFormatBuilder;
	private long mBase;
	private static final String FAST_FORMAT_DHHMMSS = "%1$02d:%2$02d:%3$02d:%4$02d";
	private static final String FAST_FORMAT_HMMSS = "%1$02d:%2$02d:%3$02d";
	private static final String FAST_FORMAT_MMSS = "%1$02d:%2$02d";
	// private static final String FAST_FORMAT_MMSS_C = "%1$02d:%2$02d";
	private static final char TIME_PADDING = '0';
	private static final char TIME_SEPARATOR = ':';
	private StringBuilder mRecycle = new StringBuilder(8);

	private String mChronoFormat;

	public TimeFormat() {

	}

	/**
	 * 
	 * @param time 秒
	 * @return
	 * 
	 * @see String time=format.setBase(System.currentTimeMillis()+30*1000);
	 */
	public String setBase(long time) {
		mBase = time;
		return updateText(System.currentTimeMillis());
	}

	private synchronized String updateText(long now) {
		long seconds = mBase - now;
		seconds /= 1000;
		boolean stillRunning = true;
		if (seconds <= 0) {
			stillRunning = false;
			seconds = 0;
		}
		String text = formatRemainingTime(mRecycle, seconds);
		if (mFormat != null) {
			Locale loc = Locale.getDefault();
			if (mFormatter == null || !loc.equals(mFormatterLocale)) {
				mFormatterLocale = loc;
				mFormatter = new Formatter(mFormatBuilder, loc);
			}
			mFormatBuilder.setLength(0);
			mFormatterArgs[0] = text;
			try {
				mFormatter.format(mFormat, mFormatterArgs);
				text = mFormatBuilder.toString();
			} catch (IllegalFormatException ex) {

			}
		}

		return text;
	}

	private String formatRemainingTime(StringBuilder recycle,
			long elapsedSeconds) {

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

		if (mChronoFormat != null) {
			return formatRemainingTime(recycle, mChronoFormat, days, hours,
					minutes, seconds);
		} else if (days > 0) {
			return formatRemainingTime(recycle, FAST_FORMAT_DHHMMSS, days,
					hours, minutes, seconds);
		} else if (hours > 0) {
			return formatRemainingTime(recycle, FAST_FORMAT_HMMSS, hours,
					minutes, seconds);
		} else {
			return formatRemainingTime(recycle, FAST_FORMAT_MMSS, minutes,
					seconds);
		}
	}

	private static String formatRemainingTime(StringBuilder recycle,
			String format, long days, long hours, long minutes, long seconds) {
		if (FAST_FORMAT_DHHMMSS.equals(format)) {
			StringBuilder sb = recycle;
			if (sb == null) {
				sb = new StringBuilder(8);
			} else {
				sb.setLength(0);
			}
			sb.append(days);
			sb.append(TIME_SEPARATOR);
			if (hours < 10) {
				sb.append(TIME_PADDING);
			} else {
				sb.append(toDigitChar(hours / 10));
			}
			sb.append(toDigitChar(hours % 10));
			sb.append(TIME_SEPARATOR);
			if (minutes < 10) {
				sb.append(TIME_PADDING);
			} else {
				sb.append(toDigitChar(minutes / 10));
			}
			sb.append(toDigitChar(minutes % 10));
			sb.append(TIME_SEPARATOR);
			if (seconds < 10) {
				sb.append(TIME_PADDING);
			} else {
				sb.append(toDigitChar(seconds / 10));
			}
			sb.append(toDigitChar(seconds % 10));
			return sb.toString();
		} else {
			return String.format(format, days, hours, minutes, seconds);
		}
	}

	private static String formatRemainingTime(StringBuilder recycle,
			String format, long hours, long minutes, long seconds) {
		if (FAST_FORMAT_HMMSS.equals(format)) {
			StringBuilder sb = recycle;
			if (sb == null) {
				sb = new StringBuilder(8);
			} else {
				sb.setLength(0);
			}
			sb.append(hours);
			sb.append(TIME_SEPARATOR);
			if (minutes < 10) {
				sb.append(TIME_PADDING);
			} else {
				sb.append(toDigitChar(minutes / 10));
			}
			sb.append(toDigitChar(minutes % 10));
			sb.append(TIME_SEPARATOR);
			if (seconds < 10) {
				sb.append(TIME_PADDING);
			} else {
				sb.append(toDigitChar(seconds / 10));
			}
			sb.append(toDigitChar(seconds % 10));
			return sb.toString();
		} else {
			return String.format(format, hours, minutes, seconds);
		}
	}

	private static String formatRemainingTime(StringBuilder recycle,
			String format, long minutes, long seconds) {
		// if (FAST_FORMAT_MMSS.equals(format)) {
		if (FAST_FORMAT_MMSS.equals(format)) {
			StringBuilder sb = recycle;
			if (sb == null) {
				sb = new StringBuilder(8);
			} else {
				sb.setLength(0);
			}
			if (minutes < 10) {
				sb.append(TIME_PADDING);
			} else {
				sb.append(toDigitChar(minutes / 10));
			}
			sb.append(toDigitChar(minutes % 10));
			sb.append(TIME_SEPARATOR);
			if (seconds < 10) {
				sb.append(TIME_PADDING);
			} else {
				sb.append(toDigitChar(seconds / 10));
			}
			sb.append(toDigitChar(seconds % 10));
			return sb.toString();
		} else {
			return String.format(format, minutes, seconds);
		}
	}

	private static char toDigitChar(long digit) {
		return (char) (digit + '0');
	}
}
