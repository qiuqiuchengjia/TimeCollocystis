package com.framework.net;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.framework.R;
import com.framework.greendroid.hud.ProgressHUD;
import com.framework.log.DebugLog;
import com.framework.log.LogUtil;
import com.framework.net.core.AsyncHttpClientCore;
import com.framework.net.core.AsyncHttpRequestCore;
import com.framework.net.core.HttpResponseHandlerGsonCore;
import com.framework.net.core.TextResponseHandlerCore;
import com.framework.net.db.controller.HttpCacheController;
import com.framework.net.db.utils.JniApi;
import com.framework.os.ApplicationInfo;
import com.framework.os.AsyncTask;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

/**
 * net post tool create 2015-4-10
 *
 * @param <JSON_TYPE>
 * @author shaxiaoning
 */
public class HttpTaskCore<JSON_TYPE> implements OnCancelListener {
    private static final String LOGTAG = LogUtil.makeLogTag(HttpTaskCore.class);
    private final List<RequestHandle> requestHandles = new LinkedList<RequestHandle>();
    /**
     * Success
     */
    private static int RESPOSE_SUCCESS_CODE = 0;
    /**
     * Failure
     */
    private static int RESPOSE_FAILURE_CODE = 1;
    /**
     * Request method
     */
    private TaskType taskType;
    /**
     * Loading progress
     */
    private LoadingType loadingType;
    /**
     * Reads the local cache
     */
    private TaskCacheType cacheType;

    /**
     * service host
     */
    private String taskUrl = null;
    /**
     * request action
     */
    private String actionParams = null;

    private int defaultTimeOut = 30 * 1000;// 30秒
    private int defaultConnectTimeout = 50 * 1000;
    private boolean defaultEnableRedirects = true;

    private HttpHandlerCoreListener<JSON_TYPE> listener;

    // private Dialog progressDialog; // 联网加载
    private Context mContext;
    private final Class<JSON_TYPE> clazz;
    private ProgressHUD mProgressHUD;

    // private String cacheKey = null;

    public HttpTaskCore(HttpHandlerCoreListener<JSON_TYPE> listener,
                        Class<JSON_TYPE> parClazz) {
        this.listener = listener;
        this.clazz = parClazz;
        this.setTaskType(TaskType.fromValue(1));// default post request
        this.setLoadingType(LoadingType.fromValue(1));// default show progress
        this.setCacheType(TaskCacheType.fromValue(2));// default un Reads the
        // local cache
    }

    public LoadingType getLoadingType() {
        return loadingType;
    }

    /**
     * is show loading dialog
     *
     * @param loadingType
     */
    public void setLoadingType(LoadingType loadingType) {
        this.loadingType = loadingType;
    }

    public HttpHandlerCoreListener<JSON_TYPE> getListener() {
        return listener;
    }

    /**
     * init asyncHttpClient
     */
    private AsyncHttpClientCore asyncHttpClient = new AsyncHttpClientCore() {

        @Override
        protected AsyncHttpRequestCore newAsyncHttpCacheRequest(
                DefaultHttpClient client, HttpContext httpContext,
                HttpUriRequest uriRequest, String contentType,
                TextResponseHandlerCore responseHandler, Context context,
                boolean onlyShowLocalCache, String cacheKey) {
            AsyncHttpRequestCore httpRequest = getHttpRequest(client,
                    httpContext, uriRequest, contentType, responseHandler,
                    context);
            return httpRequest == null ? super.newAsyncHttpCacheRequest(client,
                    httpContext, uriRequest, contentType, responseHandler,
                    context, onlyShowLocalCache, cacheKey) : httpRequest;
        }
    };

    public AsyncHttpRequestCore getHttpRequest(DefaultHttpClient client,
                                               HttpContext httpContext, HttpUriRequest uriRequest,
                                               String contentType, TextResponseHandlerCore responseHandler,
                                               Context context) {
        return null;
    }

    public AsyncHttpClientCore getAsyncHttpClientCore() {
        return this.asyncHttpClient;
    }

    /**
     * on key down event
     *
     * @param mContext
     */
    public void onCancelPressed(Context mContext) {
        asyncHttpClient.cancelRequests(mContext, true);

    }

