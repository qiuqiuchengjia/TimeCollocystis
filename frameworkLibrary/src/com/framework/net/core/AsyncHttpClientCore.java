package com.framework.net.core;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * create 2015-4-10
 * 
 * @author shaxiaoning
 *
 */
public class AsyncHttpClientCore extends AsyncHttpClient {
	/**
	 * Creates a new AsyncHttpClient with default constructor arguments values
	 */
	public AsyncHttpClientCore() {
		super();
	}

	/**
	 * Creates a new AsyncHttpClient.
	 *
	 * @param httpPort
	 *            non-standard HTTP-only port
	 */
	public AsyncHttpClientCore(int httpPort) {
		super(false, httpPort, 443);
	}

	/**
	 * Creates a new AsyncHttpClient.
	 *
	 * @param httpPort
	 *            non-standard HTTP-only port
	 * @param httpsPort
	 *            non-standard HTTPS-only port
	 */
	public AsyncHttpClientCore(int httpPort, int httpsPort) {
		super(false, httpPort, httpsPort);
	}

	/**
	 * Creates new AsyncHttpClient using given params
	 *
	 * @param fixNoHttpResponseException
	 *            Whether to fix issue or not, by omitting SSL verification
	 * @param httpPort
	 *            HTTP port to be used, must be greater than 0
	 * @param httpsPort
	 *            HTTPS port to be used, must be greater than 0
	 */
	public AsyncHttpClientCore(boolean fixNoHttpResponseException,
			int httpPort, int httpsPort) {
		super(fixNoHttpResponseException, httpPort, httpsPort);
	}

	/**
	 * Perform a HTTP POST request and track the Android Context which initiated
	 * the request. Set headers only for this request
	 *
	 * @param context
	 *            the Android Context which initiated the request.
	 * @param url
	 *            the URL to send the request to.
	 * @param headers
	 *            set headers only for this request
	 * @param params
	 *            additional POST parameters to send with the request.
	 * @param contentType
	 *            the content type of the payload you are sending, for example
	 *            application/json if sending a json payload.
	 * @param responseHandler
	 *            the response handler instance that should handle the response.
	 * @return RequestHandle of future request process
	 */
	public RequestHandle post(Context context, String url, Header[] headers,
			RequestParams params, String contentType,
			boolean onlyShowLocalCache, TextResponseHandlerCore responseHandler) {
		HttpEntityEnclosingRequestBase request = new HttpPost(URI.create(url)
				.normalize());
		if (params != null)
			request.setEntity(paramsToEntity(params, responseHandler));
		if (headers != null)
			request.setHeaders(headers);
		String uri = getUrlWithQueryString(isUrlEncodingEnabled(), url, params);
		return sendRequest((DefaultHttpClient) getHttpClient(),
				getHttpContext(), request, contentType, responseHandler,
				context, onlyShowLocalCache, uri);
	}

	/**
	 * Perform a HTTP GET request and track the Android Context which initiated
	 * the request with customized headers
	 *
	 * @param context
	 *            Context to execute request against
	 * @param url
	 *            the URL to send the request to.
	 * @param headers
	 *            set headers only for this request
	 * @param params
	 *            additional GET parameters to send with the request.
	 * @param responseHandler
	 *            the response handler instance that should handle the response.
	 * @return RequestHandle of future request process
	 */
	public RequestHandle get(Context context, String url, Header[] headers,
			RequestParams params, boolean onlyShowLocalCache,
			TextResponseHandlerCore responseHandler) {
		String uri = getUrlWithQueryString(isUrlEncodingEnabled(), url, params);
		HttpUriRequest request = new HttpGet(uri);
		if (headers != null)
			request.setHeaders(headers);
		return sendRequest((DefaultHttpClient) getHttpClient(),
				getHttpContext(), request, null, responseHandler, context,
				onlyShowLocalCache, uri);
	}

