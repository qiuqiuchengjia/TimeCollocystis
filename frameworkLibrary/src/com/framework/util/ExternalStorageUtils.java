package com.framework.util;

import android.os.Environment;

import com.framework.log.DebugLog;
import com.framework.log.LogUtil;

import java.io.File;

/**
 * Environment .getExternalStorageDirectory()
 * 
 * @author shaxiaoning
 *
 */
public class ExternalStorageUtils {
	private static final String LOGTAG = LogUtil
			.makeLogTag(ExternalStorageUtils.class);
	private static String locationPath = null;

	// private String namespace = null;

	public ExternalStorageUtils(String namespace, String directory) {
		init(namespace, directory);
	}

	private void init(String namespace, String directory) {
		String path = null;
		// if (Environment.getExternalStorageState().equals(
		// Environment.MEDIA_MOUNTED)) { // mnt/sdcard
		path = Environment.getExternalStorageDirectory().getAbsolutePath();
		DebugLog.i(LOGTAG, "ExternalStorageDirectory-path:" + path);
		String tmpPath = path + File.separator + namespace + File.separator
				+ directory;

		File file = new File(tmpPath);
		if (!file.exists())
			file.mkdirs();
		locationPath = tmpPath + File.separator;
		DebugLog.i(LOGTAG, "tmpPath:" + tmpPath);
		// }

	}

	/**
	 * 
	 * @return
	 */
	public static String envExternalStorageDirectory() {
		return locationPath != null ? locationPath : null;
	}

	/**
	 * 获取一个路径，如果不存在则新建
	 * @param dir
	 * @return
	 */
	public static String getDir(String dir) {
		File workDir = new File(envExternalStorageDirectory());

		if (dir.length() == 0) {
			return workDir.getPath() + "/";
		} else {
			File newDir = new File(workDir, dir);
			if (!newDir.exists()) {
				newDir.mkdirs();
			}
			return newDir.getPath() + "/";
		}
	}
}
