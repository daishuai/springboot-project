package com.daishuai.weather.task;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Daishuai
 * @date 2020/7/27 21:05
 */
@Slf4j
//@Component
public class WeatherCrawlerTask {

    public static final String BASE_URL = "https://forecast.weather.com.cn/napi/h5map/city/101/jQuery1595854792282?callback=jQuery1595854792282";

    @Scheduled(cron = "0 0/5 * * * ?")
    public void execute() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(6000).build();

        HttpGet httpGet = new HttpGet(BASE_URL);
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity entity = httpResponse.getEntity();
            log.info(JSON.toJSONString(entity));
        }
    }
}
