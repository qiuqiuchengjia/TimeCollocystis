package com.framework.net.core;

import android.text.TextUtils;

import com.framework.log.DebugLog;
import com.framework.log.LogUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by 2015-4-10
 *
 * @param <JSON_TYPE>
 */
public abstract class HttpResponseHandlerGsonCore<JSON_TYPE> extends
        HttpResponseHandlerJsonCore<JSON_TYPE> {
    private static final String LOGTAG = LogUtil
            .makeLogTag(HttpResponseHandlerGsonCore.class);
    private final Class<JSON_TYPE> clazz;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss");
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(
            Date.class, new JsonDeserializer<Date>() {
                @Override
                public Date deserialize(JsonElement json,
                                        java.lang.reflect.Type typeOfT,
                                        JsonDeserializationContext context)
                        throws JsonParseException {
                    DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String dateString = json.getAsString()
                            .replace("Z", "+0000");
                    return DATE_FORMAT.parse(dateString, new ParsePosition(0));
                }
            }).create();

    public HttpResponseHandlerGsonCore(Class<JSON_TYPE> parClazz) {
        this.clazz = parClazz;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers,
                          String rawJsonResponse, JSON_TYPE responseEntity, String cacheKey) {
        onSuccessCore(statusCode, headers, rawJsonResponse, responseEntity, cacheKey);

    }

    @Override
    public void onFailure(int statusCode, Header[] headers,
                          Throwable throwable, String rawJsonData, JSON_TYPE errorResponse, String cacheKey) {
        ononFailureCore(statusCode, headers, throwable, rawJsonData,
                errorResponse, cacheKey);

        try {
            if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
                // try login
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected JSON_TYPE parseResponse(String s, String url, boolean b)
            throws Throwable {
        try {
            if (TextUtils.isEmpty(s)) {
                return null;
            }

            return GSON.fromJson(s, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onLocalFailure(Throwable t) {
        DebugLog.e(LOGTAG, "onLocalFailure:" + t.getMessage());

    }

    /**
     * success core
     *
     * @param statusCode
     * @param headers
     * @param rawJsonResponse
     * @param responseEntity
     */

    abstract public void onSuccessCore(int statusCode, Header[] headers,
                                       String rawJsonResponse, JSON_TYPE responseEntity, String cacheKey);

    /**
     * failure core
     *
     * @param statusCode
     * @param headers
     * @param throwable
     * @param rawJsonData
     * @param errorResponse
     */
    abstract public void ononFailureCore(int statusCode, Header[] headers,
                                         Throwable throwable, String rawJsonData, JSON_TYPE errorResponse, String cacheKey);
}