	private Map<Context, List<RequestHandle>> getRequestMap() {
		try {
			Field field = AsyncHttpClient.class.getDeclaredField("requestMap");
			field.setAccessible(true);
			return (Map<Context, List<RequestHandle>>) field.get(this);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns HttpEntity containing data from RequestParams included with
	 * request declaration. Allows also passing progress from upload via
	 * provided ResponseHandler
	 *
	 * @param params
	 *            additional request params
	 * @param responseHandler
	 *            ResponseHandlerInterface or its subclass to be notified on
	 *            progress
	 */
	private HttpEntity paramsToEntity(RequestParams params,
			ResponseHandlerInterface responseHandler) {
		HttpEntity entity = null;

		try {
			if (params != null) {
				entity = params.getEntity(responseHandler);
			}
		} catch (IOException e) {
			if (responseHandler != null) {
				responseHandler.sendFailureMessage(0, null, null, e);
			} else {
				e.printStackTrace();
			}
		}

		return entity;
	}

	/**
	 * Puts a new request in queue as a new thread in pool to be executed
	 *
	 * @param client
	 *            HttpClient to be used for request, can differ in single
	 *            requests
	 * @param contentType
	 *            MIME body type, for POST and PUT requests, may be null
	 * @param context
	 *            Context of Android application, to hold the reference of
	 *            request
	 * @param httpContext
	 *            HttpContext in which the request will be executed
	 * @param responseHandler
	 *            ResponseHandler or its subclass to put the response into
	 * @param uriRequest
	 *            instance of HttpUriRequest, which means it must be of
	 *            HttpDelete, HttpPost, HttpGet, HttpPut, etc.
	 * @return RequestHandle of future request process
	 */
	protected RequestHandle sendRequest(DefaultHttpClient client,
			HttpContext httpContext, HttpUriRequest uriRequest,
			String contentType, TextResponseHandlerCore responseHandler,
			Context context, boolean onlyShowLocalCache, String cacheKey) {
		if (uriRequest == null) {
			throw new IllegalArgumentException(
					"HttpUriRequest must not be null");
		}

		if (responseHandler == null) {
			throw new IllegalArgumentException(
					"ResponseHandler must not be null");
		}

		if (responseHandler.getUseSynchronousMode()) {// &&
														// !responseHandler.getUsePoolThread()
			throw new IllegalArgumentException(
					"Synchronous ResponseHandler used in AsyncHttpClient. You should create your response handler in a looper thread or use SyncHttpClient instead.");
		}

		if (contentType != null) {
			if (uriRequest instanceof HttpEntityEnclosingRequestBase
					&& ((HttpEntityEnclosingRequestBase) uriRequest)
							.getEntity() != null) {
				Log.w(LOG_TAG,
						"Passed contentType will be ignored because HttpEntity sets content type");
			} else {
				uriRequest.setHeader(HEADER_CONTENT_TYPE, contentType);
			}
		}

		responseHandler.setRequestHeaders(uriRequest.getAllHeaders());
		responseHandler.setRequestURI(uriRequest.getURI());

		AsyncHttpRequestCore request = newAsyncHttpCacheRequest(client,
				httpContext, uriRequest, contentType, responseHandler, context,
				onlyShowLocalCache, cacheKey);
		getThreadPool().submit(request);
		RequestHandle requestHandle = new RequestHandle(request);

		if (context != null) {
			// Add request to request map
			Map<Context, List<RequestHandle>> requestMapHolder = getRequestMap();
			List<RequestHandle> requestList = requestMapHolder.get(context);
			synchronized (requestMapHolder) {
				if (requestList == null) {
					requestList = Collections
							.synchronizedList(new LinkedList<RequestHandle>());
					requestMapHolder.put(context, requestList);
				}
			}

			requestList.add(requestHandle);

			Iterator<RequestHandle> iterator = requestList.iterator();
			while (iterator.hasNext()) {
				if (iterator.next().shouldBeGarbageCollected()) {
					iterator.remove();
				}
			}
		}

		return requestHandle;
	}

	protected AsyncHttpRequestCore newAsyncHttpCacheRequest(
			DefaultHttpClient client, HttpContext httpContext,
			HttpUriRequest uriRequest, String contentType,
			TextResponseHandlerCore responseHandler, Context context,
			boolean onlyShowLocalCache, String cacheKey) {
		return new AsyncHttpRequestCore(client, httpContext, uriRequest,
				responseHandler, onlyShowLocalCache, cacheKey);
	}
}
