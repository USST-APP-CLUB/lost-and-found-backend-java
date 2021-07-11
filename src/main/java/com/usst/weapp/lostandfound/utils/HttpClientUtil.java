package com.usst.weapp.lostandfound.utils;

import lombok.Data;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;


import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author Sunforge
 * @Date 2021-07-10 11:20
 * @Version V1.0.0
 * @Description 向其他服务器发送http请求 【注意，静态方法不会参加实例化。】
 */
@Data
@Component
public class HttpClientUtil {

    private static final HttpClientBuilder httpClientBuilder = HttpClients.custom();
    static {
        //
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", trustHttpsCertificates())
                .build();

        // 连接池
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(50); // 连接池最大有50个连接
        cm.setDefaultMaxPerRoute(50); // 每个路由（域名）最大有50个连接 baidu.com/a baidu.com/b 是同一个域名
        httpClientBuilder.setConnectionManager(cm);

        // 设置请求默认配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);

//        // 设置默认header
//        List<Header> defaultHeaders = new ArrayList<>();
//        BasicHeader userAgentHeader = new BasicHeader("User-Agent", "x");
//        defaultHeaders.add(userAgentHeader);
//        httpClientBuilder.setDefaultHeaders(defaultHeaders);

    }

    public static String executeGet(String url, Map<String, String> headers) throws IOException{
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpGet httpGet = new HttpGet(url);
        if(headers != null){
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for(Map.Entry<String, String> entry : entries){
                httpGet.addHeader(new BasicHeader(entry.getKey(), entry.getValue()));
            }
        }
        CloseableHttpResponse response = null;
        response = closeableHttpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String toStringResult = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        HttpClientUtils.closeQuietly(response);
        return toStringResult;

//        return null;
    }

    public static String postForm(String url, List<NameValuePair> list, Map<String, String> headers) throws IOException {
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(url);
        if(headers != null){
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for(Map.Entry<String, String> entry : entries){
                httpPost.addHeader(new BasicHeader(entry.getKey(), entry.getValue()));
            }
        }
        // 确保请求是form类型
        httpPost.addHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
        // 给post 设置参数
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(list, Consts.UTF_8);
        httpPost.setEntity(formEntity);
        CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
        StatusLine statusLine = response.getStatusLine();
        if(HttpStatus.SC_OK != statusLine.getStatusCode()){
            return null;
        }
        HttpEntity entity = response.getEntity();
        String resultStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        HttpClientUtils.closeQuietly(response);
        return resultStr;

    }

//    @Async
    public String postJsonToWelink(String url, Map<String, String> body, Map<String,String> headers) throws IOException {
        return postJson(url, body, headers);
    }

    public String getJsonFromWelink(String url,Map<String, String> body, Map<String,String> headers) throws IOException{
        return executeGet("" + url + convertMapToPathParam(body), headers);
    }

    public static String postJson(String url, Map<String, String> body, Map<String,String> headers) throws IOException {
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(url);
        if(headers != null){
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for(Map.Entry<String, String> entry : entries){
                httpPost.addHeader(new BasicHeader(entry.getKey(), entry.getValue()));
            }
        }
        // 确保请求头是json类型
        httpPost.addHeader("Content-Type","application/json; charset=utf-8");
        // 设置请求体
        String bodyStr = JSONUtil.convertMapToJsonString(body);
        StringEntity jsonEntity = new StringEntity(bodyStr, Consts.UTF_8);
        jsonEntity.setContentType("application/json; charset=utf-8");
        jsonEntity.setContentEncoding(Consts.UTF_8.name());
        httpPost.setEntity(jsonEntity);

        CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
        StatusLine statusLine = response.getStatusLine();
        if(HttpStatus.SC_OK != statusLine.getStatusCode()){
            return null;
        }
        HttpEntity entity = response.getEntity();
        String resultStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        HttpClientUtils.closeQuietly(response);
        return resultStr;
    }

    public static String postJson(String url, String body, Map<String,String> headers) throws IOException {
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(url);
        if(headers != null){
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for(Map.Entry<String, String> entry : entries){
                httpPost.addHeader(new BasicHeader(entry.getKey(), entry.getValue()));
            }
        }
        // 确保请求头是json类型
        httpPost.addHeader("Content-Type","application/json; charset=utf-8");
        // 设置请求体
        StringEntity jsonEntity = new StringEntity(body, Consts.UTF_8);
        jsonEntity.setContentType("application/json; charset=utf-8");
        jsonEntity.setContentEncoding(Consts.UTF_8.name());
        httpPost.setEntity(jsonEntity);

        CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
        StatusLine statusLine = response.getStatusLine();
        if(HttpStatus.SC_OK != statusLine.getStatusCode()){
            return null;
        }
        HttpEntity entity = response.getEntity();
        String resultStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        HttpClientUtils.closeQuietly(response);
        return resultStr;
    }

    private static ConnectionSocketFactory trustHttpsCertificates() {
        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
        try{
            sslContextBuilder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            });
            SSLContext sslContext = sslContextBuilder.build();
            return new SSLConnectionSocketFactory(sslContext,
                    new String[]{"SSLv2Hello","SSLv3","TLSv1","TLSv1.1","TLSv1.2"},
                    null, NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String convertMapToPathParam(Map<String, String> body) throws UnsupportedEncodingException {
        StringBuilder paramStr = new StringBuilder("");
        for (Map.Entry<String, String> entry : body.entrySet()){
            paramStr.append("?")
                    .append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.name()))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name()))
                    .append("&");
        }
        paramStr.deleteCharAt(paramStr.length()-1);
        return paramStr.toString();
    }

}