    /**
     * @param handle
     */
    private void addRequestHandle(RequestHandle handle) {
        if (null != handle) {
            requestHandles.add(handle);
        }
    }

    public String getTaskUrl() {
        return taskUrl;
    }

    public void setTaskUrl(String taskUrl) {
        this.taskUrl = taskUrl;
    }

    /**
     * request task method
     *
     * @param mContext
     * @param httpTaskURl
     * @param action
     * @param params
     * @param taskType
     * @param cacheType
     */
    public void sendRequest(Context mContext, String httpTaskURl,
                            String action, Map<String, String> params, TaskType taskType,
                            TaskCacheType cacheType) {
        JniApi.appcontext = mContext;
        this.mContext = mContext;
        this.setTaskType(taskType);// Request method post or get
        this.setCacheType(cacheType);// Reads the local cache
        this.setTaskUrl(getTaskUrl() == null || getTaskUrl().equals("") ? httpTaskURl
                : getTaskUrl());
        this.actionParams = (action == null || action.equals("")) ? "" : action
                .trim();
        // this.cacheKey = getTaskUrl();

        boolean onlyShowLocalCache = false;
//		String contentType = RequestParams.APPLICATION_JSON;
        String contentType = RequestParams.APPLICATION_OCTET_STREAM;
        if (getCacheType() == TaskCacheType.READ_CACHE)// read cache
            onlyShowLocalCache = true;
        boolean isNetwork = isNetworkAvailable(mContext); // net
        if (!isNetwork && !onlyShowLocalCache) {// net error
            String errorText = mContext.getResources().getString(R.string.net_error);
            getListener().onFailure(errorText, null);
            return;
        }

        if (taskType == TaskType.POST && params != null)
            actionParams = String.format("%s?app=%s&mod=%s&act=%s",
                    actionParams,
                    params.containsKey("app") ? params.get("app") : "",
                    params.containsKey("mod") ? params.get("mod") : "",
                    params.containsKey("act") ? params.get("act") : "");

        doExecute(params, contentType, null, onlyShowLocalCache, taskType);
    }

    /**
     * send file to server
     *
     * @param mContext
     * @param httpTaskURl
     * @param action
     * @param params
     * @param taskType
     * @param cacheType
     */
    public void sendRequestFile(Context mContext, String httpTaskURl,
                                String action, Map<String, String> acParams,
                                RequestParams params, TaskType taskType,
                                TaskCacheType cacheType) {
        JniApi.appcontext = mContext;
        this.mContext = mContext;
        this.setTaskType(taskType);// Request method post or get
        this.setCacheType(cacheType);// Reads the local cache
        this.setTaskUrl(getTaskUrl() == null || getTaskUrl().equals("") ? httpTaskURl
                : getTaskUrl());
        this.actionParams = (action == null || action.equals("")) ? "" : action
                .trim();
        // this.cacheKey = getTaskUrl();
        boolean onlyShowLocalCache = false;
        // String contentType = RequestParams.APPLICATION_JSON;
        String contentType = RequestParams.APPLICATION_OCTET_STREAM;
        if (getCacheType() == TaskCacheType.READ_CACHE)// read cache
            onlyShowLocalCache = true;
        boolean isNetwork = isNetworkAvailable(mContext); // net
        if (!isNetwork && !onlyShowLocalCache) {// net error
            String errorText = mContext.getString(R.string.net_error);
            getListener().onFailure(errorText, null);
            return;
        }

        if (taskType == TaskType.FILE && acParams != null)
            actionParams = String.format("%s?app=%s&mod=%s&act=%s",
                    actionParams,
                    acParams.containsKey("app") ? acParams.get("app") : "",
                    acParams.containsKey("mod") ? acParams.get("mod") : "",
                    acParams.containsKey("act") ? acParams.get("act") : "");
        doExecute(null, contentType, params, onlyShowLocalCache, taskType);
    }

