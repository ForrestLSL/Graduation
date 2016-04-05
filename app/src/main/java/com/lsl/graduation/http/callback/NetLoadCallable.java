package com.lsl.graduation.http.callback;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

import android.util.Log;

import com.lsl.graduation.http.cache.Cache;
import com.lsl.graduation.http.context.LoadContext;
import com.lsl.graduation.http.loader.LoadDispatcher;
import com.lsl.graduation.http.loadlistener.LoadListener;
import com.lsl.graduation.http.net.HttpManager;
import com.lsl.graduation.http.utils.ByteArrayPool;
import com.lsl.graduation.http.utils.PoolingByteArrayOutputStream;
import com.lsl.graduation.utils.MLog;


public class NetLoadCallable<Result> implements Callable<Result> {

	private ByteArrayPool mPool;
	private static int DEFAULT_POOL_SIZE = 4096;
	private LoadContext<Result> context;

	public NetLoadCallable(LoadContext<Result> context) {
		this.context = context;
		mPool = new ByteArrayPool(DEFAULT_POOL_SIZE);
		// if (context.getTarget() instanceof LoadListener) {
		// this.LoadListener = (LoadListener<Result>) context.getTarget();
		// }
	}

	@Override
	public Result call() throws IOException, ParseException {
		if (context.getParser() == null)
			throw new IllegalArgumentException("LoadContext: "
					+ context.getUrl()
					+ " is illegal, Parser cannot be null");
		Result r = loadHTTP();
		context.setResult(r);
		if (null != context.getListener()
				&& context.getListener() instanceof LoadListener) {
			((LoadListener<Result>) context.getListener()).postExecut(context);
		}
		return r;
	}

	private Result loadHTTP() throws IOException, ParseException {
		String param = context.getUrl();
		HttpResponse response = null;
		long duration = System.currentTimeMillis();
		switch (context.getMethod()) {
		case HTTP_METHOD_POST:
			response = HttpManager.executeHttpPost(param, context.getParams(), context.getHeaders());
			break;
		case HTTP_METHOD_PUT:
//			response = HttpManager.executeHttpPut(param, context.getParams(), context.getHeaders());
			break;
		default:
			response = HttpManager.executeHttpGet(param, context.getParams(), context.getHeaders());
			break;
		}
		Result r = null;
		if (response != null) {
			byte[] responseContents = null;
			Map<String, String> responseHeaders = new HashMap<String, String>();
			responseHeaders = convertHeaders(response.getAllHeaders());
			if (response.getEntity() != null) {
				responseContents = entityToBytes(response.getEntity());
//				MLog.d(new String(responseContents));
			} else {
				responseContents = new byte[0];
			}
			r = context.getParser().parse(responseContents);
			Map<String, String> map_value = new HashMap<String, String>();
			map_value.put("url", context.getUrl());
//			MobclickAgent.onEventValue(context.appCtx, "LoadData", map_value, (int) ((System.currentTimeMillis()-duration)/1000));
			MLog.d(((System.currentTimeMillis() - duration) / 1000) + "");
			Cache.Entry entry = new Cache.Entry();
			entry.data = responseContents;
			entry.responseHeaders = responseHeaders;
			String headerValue = responseHeaders.get("Date");
	        if (headerValue != null) {
	        	entry.softTtl = parseDateAsEpoch(headerValue);
	        }
	        entry.ttl = System.currentTimeMillis() + 1000*60*3;
			LoadDispatcher.getCacheManager().put(context.getParamsString(), entry);
			context.setResponseHeaders(responseHeaders);
//			SharedPreferences sp = AppContext.getInstance().getSharedPreferences("UserConfig", Context.MODE_PRIVATE);
//			String session = responseHeaders.get("Set-Cookie");
//			if(!TextUtils.isEmpty(session)){
//				sp.edit().putString("session", session).commit();
//			}
			context.setType(LoadContext.TYPE_HTTP);
		}
		return r;
	}

	private static Map<String, String> convertHeaders(Header[] headers) {
		Map<String, String> result = new HashMap<String, String>();
		for (int i = 0; i < headers.length; i++) {
			result.put(headers[i].getName(), headers[i].getValue());
		}
		return result;
	}

	/** Reads the contents of HttpEntity into a byte[]. */
	private byte[] entityToBytes(HttpEntity entity) throws IOException
			 {
		PoolingByteArrayOutputStream bytes = new PoolingByteArrayOutputStream(
				mPool, (int) entity.getContentLength());
		byte[] buffer = null;
		try {
			InputStream in = entity.getContent();
			if (in == null) {
				throw new RuntimeException("InputStream is null");
			}
			buffer = mPool.getBuf(1024);
			int count;
			while ((count = in.read(buffer)) != -1) {
				bytes.write(buffer, 0, count);
			}
			return bytes.toByteArray();
		} finally {
			try {
				// Close the InputStream and release the resources by
				// "consuming the content".
				entity.consumeContent();
			} catch (IOException e) {
				// This can happen if there was an exception above that left the
				// entity in
				// an invalid state.
			}
			mPool.returnBuf(buffer);
			bytes.close();
		}
	}
	
	/**
     * Parse date in RFC1123 format, and return its value as epoch
     */
    public static long parseDateAsEpoch(String dateStr) {
        try {
            // Parse date in RFC1123 format if this header contains one
            return DateUtils.parseDate(dateStr).getTime();
        } catch (DateParseException e) {
            // Date in invalid format, fallback to 0
            return 0;
        }
    }
}