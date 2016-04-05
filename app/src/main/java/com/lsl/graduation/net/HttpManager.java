package com.lsl.graduation.net;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * Created by Forrest on 16/3/30.
 */
public class HttpManager {
    static IHttpManger proxy=new HttpManagerImpl();
    public static void setProxy(IHttpManger manager) {
        if (manager != null)
            proxy = manager;
        else{
            proxy=new HttpManagerImpl();
        }
    }

    public static IHttpManger getProxy() {
        return proxy;
    }

    public static HttpResponse executeHttpGet(String url, Map<String, String> parameters, Map<String, String> headers) throws IOException {
        return proxy.excuteHttpGet(url, parameters, headers);
    }

    public static HttpResponse executeHttpPost(String url, Map<String, String> parameters, Map<String, String> headers)
            throws IOException {
        return proxy.excuteHttpPost(url, parameters, headers);
    }

//	public static boolean shouldUseProxy() {
//		return proxy.shouldUseProxy();
//	}

    public static DefaultHttpClient getHttpClient(boolean isHttp) {
        return proxy.getHttpClient(isHttp);
    }

    public static HttpURLConnection getUrlConnection(String url) throws IOException{
        return proxy.getHttpUrlConnection(url);
    }

    public static InputStream getInputStream(String url) throws IOException {
        return proxy.getInputStream(url);
    }

    public static String getHttpText(String url) throws IOException {
        return proxy.getHttpText(url);
    }
}