    /**
     * @param params
     * @param contentType
     * @param onlyShowLocalCache
     * @param taskType
     */
    private void doExecute(Map<String, String> params, String contentType,
                           RequestParams mParams, boolean onlyShowLocalCache, TaskType taskType) {
        if (taskType == TaskType.POST) {// POST
            addRequestHandle(executePost(mContext, getAsyncHttpClientCore(),
                    getTaskUrl() + actionParams, getRequestHeaders(),
                    contentType, new RequestParams(params), onlyShowLocalCache,
                    getResponseHandler()));

        } else if (taskType == TaskType.FILE) {

            contentType = RequestParams.APPLICATION_OCTET_STREAM;
            mParams.setHttpEntityIsRepeatable(true);
            mParams.setUseJsonStreamer(false);
            addRequestHandle(executePost(mContext, getAsyncHttpClientCore(),
                    getTaskUrl() + actionParams, getRequestHeaders(),
                    contentType, mParams, onlyShowLocalCache,
                    getResponseHandler()));
        } else {// Get
            addRequestHandle(executeGet(mContext, getAsyncHttpClientCore(),
                    getTaskUrl() + actionParams, getRequestHeaders(),
                    new RequestParams(params), onlyShowLocalCache,
                    getResponseHandler()));
        }
    }

    private boolean isNetworkAvailable(Context context) {
        if (context == null)
            return false;
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }

    /**
     * Perform a HTTP POST request and track the Android Context which initiated
     * the request. Set headers only for this request
     *
     * @param context         the Android Context which initiated the request.
     * @param url             the URL to send the request to.
     * @param headers         set headers only for this request
     * @param params          additional POST parameters to send with the request.
     * @param contentType     the content type of the payload you are sending, for example
     *                        application/json if sending a json payload.
     * @param responseHandler the response handler instance that should handle the response.
     * @return RequestHandle of future request process
     */
    public RequestHandle executePost(Context mContext,
                                     AsyncHttpClientCore client, String URL, Header[] headers,
                                     String contentType, RequestParams params,
                                     boolean onlyShowLocalCache, TextResponseHandlerCore responseHandler) {
        client.setTimeout(getDefaultTimeOut());
        client.setConnectTimeout(getDefaultConnectTimeout());
        client.setEnableRedirects(defaultEnableRedirects);
        return client.post(mContext, URL, headers, params, null,
                onlyShowLocalCache, responseHandler);

    }

    /**
     * Perform a HTTP GET request and track the Android Context which initiated
     * the request with customized headers
     *
     * @param context         Context to execute request against
     * @param url             the URL to send the request to.
     * @param headers         set headers only for this request
     * @param params          additional GET parameters to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     * @return RequestHandle of future request process
     */
    public RequestHandle executeGet(Context mContext,
                                    AsyncHttpClientCore client, String URL, Header[] headers,
                                    RequestParams params, boolean onlyShowLocalCache,
                                    TextResponseHandlerCore responseHandler) {
        client.setTimeout(getDefaultTimeOut());
        client.setConnectTimeout(getDefaultConnectTimeout());
        client.setEnableRedirects(defaultEnableRedirects);
        return client.get(mContext, URL, headers, params, onlyShowLocalCache,
                responseHandler);

    }

    /**
     * header
     *
     * @param headersText
     * @return
     */
    public Header[] getRequestHeaders() {
        List<Header> headers = getRequestHeadersList();
        return headers.toArray(new Header[headers.size()]);
    }

    private List<Header> getRequestHeadersList() {
        List<Header> headers = new ArrayList<Header>();
        String headersText = "";
        String headersRaw = headersText == null ? null : headersText.toString();

        // 学乎添加固定UA
        headers.add(new BasicHeader("User-Agent", "XUEHU_ANDROID_" + ApplicationInfo.getVersionName(mContext)));

        if (headersRaw != null && headersRaw.length() > 3) {
            String[] lines = headersRaw.split("\\r?\\n");
            for (String line : lines) {
                try {
                    int equalSignPos = line.indexOf('=');
                    if (1 > equalSignPos) {
                        throw new IllegalArgumentException(
                                "Wrong header format, may be 'Key=Value' only");
                    }

                    String headerName = line.substring(0, equalSignPos).trim();
                    String headerValue = line.substring(1 + equalSignPos)
                            .trim();
                    DebugLog.d(LOGTAG, String.format("Added header: [%s:%s]",
                            headerName, headerValue));

                    headers.add(new BasicHeader(headerName, headerValue));
                } catch (Throwable t) {
                    DebugLog.e(LOGTAG, "Not a valid header line: " + line, t);
                }
            }
        }
        return headers;
    }

