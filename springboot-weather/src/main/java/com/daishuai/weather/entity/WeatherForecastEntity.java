package com.daishuai.weather.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 24小时天气预报
 * @author Daishuai
 * @date 2020/7/28 10:47
 */
@Data
@Entity
@Table(name = "weather_forecast")
public class WeatherForecastEntity {

    @Id
    private String uuid;

    /**
     * 区域编码
     */
    private String regionCode;

    /**
     * 区域名称
     */
    private String regionName;

    /**
     * 白天天气
     */
    private String dayWeather;

    /**
     * 白天风速
     */
    private String dayWindSpeed;

    /**
     * 白天风向
     */
    private String dayWindDirection;

    /**
     * 最低温度
     */
    private Double minTemperature;

    /**
     * 最高温度
     */
    private Double maxTemperature;

    /**
     * 夜晚风速
     */
    private String nightWindSpeed;

    /**
     * 夜晚风向
     */
    private String nightWindDirection;

    /**
     * 夜晚天气
     */
    private String nightWeather;

    /**
     * 预报时间
     */
    private Date forecastTime;


}
