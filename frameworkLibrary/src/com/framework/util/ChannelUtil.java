package com.framework.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.framework.log.LogUtil;

public class ChannelUtil {
	private static final String LOGTAG=LogUtil.makeLogTag(ChannelUtil.class);
	private static final String CHANNEL_KEY = "Easywedchannel";
	//private static final String CHANNEL_KEY = "BaiduMobAd_CHANNEL";
	private static final String CHANNEL_VERSION_KEY = "channel_version";
	private static String mChannel;
	/**
	 * ?????  ????????""
	 * @param context
	 * @return
	 */
	public static String getChannel(Context context){
		return getChannel(context, "");
	}
	/**
	 * ?????  ????????defaultChannel
	 * @param context
	 * @param defaultChannel
	 * @return
	 */
	public static String getChannel(Context context, String defaultChannel) {
		//?????
		if(!TextUtils.isEmpty(mChannel)){
			return mChannel;
		}
		//sp???
		mChannel = getChannelBySharedPreferences(context);
		if(!TextUtils.isEmpty(mChannel)){
			return mChannel;
		}
		//?apk???
		mChannel = getChannelFromApk(context, CHANNEL_KEY);
		if(!TextUtils.isEmpty(mChannel)){
			//??sp???
			saveChannelBySharedPreferences(context, mChannel);
			return mChannel;
		}
		//System.out.println("mChannel:"+mCProgressWheels_circlePaddinghannel);
		//??????
		return defaultChannel;
    }
	/**
	 * ?apk???????
	 * @param context
	 * @param channelKey
	 * @return
	 */
	private static String getChannelFromApk(Context context, String channelKey) {
		//?apk????
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        //????meta-inf/?? ?????????
        String key = "META-INF/" + channelKey;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith(key)) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String[] split = ret.split("_");
        String channel = "";
        if (split != null && split.length >= 2) {
        	channel = ret.substring(split[0].length() + 1);
        }
//        DebugLog.i(LOGTAG, "channel:"+channel);
       // System.out.println("channel:"+channel);
        return channel;
	}
	/**
	 * ????channel & ?????
	 * @param context
	 * @param channel
	 */
	private static void saveChannelBySharedPreferences(Context context, String channel){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString(CHANNEL_KEY, channel);
		editor.putInt(CHANNEL_VERSION_KEY, getVersionCode(context));
		editor.commit();
	}
	/**
	 * ?sp???channel
	 * @param context
	 * @return ?????????sp????????sp?????
	 */
	private static String getChannelBySharedPreferences(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		int currentVersionCode = getVersionCode(context);
		if(currentVersionCode == -1){
			//????
			return "";
		}
		int versionCodeSaved = sp.getInt(CHANNEL_VERSION_KEY, -1);
		if(versionCodeSaved == -1){
			//???????channel??????
			//?????  ?? ?????????
			return "";
		}
		if(currentVersionCode != versionCodeSaved){
			return "";
		}
		return sp.getString(CHANNEL_KEY, "");
	}
	/**
	 * ??????????
	 * @param context
	 * @return
	 */
	private static int getVersionCode(Context context){
		try{
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		}catch(NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}
}
