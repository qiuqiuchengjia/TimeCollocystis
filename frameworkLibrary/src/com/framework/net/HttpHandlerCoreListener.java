package com.framework.net;

/**
 * http handler
 *
 * @author shaxiaoning
 */
public interface HttpHandlerCoreListener<JSON_TYPE> {
    /**
     * 返回成功
     *
     * @param JSON_TYPE entity
     */
    public void onSuccess(JSON_TYPE responseEntity);

    /**
     * 返回失败信息
     *
     * @param errorText
     * @param responseEntity
     */
    public void onFailure(String errorText, JSON_TYPE responseEntity);

    /**
     * 返回token 失效
     * @param errorText
     * @param responseEntity
     */
    void onTokenFailure(String errorText, JSON_TYPE responseEntity);
}
