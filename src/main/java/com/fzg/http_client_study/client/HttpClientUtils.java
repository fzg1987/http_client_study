package com.fzg.http_client_study.client;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpClientUtils {
    public static String sendGet(String urlParam, String token) {
        // 1. 创建httpClient实例对象
        HttpClient httpClient = new HttpClient();
        // 设置httpClient连接主机服务器超时时间：15000毫秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        // 2. 创建GetMethod实例对象
        GetMethod getMethod = new GetMethod(urlParam);
        // 3. 设置post请求超时时间，请求头
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,60000);
        getMethod.addRequestHeader("Content-Type","application/json");
        if (StringUtils.isEmpty(token)) {
            Header header = new Header("Authorization","Breaker " + token);
            getMethod.addRequestHeader(header);
        }
        try {
            // 4. 执行getMethod，调用http接口
            httpClient.executeMethod(getMethod);
            // 5. 读取内容（流的形式读取）
            InputStream is = getMethod.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            // 采用线程安全的StringBuffer
            StringBuffer res = new StringBuffer();
            String str = "";
            while ((str = br.readLine()) != null) {
                res.append(str);
            }
            return res.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 6. 释放连接
            getMethod.releaseConnection();
        }
        return null;
    }
}
