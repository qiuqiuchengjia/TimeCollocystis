package com.framework.os;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.framework.log.DebugLog;
import com.framework.log.LogUtil;

/**
 * 应用信息
 *
 * @author sxn
 */
public class ApplicationInfo {
    private static final String LOGTAG = LogUtil
            .makeLogTag(ApplicationInfo.class);

    /**
     * 获取应用版本
     *
     * @param mContext
     * @return
     */
    public static String getVersionName(Context mContext) {
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packInfo;
        String version = null;
        try {
            packInfo = packageManager.getPackageInfo(mContext.getPackageName(),
                    0);
            version = packInfo.versionName;
        } catch (NameNotFoundException e) {
            version = "1.0.0";
        }
        return version;
    }

    /**
     * 获取应用版本
     *
     * @param mContext
     * @return
     */
    public static int getVerCode(Context mContext) {
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packInfo;
        int version = 0;
        try {
            packInfo = packageManager.getPackageInfo(mContext.getPackageName(),
                    0);
            version = packInfo.versionCode;
        } catch (NameNotFoundException e) {
            version = 0;
        }
        return version;
    }

    /**
     * 获取手机分辨率
     *
     * @return　DisplayMetrics
     */
    public static int[] getDisplayMetrics(Context mContext) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        int[] dp = {dm.widthPixels, dm.heightPixels};
        return dp;
    }

    /**
     * 获取屏幕宽和高
     */
    public static String[] getMetrics(Context mContext) {
        DisplayMetrics displayMertrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(displayMertrics);
        int widthPixels = displayMertrics.widthPixels;
        int heightPixels = displayMertrics.heightPixels;
        float density = displayMertrics.density;
        int densityDpi = displayMertrics.densityDpi;

        return new String[]{widthPixels + "", heightPixels + "",
                density + "", densityDpi + ""};
    }

    /**
     * @param mContext
     * @return
     */
    public static float getMetricsDensity(Context mContext) {
        DisplayMetrics displayMertrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(displayMertrics);
        //int widthPixels = displayMertrics.widthPixels;
        //int heightPixels = displayMertrics.heightPixels;
        float density = displayMertrics.density;
        //int densityDpi = displayMertrics.densityDpi;

        return density;
    }

    /**
     * 获取屏幕宽和高
     */
    public static float[] getMetricsDisplay(Context mContext) {
        DisplayMetrics displayMertrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(displayMertrics);
        int widthPixels = displayMertrics.widthPixels;
        int heightPixels = displayMertrics.heightPixels;
        float density = displayMertrics.density;
        int densityDpi = displayMertrics.densityDpi;

        return new float[]{widthPixels, heightPixels, density, densityDpi};
    }

    /**
     * 打印设备信息
     *
     * @param mActivity
     */
    public static void debugMetrics(Activity mActivity) {
        String[] metr = getMetrics(mActivity);
        for (String m : metr) {
            DebugLog.i(LOGTAG, "metrics:" + m);

        }
    }

    /**
     * 获得设备deviceId
     *
     * @param mContext
     * @return
     */
    public static String getDeviceId(Context mContext) {
        String deviceId = ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        return deviceId;
    }

    /**
     * 获取toolbar 的高度
     *
     * @param mContext
     * @return
     */
    public static int getActionBarSize(Context mContext) {
        // Activity activity = ;
        if (mContext == null) {
            return 0;
        }
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{android.R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = mContext.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    public static int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.top) + 2;
    }
}
