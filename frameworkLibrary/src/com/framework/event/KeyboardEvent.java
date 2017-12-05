package com.framework.event;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 自定义键盘事件
 *
 * @author shaxiaoning
 */
public class KeyboardEvent {

    private KeyboardEvent() {

    }

    /**
     * 隐藏键盘
     *
     * @param mContext
     */
    public static boolean hideSoftInput(Context mContext, View view) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return false;

    }

    /**
     * 显示键盘
     *
     * @param mContext
     */
    public static boolean showSoftInput(Context mContext, View view) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.showSoftInputFromInputMethod(((Activity)
        // mContext).getCurrentFocus()
        // .getWindowToken(), 0);
        return imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static boolean softInputIsActivite(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }
}
