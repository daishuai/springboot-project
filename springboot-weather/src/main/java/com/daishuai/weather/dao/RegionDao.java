package com.daishuai.weather.dao;

import com.daishuai.weather.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Daishuai
 * @date 2020/7/28 8:48
 */
public interface RegionDao extends JpaRepository<RegionEntity, String> {
}
