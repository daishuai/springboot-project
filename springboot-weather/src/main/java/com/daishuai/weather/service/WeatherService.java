package com.daishuai.weather.service;

import com.alibaba.fastjson.JSONObject;
import com.daishuai.weather.entity.RegionEntity;

import java.util.List;

/**
 * @author Daishuai
 * @date 2020/7/27 21:33
 */
public interface WeatherService {

    /**
     * 获取天气信息
     *
     * @param regionCode
     * @return
     */
    JSONObject getWeatherInfo(String regionCode);

    /**
     * 获取行政区划信息
     *
     * @return
     */
    List<RegionEntity> getRegions();

    /**
     * 获取行政区划树
     *
     * @return
     */
    List<RegionEntity> getRegionTree();
}
