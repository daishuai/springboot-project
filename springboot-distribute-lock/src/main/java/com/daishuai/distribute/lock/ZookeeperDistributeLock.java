package com.daishuai.distribute.lock;

import org.springframework.stereotype.Component;

/**
 * Zookeeper实现分布式锁
 *
 * @author Daishuai
 * @date 2020/9/21 13:40
 */
@Component
public class ZookeeperDistributeLock implements DistributeLock {
    @Override
    public boolean lock(String lockKey) {
        return false;
    }

    @Override
    public boolean lock(String lockKey, long expireMillis, int retryTimes, long retryInterval) {
        return false;
    }

    @Override
    public boolean releaseLock(String lockKey) {
        return false;
    }
}
