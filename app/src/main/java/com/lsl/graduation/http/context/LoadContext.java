package com.lsl.graduation.http.context;


import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import com.lsl.graduation.http.loader.LoadDispatcher;
import com.lsl.graduation.http.loadlistener.LoadListener;
import com.lsl.graduation.http.parser.Parser;
import com.lsl.graduation.utils.MLog;

public abstract class LoadContext<Result> {

    /**
     * Flag indicates only cache should be loaded
     */
    public static final int FLAG_CACHE_ONLY = 0x100;

    /**
     * Flag indicates only HTTP content should be loaded
     */
    public static final int FLAG_HTTP_ONLY = 0x101;

    /**
     * Flag indicates cache should be loaded first, load HTTP content
     * if failed to load cache
     */
    public static final int FLAG_CACHE_FIRST = 0x102;

    /**
     * Flag indicates HTTP content should be loaded first, load cache
     * if failed to load cache
     */
    public static final int FLAG_HTTP_FIRST = 0x103;


    /**
     * Flag indicates result is loaded from cache
     */
    public static final int TYPE_CACHE = 0x200;

    /**
     * Flag indicates result is loaded from HTTP
     */
    public static final int TYPE_HTTP = 0x201;

    public enum HTTP_LOAD_METHOD {
        HTTP_METHOD_POST,
        HTTP_METHOD_GET,
        HTTP_METHOD_PUT,
    }

    protected String url = null;
    protected LoadListener<Result> listener = null;
    protected Result result = null;
    protected Parser<Result> parser = null;
    protected int flag;
    protected HTTP_LOAD_METHOD method;
    protected Map<String, String> params = new HashMap<String, String>();
    protected Map<String, String> headers = new HashMap<String, String>();
    protected Exception exception;
    protected boolean canceled;
    protected int type = TYPE_HTTP;
    protected Dialog dialog;
    public static Context appCtx;
    protected Context ctx;
    public Map<String, String> responseHeaders = Collections.emptyMap();

    //重试次数
    private int tryTimes = 0;

    public LoadContext() {
    }

    public LoadContext(LoadContext<Result> o) {
        this.url = o.url;
        this.listener = o.listener;
        this.result = o.result;
    }

    public String getUrl() {
        return url;
    }

    protected LoadContext<Result> post(String url) {
        this.url = url;
        this.method = HTTP_LOAD_METHOD.HTTP_METHOD_POST;
        return this;
    }

    protected LoadContext<Result> get(String url) {
        this.url = url;
        this.method = HTTP_LOAD_METHOD.HTTP_METHOD_GET;
        return this;
    }

    protected LoadContext<Result> put(String url) {
        this.url = url;
        this.method = HTTP_LOAD_METHOD.HTTP_METHOD_PUT;
        return this;
    }

    public LoadListener<Result> getListener() {
        return listener;
    }

    protected LoadContext<Result> listener(LoadListener<Result> listener) {
        this.listener = listener;
        return this;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public LoadContext<Result> dialog(Dialog dialog) {
        this.dialog = dialog;
        return this;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Parser<Result> getParser() {
        return parser;
    }

    protected LoadContext<Result> parser(Parser<Result> parser) {
        this.parser = parser;
        return this;
    }

    public Context getCtx() {
        return ctx;
    }

    public LoadContext<Result> ctx(Activity context) {
        this.ctx = context;
        return this;
    }

    protected LoadContext<Result> flag(int flag) {
        this.flag = flag;
        return this;
    }

    public int getFlag() {
        return flag;
    }

    public int getTryTimes() {
        return tryTimes;
    }

    public void increaseTryTimes() {
        tryTimes++;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void canceled(boolean canceled) {
        this.canceled = canceled;
    }

    public HTTP_LOAD_METHOD getMethod() {
        return method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    protected LoadContext<Result> param(String key, String value) {
        params.put(key, value);
        return this;
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    @Override
    public String toString() {
        return "LoadContext [param = " + url + ", target = " + listener + ", resul = " + result + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof LoadContext)) return false;
        try {
            @SuppressWarnings("unchecked")
            LoadContext<Result> context = (LoadContext<Result>) o;
            return url.equals(context.url) && listener.equals(context.listener);
        } catch (ClassCastException e) {
            return false;
        }
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public LoadContext<Result> headers(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public String getParamsString() {
        if (!TextUtils.isEmpty(url)) {
            StringBuilder builder = new StringBuilder();
            builder.append(url);
            Iterator<String> iterator = params.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                builder.append(key + "=" + params.get(key));
            }
            return builder.toString();
        }
        return null;
    }

    public void load() {
        if (parser == null && TextUtils.isEmpty(url)) {
            MLog.w( "this context lost param:  'parser'");
            return;
        } else if (TextUtils.isEmpty(url)) {
            MLog.w( "this context lost param:  'url'");
            return;
        }
        for (String key : params.keySet()) {
            MLog.i("param---" + key + ":" + params.get(key));
        }
        LoadDispatcher.getInstance().startLoading(this);
    }
}

