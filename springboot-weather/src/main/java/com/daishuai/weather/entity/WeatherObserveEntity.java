package com.daishuai.weather.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 实时监测天气
 * @author Daishuai
 * @date 2020/7/28 10:47
 */
@Data
@Table(name = "weather_observe")
@Entity
public class WeatherObserveEntity {

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
     * 相对湿度
     */
    private Integer relativeHumidity;

    /**
     * 风速
     */
    private String windSpeed;

    /**
     * 风向
     */
    private String windDirection;

    /**
     * 温度
     */
    private Double temperature;

    /**
     * 预报时间
     */
    private Date forecastTime;
}