    /**
     * Get request
     *
     * @param headersText
     * @return
     */
    private String getParams(Map<String, Object> headersText) {
        // DebugLog.d(LOGTAG, "requestText:" + headersText);
        StringBuffer strBuff = new StringBuffer();
        if (headersText != null) {
            Iterator<Map.Entry<String, Object>> it = headersText.entrySet()
                    .iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                strBuff.append(entry.getKey() + "=" + entry.getValue());
                // .getKey()+"="+entry.getValue()
                if (it.hasNext())
                    strBuff.append("&");
                // headers.add(new BasicHeader(entry.getKey(),
                // entry.getValue().toString()));
            }
            return "?" + strBuff;
        } else {
            return "";
        }
    }

//	/**
//	 * net response Text
//	 *
//	 * @return
//	 */
//	private HttpResponseHandlerGsonCore<JSON_TYPE> getResponseHandler() {
//		return new HttpResponseHandlerGsonCore<JSON_TYPE>(clazz) {
//			@Override
//			public void onStart() {
//				if (getLoadingType() == LoadingType.SHOW) {
//					// showLoadingDialog();
//					showLoading();
//				}
//			}
//
//			@Override
//			public void onLocalSuccess(JSON_TYPE responseEntity,
//					String rawJsonData, String cacheKey) {
//				try {
//					if (getCacheType() == TaskCacheType.READ_CACHE)
//						responseSuccess(rawJsonData, responseEntity, cacheKey);
//				} catch (Exception e) {
//					e.printStackTrace();
//					getListener().onFailure(e.getMessage(), responseEntity);
//				}
//
//			}
//
//			@Override
//			public void onSuccessCore(int statusCode, Header[] headers,
//					String rawJsonResponse, JSON_TYPE responseEntity,
//					String cacheKey) {
//				try {
//					responseSuccess(rawJsonResponse, responseEntity, cacheKey);
//				} catch (Exception e) {
//					e.printStackTrace();
//					getListener().onFailure(e.getMessage(), responseEntity);
//				}
//			}
//
//			@Override
//			public void ononFailureCore(int statusCode, Header[] headers,
//					Throwable throwable, String rawJsonData,
//					JSON_TYPE errorResponse, String cacheKey) {
//				String errorText = StatusCode.getSuccess(statusCode);
//				if (getListener() != null)
//					getListener().onFailure(errorText, responseEntity);
//
//				clearData(cacheKey);
//			}
//
//			@Override
//			public void onProgress(int bytesWritten, int totalSize) {
//
//			}
//
//			@Override
//			public void onFinish() {
//				if (getLoadingType() == LoadingType.SHOW) {
//					try {
//						// closeLoadingDialog();
//						onDismiss();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//
//		};
//
//	}

