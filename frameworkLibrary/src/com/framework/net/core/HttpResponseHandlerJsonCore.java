package com.framework.net.core;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

/**
 * Created by 2015-4-10
 *
 * @param <JSON_TYPE>
 */
public abstract class HttpResponseHandlerJsonCore<JSON_TYPE> extends
        TextResponseHandlerCore {
    private static final String LOG_TAG = "BaseJsonHttpResponseHandler";

    /**
     * Creates a new JsonHttpResponseHandler with default charset "UTF-8"
     */
    public HttpResponseHandlerJsonCore() {
        this(DEFAULT_CHARSET);
    }

    /**
     * Creates a new JsonHttpResponseHandler with given string encoding
     *
     * @param encoding result string encoding, see <a href=
     *                 "http://docs.oracle.com/javase/7/docs/api/java/nio/charset/Charset.html"
     *                 >Charset</a>
     */
    public HttpResponseHandlerJsonCore(String encoding) {
        super();
    }

    @Override
    public void onCache(final String rawJsonResponse, final String cacheKey) {
        Runnable parser = new Runnable() {
            @Override
            public void run() {
                try {
                    final JSON_TYPE jsonResponse = parseResponse(
                            rawJsonResponse, cacheKey, false);
                    postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            onLocalSuccess(jsonResponse, rawJsonResponse, cacheKey);

                        }
                    });
                } catch (final Throwable t) {
                    postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            onLocalFailure(t);
                        }
                    });
                }
            }
        };
        if (!getUseSynchronousMode()) {
            new Thread(parser).start();
        } else {
            // In synchronous mode everything should be run on one thread
            parser.run();
        }
    }

    /**
     * Base abstract method, handling defined generic type
     *
     * @param statusCode      HTTP status line
     * @param headers         response headers
     * @param rawJsonResponse string of response, can be null
     * @param response        response returned by {@link #parseResponse(String, boolean)}
     */
    public abstract void onSuccess(int statusCode, Header[] headers,
                                   String rawJsonResponse, JSON_TYPE responseEntity, String cacheKey);

    /**
     * Base abstract method, handling defined generic type
     *
     * @param statusCode    HTTP status line
     * @param headers       response headers
     * @param throwable     error thrown while processing request
     * @param rawJsonData   raw string data returned if any
     * @param errorResponse response returned by {@link #parseResponse(String, boolean)}
     */
    public abstract void onFailure(int statusCode, Header[] headers,
                                   Throwable throwable, String rawJsonData, JSON_TYPE errorResponse, String cacheKey);

    @Override
    public final void onSuccess(final int statusCode, final Header[] headers,
                                final String responseString) {
        if (statusCode != HttpStatus.SC_NO_CONTENT) {
            Runnable parser = new Runnable() {
                @Override
                public void run() {
                    try {
                        final JSON_TYPE jsonResponse = parseResponse(
                                responseString, null, false);
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                onSuccess(statusCode, headers, responseString,
                                        jsonResponse, null);
                            }
                        });
                    } catch (final Throwable t) {
                        // Log.d(LOG_TAG, "parseResponse thrown an problem", t);
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                onFailure(statusCode, headers, t,
                                        responseString, null, null);
                            }
                        });
                    }
                }
            };
            if (!getUseSynchronousMode()) {
                new Thread(parser).start();
            } else {
                // In synchronous mode everything should be run on one thread
                parser.run();
            }
        } else {
            onSuccess(statusCode, headers, null, null, null);
        }
    }

    @Override
    public final void onFailure(final int statusCode, final Header[] headers,
                                final String responseString, final Throwable throwable) {
        if (responseString != null) {
            Runnable parser = new Runnable() {
                @Override
                public void run() {
                    try {
                        final JSON_TYPE jsonResponse = parseResponse(
                                responseString, null, true);
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                onFailure(statusCode, headers, throwable,
                                        responseString, jsonResponse, null);
                            }
                        });
                    } catch (Throwable t) {
                        // Log.d(LOG_TAG, "parseResponse thrown an problem", t);
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                onFailure(statusCode, headers, throwable,
                                        responseString, null, null);
                            }
                        });
                    }
                }
            };
            if (!getUseSynchronousMode()) {
                new Thread(parser).start();
            } else {
                // In synchronous mode everything should be run on one thread
                parser.run();
            }
        } else {
            onFailure(statusCode, headers, throwable, null, null, null);
        }
    }

    /**
     * Should return deserialized instance of generic type, may return object
     * for more vague handling
     *
     * @param rawJsonData response string, may be null
     * @param isFailure   indicating if this method is called from onFailure or not
     * @return object of generic type or possibly null if you choose so
     * @throws Throwable allows you to throw anything from within deserializing JSON
     *                   response
     */
    protected abstract JSON_TYPE parseResponse(String rawJsonData,
                                               String cacheKey, boolean isFailure) throws Throwable;

    /**
     * Reads the local cache
     *
     * @param responseEntity entiry
     * @param rawJsonData    responseText
     */
    public abstract void onLocalSuccess(JSON_TYPE responseEntity,
                                        String rawJsonData, String cacheKey);

    public abstract void onLocalFailure(Throwable t);
}
