package cn.tocute.util;

import cn.tocute.conf.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.MDC;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * header中不能传输中文，否则需要发送和接收方使用urlencode及urldecode
 */

@Slf4j
public class HttpUtil {
    private static final CloseableHttpClient httpclient = HttpClients.createDefault();

    public static String postByJson(String url, RequestConfig config, Map<String, String> headerMap, String jsonString) throws IOException {
        HttpPost httppost = new HttpPost(url);
        if (config == null) {
            config = RequestConfig.custom()
                    .setConnectionRequestTimeout(1_000)//http连接池获取连接的时间
                    .setConnectTimeout(3_000)        //http建立三次握手的时间
                    .setSocketTimeout(30_000)        //http数据包返回最长间隔时间，单数据包就是数据返回时间
                    .build();
        }
        httppost.setConfig(config);
        if (headerMap == null) {
            headerMap = new HashMap<>();
        }
        headerMap.put("User-Agent", "java/ai");
        headerMap.put(Constant.TRACE_ID, MDC.get(Constant.TRACE_ID));
        for (Map.Entry<String, String> header : headerMap.entrySet()) {
            httppost.setHeader(header.getKey(), header.getValue());
        }
        if (null == jsonString) {
            throw new RuntimeException("postByJson must has body json String.");
        }

        StringEntity StringEntity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
        httppost.setEntity(StringEntity);

        try (CloseableHttpResponse response = httpclient.execute(httppost)) {
            int httpCode = response.getStatusLine().getStatusCode();
            if (httpCode != HttpStatus.SC_OK) {
                log.info("request httpCode:{}", httpCode);
                throw new RuntimeException("http response code err：" + httpCode);
            }
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } catch (
                IOException e) {
            throw new RuntimeException("http request IOException：" + e.getMessage(), e);
        }

    }

    public static String postByUrlEncodedForm(String url, RequestConfig config, Map<String, String> headerMap, Map<String, String> bodyMap) throws IOException {
        HttpPost httppost = new HttpPost(url);
        if (config == null) {
            config = RequestConfig.custom()
                    .setConnectionRequestTimeout(1_000)//http连接池获取连接的时间
                    .setConnectTimeout(3_000)        //http建立三次握手的时间
                    .setSocketTimeout(30_000)        //http数据包返回最长间隔时间，单数据包就是数据返回时间
                    .build();
        }
        httppost.setConfig(config);
        if (headerMap == null) {
            headerMap = new HashMap<>();
        }
        headerMap.put("User-Agent", "java/ai");
        headerMap.put(Constant.TRACE_ID, MDC.get(Constant.TRACE_ID));
        for (Map.Entry<String, String> header : headerMap.entrySet()) {
            httppost.setHeader(header.getKey(), header.getValue());
        }
        if (bodyMap != null) {
            ArrayList<NameValuePair> pairList = new ArrayList<>();
            for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
                BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
                pairList.add(pair);
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairList, StandardCharsets.UTF_8);
            httppost.setEntity(entity);
        }

        try (CloseableHttpResponse response = httpclient.execute(httppost)) {
            int httpCode = response.getStatusLine().getStatusCode();
            if (httpCode != HttpStatus.SC_OK) {
                log.info("request httpCode:{}", httpCode);
                throw new RuntimeException("http response code err：" + httpCode);
            }
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("http request IOException：" + e.getMessage(), e);
        }
    }

}
