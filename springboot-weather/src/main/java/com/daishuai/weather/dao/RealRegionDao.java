package com.daishuai.weather.dao;

import com.daishuai.weather.entity.RealRegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Daishuai
 * @date 2020/11/4 12:04
 */
public interface RealRegionDao extends JpaRepository<RealRegionEntity, Long> {

    /**
     * 根据行政级别查询
     *
     * @param level
     * @return
     */
    List<RealRegionEntity> findByXzjb(short level);

    /**
     * 根据行政内部编码查询
     *
     * @param innerCode
     * @return
     */
    List<RealRegionEntity> findByXznbbmLike(String innerCode);
}
