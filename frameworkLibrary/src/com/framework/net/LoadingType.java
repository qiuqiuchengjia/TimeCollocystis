package com.framework.net;

import android.util.SparseArray;

/**
 * enum loading progress
 * 
 * @author sxn
 *
 */
public enum LoadingType {
	/**
	 * show loading dialog
	 */
	SHOW(1),
	/**
	 * un show loading dialog
	 */
	UNSHOW(2);
	private int mValue;

	LoadingType(int mValue) {
		this.mValue = mValue;

	}

	private static final SparseArray<LoadingType> STRING_MAPPING = new SparseArray<LoadingType>();

	static {
		for (LoadingType via : LoadingType.values()) {
			STRING_MAPPING.put(via.mValue, via);
		}
	}

	/**
	 * init LoadingType.fromValue(position)
	 *
	 * @param value
	 * @return
	 */
	public static LoadingType fromValue(int value) {
		return STRING_MAPPING.get(value);
	}

}
