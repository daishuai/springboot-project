package com.daishuai.weather.dao;

import com.daishuai.weather.entity.WeatherForecastEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Daishuai
 * @date 2020/7/28 11:16
 */
public interface WeatherForecastDao extends JpaRepository<WeatherForecastEntity, String> {
}
