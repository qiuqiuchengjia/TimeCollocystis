/*
 *  <net.thepaksoft.fdtrainer.NestedListView
                android:id="@+id/crewList"
                android:layout_width="0dip"
                android:layout_height="wrap_content"
                android:layout_marginBottom="2dp"
                android:layout_weight="1"
                android:background="@drawable/round_shape"
                android:cacheColorHint="#00000000" >
            </net.thepaksoft.fdtrainer.NestedListView>
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.framework.greendroid.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * listview can inside a Scrollview
 * 
 * @author sxn
 * 
 */
public class NestedListView extends ListView implements OnTouchListener,
		OnScrollListener {

	private int listViewTouchAction;
	private static final int MAXIMUM_LIST_ITEMS_VIEWABLE = 99;

	public NestedListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		listViewTouchAction = -1;
		setOnScrollListener(this);
		setOnTouchListener(this);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (getAdapter() != null
				&& getAdapter().getCount() > MAXIMUM_LIST_ITEMS_VIEWABLE) {
			if (listViewTouchAction == MotionEvent.ACTION_MOVE) {
				scrollBy(0, -1);
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int newHeight = 0;
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {
			ListAdapter listAdapter = getAdapter();
			if (listAdapter != null && !listAdapter.isEmpty()) {
				int listPosition = 0;
				for (listPosition = 0; listPosition < listAdapter.getCount()
						&& listPosition < MAXIMUM_LIST_ITEMS_VIEWABLE; listPosition++) {
					View listItem = listAdapter.getView(listPosition, null,
							this);
					// now it will not throw a NPE if listItem is a ViewGroup
					// instance
					if (listItem instanceof ViewGroup) {
						listItem.setLayoutParams(new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT));
					}
					listItem.measure(widthMeasureSpec, heightMeasureSpec);
					newHeight += listItem.getMeasuredHeight();
				}
				newHeight += getDividerHeight() * listPosition;
			}
			if ((heightMode == MeasureSpec.AT_MOST) && (newHeight > heightSize)) {
				if (newHeight > heightSize) {
					newHeight = heightSize;
				}
			}
		} else {
			newHeight = getMeasuredHeight();
		}
		setMeasuredDimension(getMeasuredWidth(), newHeight);
	}

	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (getAdapter() != null
				&& getAdapter().getCount() > MAXIMUM_LIST_ITEMS_VIEWABLE) {
			if (listViewTouchAction == MotionEvent.ACTION_MOVE) {
				scrollBy(0, 1);
			}
		}
		int action = event.getAction();
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            // Disallow ScrollView to intercept touch events.
            v.getParent().requestDisallowInterceptTouchEvent(true);
            break;

        case MotionEvent.ACTION_UP:
            // Allow ScrollView to intercept touch events.
            v.getParent().requestDisallowInterceptTouchEvent(false);
            break;
        }

        // Handle ListView touch events.
        v.onTouchEvent(event);
        return true;
		//return false;
	}

	private void setListViewScrollable(final ListView list) {
		list.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				listViewTouchAction = event.getAction();
				if (listViewTouchAction == MotionEvent.ACTION_MOVE) {
					list.scrollBy(0, 1);
				}
				return false;
			}
		});
		list.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (listViewTouchAction == MotionEvent.ACTION_MOVE) {
					list.scrollBy(0, -1);
				}
			}
		});
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		requestDisallowInterceptTouchEvent(true);
		return super.onInterceptTouchEvent(ev);
	}
	public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
      //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
    }

}
