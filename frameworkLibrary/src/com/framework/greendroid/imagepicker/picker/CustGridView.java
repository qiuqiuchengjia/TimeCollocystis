/*
 * Copyright 2014 Habzy Huang
 */
package com.framework.greendroid.imagepicker.picker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class CustGridView extends GridView {

    private boolean isScrollable = true;
    private boolean isItemClick = true;

    public CustGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustGridView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isScrollable) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int expandSpec =
                    MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        }
    }

    public void setSrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }

    public void setItemClick(boolean itemClick) {
        isItemClick = itemClick;
    }
    //    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//                MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, expandSpec);
//    }

    // 通过重新dispatchTouchEvent方法来禁止滑动
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (!isScrollable) {
                return false;
            }
        }
        if (isItemClick) {
            return super.dispatchTouchEvent(ev);
        } else {
            return false;
        }
    }

}
