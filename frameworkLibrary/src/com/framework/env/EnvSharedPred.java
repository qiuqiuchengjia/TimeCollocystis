package com.framework.env;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences
 * 
 * @author sxn
 * 
 */
public class EnvSharedPred {

	/**
	 * 保存数据
	 * 
	 * @param mContext
	 * @param namespace
	 * @param IsharedPraredattr
	 * @see new EnvSharedPred(LoginActivity.this,
	 *      KMConstants.ENV_SHAREDPREFERENCES_NAMESPACE_USERINFO, new
	 *      ISharedPredAttribute() {
	 * @Override public void addAttribute(Editor editor) {
	 *           editor.putString(KMConstants.ENV_SHAREDPREF_USERID,
	 *           bean.getUserId());
	 *           editor.putString(KMConstants.ENV_SHAREDPREF_TMPKEY,
	 *           bean.getSkey()); }, new ISharedPredGetAttribute(){
	 * @Override public void getAttrBute(SharedPreferences sp){
	 *           sp.getString(key,"defaut");
	 * 
	 *           }, new ISharedPredRemoveAttribute(){
	 * @Override public void removeAttribute(Editor editor){
	 * 
	 * 
	 *           }
	 * 
	 *           } } });
	 */
	public EnvSharedPred(Context mContext, String namespace,
			ISharedPredAttribute sharedPraredattr,
			ISharedPredGetAttribute sharedPraredGetattr,
			ISharedPredRemoveAttribute sharedPraredRmattr) {
		SharedPreferences sp = mContext.getSharedPreferences(namespace, 0);
		Editor editor = sp.edit();
		if (sharedPraredattr != null)
			sharedPraredattr.addAttribute(editor);
		if (sharedPraredRmattr != null)
			sharedPraredRmattr.removeAttribute(editor);
		editor.commit();
		if (sharedPraredGetattr != null)
			sharedPraredGetattr.getAttrBute(sp);
	}

	/**
	 * 添加
	 * @param mContext
	 * @param namespace
	 * @param sharedPraredattr
	 */
	public EnvSharedPred(Context mContext, String namespace,
			ISharedPredAttribute sharedPraredattr) {
		SharedPreferences sp = mContext.getSharedPreferences(namespace, 0);
		Editor editor = sp.edit();
		if (sharedPraredattr != null)
			sharedPraredattr.addAttribute(editor);
		editor.commit();
	}
	/**
	 * 读取
	 * @param mContext
	 * @param namespace
	 * @param sharedPraredGetattr
	 */
	public EnvSharedPred(Context mContext, String namespace,
			ISharedPredGetAttribute sharedPraredGetattr) {
		SharedPreferences sp = mContext.getSharedPreferences(namespace, 0);
		if (sharedPraredGetattr != null)
			sharedPraredGetattr.getAttrBute(sp);
	}
	/**
	 * 刪除
	 * @param mContext
	 * @param namespace
	 * @param sharedPraredRmattr
	 */
	public EnvSharedPred(Context mContext, String namespace,
			ISharedPredRemoveAttribute sharedPraredRmattr) {
		SharedPreferences sp = mContext.getSharedPreferences(namespace, 0);
		Editor editor = sp.edit();
		if (sharedPraredRmattr != null)
			sharedPraredRmattr.removeAttribute(editor);
		editor.commit();
	}
	
	

	/**
	 * 添加节点
	 * 
	 * @author sxn
	 * 
	 */
	public interface ISharedPredAttribute {
		/**
		 * 添加值
		 * 
		 * @param attrkey
		 * @param attrValues
		 */
		public void addAttribute(Editor editor);

	}

	/**
	 * 删除节点
	 * 
	 * @author sxn
	 * 
	 */
	public interface ISharedPredRemoveAttribute {
		/**
		 * 去除值
		 * 
		 * @param attrKey
		 */
		public void removeAttribute(Editor editor);
	}

	/**
	 * 获得节点
	 * 
	 * @author sxn
	 * 
	 */
	public interface ISharedPredGetAttribute {
		/**
		 * 取值
		 * 
		 * @param sp
		 */
		public void getAttrBute(SharedPreferences sp);
	}

}
