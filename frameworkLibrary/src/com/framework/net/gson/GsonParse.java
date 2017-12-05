package com.framework.net.gson;
/**
 * GSON parse
 * @author sxn
 *
 */
public interface GsonParse<T> {
	/**
	 * 
	 * @param responseText
	 * @return bean
	 */
	public T onParse(String responseText)throws Exception;

}
