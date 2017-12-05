package com.framework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * pattern matcher
 * 
 * @author shaxiaoning
 *
 */
public class PatternMatcher {
	public PatternMatcher() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 验证
	 * 
	 * @param pattern
	 *            规则
	 * @param prefix
	 *            源
	 */
	public static boolean matcherPrefix(String pattern, String inputPrefix) {
		Pattern pat = null;
		try {
			pat = Pattern.compile(pattern);
			Matcher matcher = pat.matcher(inputPrefix);
			return matcher.matches();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return false;
	}

}
