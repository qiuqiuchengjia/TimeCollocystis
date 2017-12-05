package com.framework.net;

import android.util.SparseArray;

/**
 * enum
 * 
 * @see request type for POST or GET
 * @author sxn
 *
 */
public enum TaskType {
	/**
	 * request method for post
	 */
	POST(1),
	/**
	 * request method for get
	 */
	GET(2),
	/**
	 * request method for post file
	 */
	FILE(3);

	final int mValue;

	TaskType(int value) {
		mValue = value;
	}

	private static final SparseArray<TaskType> STRING_MAPPING = new SparseArray<TaskType>();

	static {
		for (TaskType via : TaskType.values()) {
			STRING_MAPPING.put(via.mValue, via);
		}
	}

	/**
	 * init Position.fromValue(position)
	 *
	 * @param value
	 * @return
	 */
	public static TaskType fromValue(int value) {
		return STRING_MAPPING.get(value);
	}

}
