package com.daishuai.distribute.lock;

import org.springframework.stereotype.Component;

/**
 * 数据库实现的分布式锁
 *
 * @author Daishuai
 * @date 2020/9/21 13:38
 */
@Component
public class MySqlDistributeLock implements DistributeLock{
    @Override
    public boolean lock(String lockKey) {
        return false;
    }

    @Override
    public boolean releaseLock(String lockKey) {
        return false;
    }
}
