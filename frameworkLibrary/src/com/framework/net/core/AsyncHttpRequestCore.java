package com.framework.net.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import android.text.TextUtils;
import android.util.Log;

import com.framework.log.DebugLog;
import com.framework.log.LogUtil;
import com.framework.net.db.controller.HttpCacheController;
import com.framework.net.db.domain.HttpCacheEntity;
import com.framework.net.db.utils.JniApi;
import com.framework.os.NetworkManager;
import com.loopj.android.http.AsyncHttpRequest;

/**
 * AsyncHttpRequestCore create by 2015-4-10
 *
 */
public class AsyncHttpRequestCore extends AsyncHttpRequest implements Runnable {
	private static final String LOGTAG = LogUtil
			.makeLogTag(AsyncHttpRequestCore.class);
	private final AbstractHttpClient client;
	private final HttpContext context;
	private final HttpUriRequest request;
	private final TextResponseHandlerCore responseHandler;
	private int executionCount;
	private boolean isCancelled = false;
	private boolean cancelIsNotified = false;
	private boolean isFinished = false;
	private String cacheKey;
	private boolean onlyShowLocalCache;

	public AsyncHttpRequestCore(AbstractHttpClient client, HttpContext context,
			HttpUriRequest request, TextResponseHandlerCore responseHandler,
			boolean onlyShowLocalCache, String cacheKey) {
		super(client, context, request, responseHandler);
		this.client = client;
		this.context = context;
		this.request = request;
		this.responseHandler = responseHandler;
		this.onlyShowLocalCache = onlyShowLocalCache;
		this.cacheKey = TextUtils.isEmpty(cacheKey) ? request.getURI()
				.toString() : cacheKey;
		DebugLog.i(LOGTAG, "cacheKey:" + cacheKey);

	}

	@Override
	public void run() {
		if (isCancelled()) {
			return;
		}

		if (responseHandler != null) {
			responseHandler.sendStartMessage();
		}

		if (isCancelled()) {
			return;
		}

		try {
			makeRequestWithRetries();
		} catch (IOException e) {
			if (!isCancelled() && responseHandler != null) {
				responseHandler.sendFailureMessage(0, null, null, e);
			} else {
				Log.e("AsyncHttpRequest",
						"makeRequestWithRetries returned error, but handler is null",
						e);
			}
		}

		if (isCancelled()) {
			return;
		}

		if (responseHandler != null) {
			responseHandler.sendFinishMessage();
		}

		isFinished = true;
	}

	private void makeRequest() throws IOException {
		if (isCancelled()) {
			return;
		}
		// Fixes #115
		if (request.getURI().getScheme() == null) {
			// subclass of IOException so processed in the caller
			throw new MalformedURLException("No valid URI scheme was provided");
		}
		if (new NetworkManager().isNetworkAvailable(JniApi.appcontext))// net

			HttpCacheController.clearLocalCache();

		// ---------读取缓存------------------------
		if (onlyShowLocalCache) {
			byte[] cache = HttpCacheController.getCacheByUrl(cacheKey);
			DebugLog.i(LOGTAG, "makeRequest-reading:" + cache);
			if (cache != null && cache.length > 0) {
				responseHandler.sendCacheMessage(cache, cacheKey);
				return;
			}
		}
		// ---------读取缓存结束------------------------

		HttpResponse response = client.execute(request, context);
		DebugLog.i(LOGTAG, "response:" + response);
		DebugLog.i(LOGTAG, "response.getStatusLine().getStatusCode():"
				+ response.getStatusLine().getStatusCode());
		if (response.getStatusLine().getStatusCode() == 200) {
			// response.getEntity().
			// ------------- 写缓存--------------
			if (!isCancelled() && responseHandler != null) {
				try {
					byte[] newCache = responseHandler
							.sendResponseMessageWithCache(response, cacheKey);
					DebugLog.i(LOGTAG, "makeRequest-newCache:" + newCache);
					if (onlyShowLocalCache) {
						if (newCache != null) {
							HttpCacheController
									.addOrUpdate(new HttpCacheEntity(cacheKey,
											newCache));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// ------------- 写缓存结束-------------
		// Carry out pre-processing for this response.
		responseHandler.onPreProcessResponse(responseHandler, response);

		if (isCancelled()) {
			return;
		}
		// Carry out post-processing for this response.
		responseHandler.onPostProcessResponse(responseHandler, response);

	}

	private void makeRequestWithRetries() throws IOException {
		boolean retry = true;
		IOException cause = null;
		HttpRequestRetryHandler retryHandler = client
				.getHttpRequestRetryHandler();
		try {
			while (retry) {
				try {
					makeRequest();
					return;
				} catch (UnknownHostException e) {
					// switching between WI-FI and mobile data networks can
					// cause a retry which then results in an
					// UnknownHostException
					// while the WI-FI is initialising. The retry logic will be
					// invoked here, if this is NOT the first retry
					// (to assist in genuine cases of unknown host) which seems
					// better than outright failure
					cause = new IOException("UnknownHostException exception: "
							+ e.getMessage());
					retry = (executionCount > 0)
							&& retryHandler.retryRequest(cause,
									++executionCount, context);
				} catch (NullPointerException e) {
					// there's a bug in HttpClient 4.0.x that on some occasions
					// causes
					// DefaultRequestExecutor to throw an NPE, see
					// http://code.google.com/p/android/issues/detail?id=5255
					cause = new IOException("NPE in HttpClient: "
							+ e.getMessage());
					retry = retryHandler.retryRequest(cause, ++executionCount,
							context);
				} catch (IOException e) {
					if (isCancelled()) {
						// Eating exception, as the request was cancelled
						return;
					}
					cause = e;
					retry = retryHandler.retryRequest(cause, ++executionCount,
							context);
				}
				if (retry && (responseHandler != null)) {
					responseHandler.sendRetryMessage(executionCount);
				}
			}
		} catch (Exception e) {
			// catch anything else to ensure failure message is propagated
			Log.e("AsyncHttpRequest", "Unhandled exception origin cause", e);
			cause = new IOException("Unhandled exception: " + e.getMessage());
		}

		// cleaned up to throw IOException
		throw (cause);
	}

	public boolean isCancelled() {
		if (isCancelled) {
			sendCancelNotification();
		}
		return isCancelled;
	}

	private synchronized void sendCancelNotification() {
		if (!isFinished && isCancelled && !cancelIsNotified) {
			cancelIsNotified = true;
			if (responseHandler != null)
				responseHandler.sendCancelMessage();
		}
	}

	public boolean isDone() {
		return isCancelled() || isFinished;
	}

	public boolean cancel(boolean mayInterruptIfRunning) {
		isCancelled = true;
		request.abort();
		return isCancelled();
	}
}
