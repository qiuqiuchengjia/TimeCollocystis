package com.framework.net.gson;

import com.google.gson.Gson;
/**
 * GSON
 * @author sxn
 *
 * @param <T>
 */
public abstract class GsonParseImpl<T> implements GsonParse<T>{
	private Class<T> classBean;
	public GsonParseImpl(Class<T> tCalss) {
		this.classBean=tCalss;
	}

	@Override
	public T onParse(String responseText) throws Exception{
		Gson gson = new Gson();
		T bean = (T) gson.fromJson(responseText, classBean);
		return bean;
	}
	
	
}
