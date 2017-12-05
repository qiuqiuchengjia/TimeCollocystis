package com.framework.net;

import android.util.SparseArray;

/**
 * enum
 * 
 * @see  
 * @author sxn
 *
 */
public enum TaskCacheType {
	/**
	 * read cache
	 */
	READ_CACHE(1), 
	/**
	 * un read cache
	 */
	UN_READ_CACHE(2);

	final int mValue;

	TaskCacheType(int value) {
		mValue = value;
	}

	private static final SparseArray<TaskCacheType> STRING_MAPPING = new SparseArray<TaskCacheType>();

	static {
		for (TaskCacheType via : TaskCacheType.values()) {
			STRING_MAPPING.put(via.mValue, via);
		}
	}

	/**
	 * init Position.fromValue(position)
	 *
	 * @param value
	 * @return
	 */
	public static TaskCacheType fromValue(int value) {
		return STRING_MAPPING.get(value);
	}

}
