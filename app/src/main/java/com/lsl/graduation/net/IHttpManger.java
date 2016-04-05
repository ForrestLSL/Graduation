package com.lsl.graduation.net;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 *
 * Created by Forrest on 16/3/30.
 */
public interface IHttpManger {
    enum HTTP_METHOD{
        GET,POST
    }

    /***
     *  返回Http应答,若不是200,则将抛出异常
     * @param url
     * @param parameters
     * @param headers
     * @return
     * @throws IOException
     */
    public abstract HttpResponse excuteHttpGet(String url, Map<String, String> parameters, Map<String, String> headers)throws IOException;
    /***
     *  返回Http应答,若不是200,则将抛出异常
     * @param url
     * @param parameters
     * @param headers
     * @return
     * @throws IOException
     */
    public abstract HttpResponse excuteHttpPost(String url, Map<String, String> parameters, Map<String, String> headers)throws IOException;
    public abstract DefaultHttpClient getHttpClient(boolean isHttps);
    /**
     * Http应答的便捷方法,如果Http返回的不是200也将抛出异常
     *
     * @param url
     * @return
     * @throws IOException
     */
    public abstract InputStream getInputStream(String url)throws IOException;

    /***
     * getInputStream的便捷方法,直接获得文本资源。
     * @param url
     * @return
     * @throws IOException
     */
    public abstract String getHttpText(String url)throws IOException;

    public abstract HttpURLConnection getHttpUrlConnection(String url)throws IOException;
}
