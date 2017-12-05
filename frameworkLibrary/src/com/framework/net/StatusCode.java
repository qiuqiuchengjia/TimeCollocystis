package com.framework.net;

import android.util.SparseArray;

/**
 * 
 * @author shaxiaoning
 *
 */
public class StatusCode {
	private final static int SUCCESS = 200; // 成功
	private final static int FAILURE = 0;
	private final static int FORBIDDEN = 403; // 服务器已经理解请求，但是拒绝执行它。
	private final static int NOTFOUND = 404; // 请求失败，请求所希望得到的资源未被在服务器上发现
	private final static int NOTALLOW = 405;
	private final static int SERVERERROR = 500; // 服务器遇到了一个未曾预料的状况，导致了它无法完成对请求的处理
	private final static int SERVICEUNAVAILABLE = 503; // 由于临时的服务器维护或者过载，服务器当前无法处理请求。
	private final static int GATEWAYTIMEOUT = 504;// 请求超时

	public StatusCode() {
	}

	public static String getSuccess(int statusCode) {
		switch (statusCode) {
		case SUCCESS:
			return "请求成功";
		case FAILURE:
			// return "无法连接到服务器，请检查网络是否可用(errorCode:" + statusCode+")";
			return "您的网络开小差了";
		case FORBIDDEN:
			// return "服务器拒绝访问(errorCode:" + statusCode+")";
			return "服务器拒绝访问";
		case NOTALLOW:// get or post
			return "不允许使用该方法";
		case NOTFOUND:
			// return "请求找不到(errorCode:" + statusCode+")";
			return "请求找不到";
		case SERVERERROR:
			// return "服务器内部错误(errorCode:" + statusCode+")";
			return "服务器内部错误";
		case SERVICEUNAVAILABLE:
			// return "服务不可用，请稍后重试(errorCode:" + statusCode+")";
			return "服务不可用，请稍后重试";
		case GATEWAYTIMEOUT:
			// return "请求超时，请稍后重试(errorCode:" + statusCode+")";
			return "请求超时，请稍后重试";
		default:
			break;
		}
		return "";
	}

}
