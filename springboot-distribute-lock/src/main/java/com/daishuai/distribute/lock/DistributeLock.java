package com.daishuai.distribute.lock;

/**
 * @author Daishuai
 * @date 2020/9/21 11:18
 */
public interface DistributeLock {

    /**
     * 获取分布式锁
     *
     * @param lockKey
     * @return
     */
    boolean lock(String lockKey);

    /**
     * 释放锁
     *
     * @param lockKey
     * @return
     */
    boolean releaseLock(String lockKey);
}
