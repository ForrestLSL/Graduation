package com.lsl.graduation.http.net;
import com.lsl.graduation.Configs;
import com.lsl.graduation.utils.MLog;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * Created by Forrest on 16/3/30.
 */
    class HttpManagerImpl implements   IHttpManger {
    private static final HttpParams defaultParams;
    static {
        defaultParams=new BasicHttpParams();
        // 设置连接超时和 Socket 超时，
        HttpConnectionParams.setConnectionTimeout(defaultParams, Configs.CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(defaultParams,Configs.SO_TIMEOUT);

    }

    @Override
    public HttpResponse excuteHttpGet(String url, Map<String, String> parameters, Map<String, String> headers) throws IOException {
        DefaultHttpClient httpClient=getHttpClient(true);
        HttpResponse response;
        response=httpClient.execute(httpGet(url,parameters,headers));
        validResponse(response);

        return response;
    }

    @Override
    public HttpResponse excuteHttpPost(String url, Map<String, String> parameters, Map<String, String> headers) throws IOException {
        DefaultHttpClient httpClient = getHttpClient(true);
        HttpResponse response;
        response = httpClient.execute(getHttpPost(url, parameters, headers));
        validResponse(response);

        return response;
    }

    @Override
    public DefaultHttpClient getHttpClient(boolean isHttps) {
        DefaultHttpClient httpClient=new DefaultHttpClient(defaultParams);
        httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
            @Override
            public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
                if (!httpRequest.containsHeader("Accept-Encoding")) {
                    httpRequest.addHeader("Accept-Encoding", "gzip");
                }
                if (!httpRequest.containsHeader("Content-Type")) {
                    httpRequest.addHeader("Content-Type", "text/html;charset=UTF-8");
                }
            }
        });
        httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
            @Override
            public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
                HttpEntity entity=httpResponse.getEntity();
                if (null!=entity){
                    Header header=entity.getContentEncoding();
                    if (header!=null){
                        HeaderElement []codecs=header.getElements();
                        for (int i=0;i<codecs.length;i++){
                            if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                                MLog.d("HttpManager", "use gzip");
                                httpResponse.setEntity(new GzipDecompressingEntity(
                                        httpResponse.getEntity()));
                                return;
                            }
                        }
                    }
                }
            }
        });
        return httpClient;
    }

    @Override
    public InputStream getInputStream(String url) throws IOException {
        HttpURLConnection connection=getUrlConnection(url);
        if (connection.getResponseCode()!=200){
            throw new IOException("responseCode:"
                    + connection.getResponseCode() + " response message:"
                    + connection.getResponseMessage());
        }
        InputStream is = connection.getInputStream();
        String encoding = connection.getHeaderField("Content-Encoding");
        if (encoding != null && GZIP_PATTERN.matcher(encoding).find()) {
            MLog.d("HttpManager", "use gzip");
            is = new GZIPInputStream(is);
        }
        return is;
    }

    @Override
    public String getHttpText(String url) throws IOException {
        HttpURLConnection connection=getUrlConnection(url);
        if (connection.getResponseCode()!=200){
            throw new IOException("responseCode:"
                    + connection.getResponseCode() + " response message:"
                    + connection.getResponseMessage());
        }
        InputStream is=connection.getInputStream();
//        String encoding = connection.getHeaderField("Content-Encoding");
//        if (encoding != null && GZIP_PATTERN.matcher(encoding).find()) {
//            is = new GZIPInputStream(is);
//        }
        String res = read(new InputStreamReader(new BufferedInputStream(is)))
                .toString();
        if (is != null) {
            try {
                is.close();
            } catch (Exception e) {
            }
        }
        connection.disconnect();
        return res;
    }

    @Override
    public HttpURLConnection getHttpUrlConnection(String url) throws IOException {

        return getHttpUrlConnection(url);
    }
    private final static class GzipDecompressingEntity extends
            HttpEntityWrapper {

        public GzipDecompressingEntity(final HttpEntity entity) {
            super(entity);
        }

        @Override
        public InputStream getContent() throws IOException,
                IllegalStateException {

            // the wrapped entity's getContent() decides about repeatability
            InputStream wrappedin = wrappedEntity.getContent();

            return new GZIPInputStream(wrappedin);
        }

        @Override
        public long getContentLength() {
            // length of ungzipped content is not known
            return -1;
        }

    }

    /**
     * HttpGet方法
     * @param url
     * @param parameters
     * @param headers
     * @return
     */
    private HttpGet httpGet(String url,Map<String, String> parameters,Map<String, String> headers){
        StringBuilder sb=new StringBuilder();
        if (parameters!=null){
            for (String key :
                    parameters.keySet()) {
                try {
                    //将代码格式设置成为utf-8的格式
                    sb.append("&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(parameters.get(key), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }
        if (sb.length()!=0){
            sb.deleteCharAt(0);
            if (url.contains("?")){
                url = url + "&" + sb.toString();
            } else {
                url = url + "?" + sb.toString();
            }
        }
        // 把实体数据设置到请求对象

        HttpGet httpGet = new HttpGet(url);
        //如果有响应头，就需要添加以下内容，
//        SharedPreferences sp = AppContext.getInstance().getSharedPreferences("UserConfig", Context.MODE_PRIVATE);
//        String session = sp.getString("session", null);
//        String token = sp.getString("token", null);
//        httpGet.addHeader("version", Config.version+"");
//        httpGet.addHeader("User-Agent", Config.userAgent);
//        httpGet.addHeader("platform","android");
//        httpGet.addHeader("mobile-time", System.currentTimeMillis()+"");
//        httpGet.addHeader("channel",AppContext.CHANNEL);
//        if (!TextUtils.isEmpty(session)) {
//            httpGet.addHeader("Cookie", session);
//        }
//        if(!TextUtils.isEmpty(token)){
//            httpGet.addHeader("sso_token", token);
//        }
//        if (headers != null) {
//            Iterator<String> iterator = headers.keySet().iterator();
//            while (iterator.hasNext()) {
//                String key = iterator.next();
//                httpGet.addHeader(key, headers.get(key));
//            }
//        }

        return httpGet;
    }
    /** 判断是否成功返回*/
    private HttpResponse validResponse(HttpResponse response)throws IOException {
        if (response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK&&response.getStatusLine().getStatusCode()!=HttpStatus.SC_NO_CONTENT){

            throw new IOException("response Code:"
                    + response.getStatusLine().getStatusCode());
        }
        return response;
    }
    /** Post方法*/
    private HttpPost getHttpPost(String url,
                                 Map<String, String> parameters,
                                 Map<String, String> headers) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (parameters != null) {
            for (String key : parameters.keySet()) {
                params.add(new BasicNameValuePair(key, parameters.get(key)));
            }
        }
//        String curtime = Long.toString(System.currentTimeMillis());
//        params.add(new BasicNameValuePair("md5str", getPHONE_MD5(curtime)));
//        params.add(new BasicNameValuePair("csrf_time", curtime));
//        params.add(new BasicNameValuePair("hq_phone", "true"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
        // 把实体数据设置到请求对象
        httpPost.setEntity(entity);
//        SharedPreferences sp = AppContext.getInstance().getSharedPreferences("UserConfig", Context.MODE_PRIVATE);
//        String session = sp.getString("session", null);
//        String token = sp.getString("token", null);
//        httpPost.addHeader("version", Config.version+"");
//        httpPost.addHeader("User-Agent", Config.userAgent);
//        httpPost.addHeader("platform","android");
//        httpPost.addHeader("mobile-time", System.currentTimeMillis()+"");
//        httpPost.addHeader("channel",AppContext.CHANNEL);
//        if (!TextUtils.isEmpty(session)) {
//            httpPost.addHeader("Cookie", session);
//        }
//        if(!TextUtils.isEmpty(token)){
//            httpPost.addHeader("sso_token", token);
//        }
//        if (headers != null) {
//            Iterator<String> iterator = headers.keySet().iterator();
//            while (iterator.hasNext()) {
//                String key = iterator.next();
//                httpPost.addHeader(key, headers.get(key));
//            }
//        }
        return httpPost;
    }
     private HttpURLConnection getUrlConnection(String url)
            throws IOException {
        URL httpUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) httpUrl
                .openConnection();
        // Log.e("Sdebug", "getHttpUrlConnection http.proxyHost=" +
        // System.getProperty("http.proxyHost"));
        // if (shouldUseProxy()) {
        // connection = (HttpURLConnection) httpUrl.openConnection();
        // setProxy();
        // } else{
        // connection = (HttpURLConnection) httpUrl.openConnection();
        // unsetProxy();
        // }
        connection.setConnectTimeout(Configs.CONNECTION_TIMEOUT);
        connection.setReadTimeout(Configs.SO_TIMEOUT);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("GET");
        connection.addRequestProperty("Accept-Encoding", "gzip");
        connection.connect();
        return connection;
    }
    final static Pattern GZIP_PATTERN = Pattern.compile("gzip",
            Pattern.CASE_INSENSITIVE);
    public static StringBuilder read(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] cbuf = new char[8192];
        int len;
        while (-1 != (len = reader.read(cbuf))) {
            sb.append(cbuf, 0, len);
        }
        return sb;
    }

}