//	/**
//	 * handle response text
//	 *
//	 * @param response
//	 * @throws Exception
//	 * @throws Exception
//	 */
//	private void responseSuccess(String rawJsonData, JSON_TYPE responseEntity,
//			String cacheKey) throws Exception {
//		DebugLog.i(LOGTAG, "responseText:" + rawJsonData);// global response
//															// log
//		int resCode = 0;
//		String des = null;
//		JSONObject json = new JSONObject(rawJsonData);
//		if (!json.isNull("code"))
//			resCode = json.getInt("code");
//		if (!json.isNull("message"))
//			des = json.getString("message");
//		DebugLog.i(LOGTAG, "des:" + des);
//
//		if (resCode == RESPOSE_SUCCESS_CODE) {
//			getListener().onSuccess(responseEntity);
//		} else {// failure
//			getListener().onFailure(des);
//			clearData(cacheKey);
//		}
//		DebugLog.i(LOGTAG, "cacheKey:" + cacheKey);
//
//	}

    /**
     * net response Text
     *
     * @return
     */
    private HttpResponseHandlerGsonCore<JSON_TYPE> getResponseHandler() {
        return new HttpResponseHandlerGsonCore<JSON_TYPE>(clazz) {

            @Override
            public void onStart() {
                if (getLoadingType() == LoadingType.SHOW) {
                    // showLoadingDialog();
                    showLoading();
                }
            }

            @Override
            public void onLocalSuccess(JSON_TYPE responseEntity,
                                       String rawJsonData, String cacheKey) {
                try {
                    if (getCacheType() == TaskCacheType.READ_CACHE)
                        responseSuccess(rawJsonData, responseEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                    getListener().onFailure(e.getMessage(), responseEntity);
                }

            }

            @Override
            public void onSuccessCore(int statusCode, Header[] headers,
                                      String rawJsonResponse, JSON_TYPE responseEntity, String cacheKey) {
                try {
                    responseSuccess(rawJsonResponse, responseEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                    getListener().onFailure(e.getMessage(), responseEntity);
                }
            }

            @Override
            public void ononFailureCore(int statusCode, Header[] headers,
                                        Throwable throwable, String rawJsonData,
                                        JSON_TYPE errorResponse, String cacheKey) {
                String errorText = StatusCode.getSuccess(statusCode);
                if (getListener() != null)
                    getListener().onFailure(errorText, null);
                clearData(cacheKey);
            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {

            }

            @Override
            public void onFinish() {
                if (getLoadingType() == LoadingType.SHOW) {
                    try {
                        // closeLoadingDialog();
                        onDismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        };

    }

    /**
     * handle response text
     *
     * @param response
     * @throws Exception
     * @throws Exception
     */
    private void responseSuccess(String rawJsonData, JSON_TYPE responseEntity)
            throws Exception {
        DebugLog.i(LOGTAG, "responseText:" + rawJsonData);// global response
        // log

        // if(((AbstractBaseBean)responseEntity).getCode()==RESPOSE_SUCCESS_CODE){
        // getListener().onSuccess(responseEntity);
        // }else{
        // getListener().onFailure(((AbstractBaseBean)responseEntity).getMessage());
        // }
        int resCode = 0;
        String des = null;
        JSONObject json = new JSONObject(rawJsonData);
        if (!json.isNull("error_code"))
            resCode = json.getInt("error_code");
        if (!json.isNull("error_msg"))
            des = json.getString("error_msg");
        DebugLog.i(LOGTAG, "des:" + des);

        if (resCode == RESPOSE_SUCCESS_CODE) {
            getListener().onSuccess(responseEntity);
        } else if (resCode == 30001) {//token failure
            getListener().onTokenFailure(des, responseEntity);
        } else {
            getListener().onFailure(des, responseEntity);
        }

    }

    private void showLoading() {
        mProgressHUD = ProgressHUD
                .show(mContext, "加载中......", true, true, this);

    }

    private void onDismiss() {
        if (mProgressHUD != null)
            mProgressHUD.dismiss();
    }

    private void setProgress(String values) {
        if (mProgressHUD != null)
            mProgressHUD.setMessage(values);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        DebugLog.i(LOGTAG, "onCancel...");
        onCancelPressed(mContext);
        if (mProgressHUD != null)
            mProgressHUD.dismiss();
    }

    public TaskCacheType getCacheType() {
        return cacheType;
    }

    public void setCacheType(TaskCacheType cacheType) {
        this.cacheType = cacheType;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public int getDefaultTimeOut() {
        return defaultTimeOut;
    }

    public void setDefaultTimeOut(int defaultTimeOut) {
        this.defaultTimeOut = defaultTimeOut;
    }

    public int getDefaultConnectTimeout() {
        return defaultConnectTimeout;
    }

    public void setDefaultConnectTimeout(int defaultConnectTimeout) {
        this.defaultConnectTimeout = defaultConnectTimeout;
    }

    /**
     * @param cacheKey
     */
    private void clearData(String cacheKey) {
        if (cacheKey != null && getCacheType() == TaskCacheType.READ_CACHE)
            new ClearErrorData().executeOnExecutor(
                    AsyncTask.DUAL_THREAD_EXECUTOR, cacheKey);
    }

    /**
     * clear data
     *
     * @author shaxiaoning
     */
    public class ClearErrorData extends AsyncTask<String, Void, Integer> {
        public ClearErrorData() {

        }

        @Override
        protected Integer doInBackground(String... params) {
            int row = HttpCacheController.deleteFailure(params[0]);
            return row;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            DebugLog.i(LOGTAG, "clear error data:" + result);

        }
    }

}
