package com.framework.greendroid.widget;

import com.framework.greendroid.widget.BounceScroller.State;

public interface BounceListener {

	public void onState(boolean header, State state);

	public void onOffset(boolean header, int offset);

}
