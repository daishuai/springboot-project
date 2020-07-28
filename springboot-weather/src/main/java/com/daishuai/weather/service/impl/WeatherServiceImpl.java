package com.daishuai.weather.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.daishuai.weather.dao.RegionDao;
import com.daishuai.weather.dao.WeatherForecastDao;
import com.daishuai.weather.dao.WeatherObserveDao;
import com.daishuai.weather.entity.RegionEntity;
import com.daishuai.weather.entity.WeatherForecastEntity;
import com.daishuai.weather.entity.WeatherObserveEntity;
import com.daishuai.weather.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daishuai
 * @date 2020/7/27 21:34
 */
@Slf4j
@Service
public class WeatherServiceImpl implements WeatherService {

    /**
     * 省份代码
     */
    public static final String CITY_URL = "http://www.weather.com.cn/data/city3jdata/china.html";

    public static final String BASE_URL = "https://forecast.weather.com.cn/napi/h5map/city/%s/jQuery%d?callback=jQuery%d";

    public static final String OBSERVE_DATE_FORMAT = "yyyyMMddHHmm";

    public static final String FORECAST_DATE_FORMAT = "yyyyMMddHH";

    @Autowired
    private RegionDao regionDao;

    @Autowired
    private WeatherForecastDao weatherForecastDao;

    @Autowired
    private WeatherObserveDao weatherObserveDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject getWeatherInfo(String regionCode) {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            URL url = new URL(String.format(BASE_URL, regionCode, currentTimeMillis, currentTimeMillis));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                String str = IOUtils.toString(inputStream, "utf-8");
                String substring = str.substring(str.indexOf("(") + 1, str.lastIndexOf(")"));
                JSONObject jsonObject = JSONObject.parseObject(substring);
                String status = jsonObject.getString("status");
                if (!StringUtils.equals(status, "success")) {
                    return jsonObject;
                }
                JSONObject result = jsonObject.getJSONObject("result");
                List<WeatherForecastEntity> forecastEntities = new ArrayList<>(result.size());
                List<WeatherObserveEntity> observeEntities = new ArrayList<>(result.size());
                result.keySet().forEach(key -> {
                    JSONObject weatherInfo = result.getJSONObject(key);
                    JSONObject area = weatherInfo.getJSONObject("area");
                    String areaid = area.getString("areaid");
                    String name = area.getString("name");
                    WeatherObserveEntity observeEntity = new WeatherObserveEntity();
                    JSONObject obs = weatherInfo.getJSONObject("obs");
                    observeEntity.setRegionCode(areaid);
                    observeEntity.setRegionName(name);
                    observeEntity.setTemperature(obs.getDouble("tem"));
                    observeEntity.setRelativeHumidity(obs.getInteger("rh"));
                    observeEntity.setWindSpeed(obs.getString("ws"));
                    observeEntity.setWindDirection(obs.getString("wd"));
                    String obsfctime = obs.getString("fctime");
                    observeEntity.setUuid(areaid + "-" + obsfctime);
                    try {
                        Date observeTime = DateUtils.parseDate(obsfctime, OBSERVE_DATE_FORMAT);
                        observeEntity.setForecastTime(observeTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    observeEntities.add(observeEntity);
                    WeatherForecastEntity forecastEntity = new WeatherForecastEntity();
                    JSONObject forecast24h = weatherInfo.getJSONObject("forecast24h");
                    forecastEntity.setRegionCode(areaid);
                    forecastEntity.setRegionName(name);
                    forecastEntity.setDayWeather(forecast24h.getString("dayweather"));
                    forecastEntity.setDayWindSpeed(forecast24h.getString("dayws"));
                    forecastEntity.setDayWindDirection(forecast24h.getString("daywd"));
                    forecastEntity.setNightWeather(forecast24h.getString("nightweather"));
                    forecastEntity.setNightWindSpeed(forecast24h.getString("nightws"));
                    forecastEntity.setNightWindDirection(forecast24h.getString("nightwd"));
                    forecastEntity.setMinTemperature(forecast24h.getDouble("mintem"));
                    forecastEntity.setMaxTemperature(forecast24h.getDouble("maxtem"));
                    String fctime = forecast24h.getString("fctime");
                    forecastEntity.setUuid(areaid + "-" + fctime);
                    try {
                        Date forecastTime = DateUtils.parseDate(fctime, FORECAST_DATE_FORMAT);
                        forecastEntity.setForecastTime(forecastTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    forecastEntities.add(forecastEntity);
                });
                weatherForecastDao.saveAll(forecastEntities);
                weatherObserveDao.saveAll(observeEntities);
                log.info(jsonObject.toJSONString());
                return jsonObject;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<RegionEntity> getRegions() {
        try {
            URL url = new URL(CITY_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                String str = IOUtils.toString(inputStream, "utf-8");
                JSONObject regionJson = JSON.parseObject(str);
                log.info(str);
                List<RegionEntity> regions = regionJson.keySet().stream().map(key -> {
                    RegionEntity region = new RegionEntity();
                    region.setCode(key);
                    region.setName(regionJson.getString(key));
                    region.setLevel(2);
                    region.setParentCode("101");
                    return region;
                }).collect(Collectors.toList());
                regionDao.saveAll(regions);
                return regions;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
