package com.daishuai.weather.dao;

import com.daishuai.weather.entity.WeatherObserveEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Daishuai
 * @date 2020/7/28 11:17
 */
public interface WeatherObserveDao extends JpaRepository<WeatherObserveEntity, String> {
}
