package com.daishuai.weather.feign;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Daishuai
 * @date 2020/7/27 21:17
 */
@FeignClient(name = "weather-feign", url = "https://forecast.weather.com.cn")
public interface WeatherFeignClient {

    /**
     * 获取天气信息
     * @return
     */
    @GetMapping(value = "/napi/h5map/city/101/jQuery1595854792282?callback=jQuery1595854792282", produces = "application/json; charset=UTF-8")
    Object getWeatherInfo();
}
