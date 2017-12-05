package com.framework.util;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 获取字符 串长度
 * 
 * @author shaxiaoning
 *
 */
public class TextUtils {

	public InputFilter[] lengthFilter(final int max_length) {
		InputFilter[] filters = new InputFilter[1];
		InputFilter filter = new InputFilter.LengthFilter(max_length) {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				int destLen = getCharacterNum(dest.toString()); // 获取字符个数(一个中文算2个字符)
				int sourceLen = getCharacterNum(source.toString());
				if (destLen + sourceLen > max_length) {

					return "";
				}
				return source;
			}
		};
		filters[0] = filter;

		return filters;
	}

	/**
	 * 
	 * @param minLength
	 * @param source
	 * @return
	 */
	public boolean lengthFilter(int minLength, String source) {

		// int destLen = getCharacterNum(dest.toString()); // 获取字符个数(一个中文算2个字符)
		int sourceLen = getCharacterNum(source.toString());
		if (sourceLen > minLength) {
			return true;
		}
		return false;
	}

	/**
	 * @description 获取一段字符串的字符个数（包含中英文，一个中文算2个字符）
	 * @param content
	 * @return
	 */
	private int getCharacterNum(final String content) {
		if (null == content || "".equals(content)) {
			return 0;
		} else {
			return (content.length() + getChineseNum(content));
		}
	}

	/**
	 * @description 返回字符串里中文字或者全角字符的个数
	 * @param s
	 * @return
	 */
	private int getChineseNum(String s) {

		int num = 0;
		char[] myChar = s.toCharArray();
		for (int i = 0; i < myChar.length; i++) {
			if ((char) (byte) myChar[i] != myChar[i]) {
				num++;
			}
		}
		return num;
	}

}
