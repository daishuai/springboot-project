package com.daishuai.distribute.dao;

import com.daishuai.distribute.entity.DistributeLockEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Daishuai
 * @date 2020/9/22 13:24
 */
@Repository
public interface DistributeLockMapper {

    /**
     * 数据库加锁
     *
     * @param lockEntity
     * @return
     */
    int lock(DistributeLockEntity lockEntity);

    /**
     * 释放锁
     *
     * @param uniqueMutex
     * @param holderId
     * @return
     */
    int releaseLock(@Param(value = "uniqueMutex") String uniqueMutex, @Param(value = "holderId") String holderId);

    /**
     * 查询锁信息
     *
     * @param uniqueMutex
     * @return
     */
    DistributeLockEntity findLockInfo(String uniqueMutex);
}
