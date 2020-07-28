package com.daishuai.weather.controller;

import com.daishuai.weather.feign.WeatherFeignClient;
import com.daishuai.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Daishuai
 * @date 2020/7/27 21:21
 */
@RestController
public class WeatherController {

    @Autowired
    private WeatherFeignClient weatherFeignClient;

    @Autowired
    private WeatherService weatherService;

    @GetMapping(value = "/weather/{regionCode}")
    public Object getWeatherInfo(@PathVariable(value = "regionCode") String regionCode) {
        return weatherService.getWeatherInfo(regionCode);
    }

    @GetMapping(value = "/regions")
    public Object getRegions() {
        return weatherService.getRegions();
    }
}
