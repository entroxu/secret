package cn.tocute.util;

import cn.tocute.conf.Constant;
import cn.tocute.conf.HttpConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
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
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.MDC;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * header中不能传输中文，否则需要发送和接收方使用urlencode及urldecode
 */

@Slf4j
public class HttpUtil {
    private static final CloseableHttpClient httpclient = HttpClients.createDefault();

    private static final String userAgent = "java/agent";

    public static String doPostByJson(String url, RequestConfig config, List<Header> headerList,  String jsonString) throws IOException {
        if (null == jsonString) {
            throw new RuntimeException("doPostByJson need json body.");
        }

        StringEntity StringEntity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
        return doPost(url,config,headerList,StringEntity);
    }

    public static String doPostByUrlEncodedForm(String url, RequestConfig config, List<Header> headerList, Map<String, String> bodyMap) throws IOException {

        if (bodyMap == null) {
            throw new RuntimeException("doPostByUrlEncodedForm need urlEncodeForm body.");
        }
        ArrayList<NameValuePair> pairList = new ArrayList<>();
        for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
            BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
            pairList.add(pair);
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairList, StandardCharsets.UTF_8);

        return doPost(url, config, headerList, entity);

    }


    public static String doPost(String url, RequestConfig config, List<Header> headerList, HttpEntity httpEntity) {
        HttpPost httppost = new HttpPost(url);
        if (config == null) {
            config = RequestConfig.custom()
                    .setConnectionRequestTimeout(1_000)//http连接池获取连接的时间
                    .setConnectTimeout(3_000)        //http建立三次握手的时间
                    .setSocketTimeout(30_000)        //http数据包返回最长间隔时间，单数据包就是数据返回时间
                    .build();
        }
        httppost.setConfig(config);

        if (headerList == null) {
            headerList = new ArrayList<>();
        }
        headerList.add(new BasicHeader(HttpConstant.USER_AGENT, userAgent));
        //headerList.add(new BasicHeader(Constant.TRACE_ID, MDC.get(Constant.TRACE_ID)));
        Header[] headers = headerList.toArray(new Header[0]);

        httppost.setHeaders(headers);
        httppost.setEntity(httpEntity);


        try (CloseableHttpResponse response = httpclient.execute(httppost)) {
            int httpCode = response.getStatusLine().getStatusCode();
            if (httpCode != HttpStatus.SC_OK) {
                throw new RuntimeException("post http response code err：" + httpCode);
            }
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("post http request IOException：" + e.getMessage(), e);
        }
    }

}
