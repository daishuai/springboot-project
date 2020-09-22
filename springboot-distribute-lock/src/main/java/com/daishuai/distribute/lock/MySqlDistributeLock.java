package com.daishuai.distribute.lock;

import com.daishuai.distribute.dao.DistributeLockMapper;
import com.daishuai.distribute.entity.DistributeLockEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * 数据库实现的分布式锁
 *
 * @author Daishuai
 * @date 2020/9/21 13:38
 */
@Slf4j
@Component
public class MySqlDistributeLock implements DistributeLock{

    @Autowired
    private DistributeLockMapper distributeLockMapper;

    private static final int EXPIRE_TIME = 10000;

    private final ThreadLocal<String> lockFlag = new ThreadLocal<String>();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean lock(String lockKey) {
        DistributeLockEntity lockInfo = distributeLockMapper.findLockInfo(lockKey);
        if (lockInfo != null) {
            Date createTime = lockInfo.getCreateTime();
            System.out.println(DateFormatUtils.format(createTime, "yyyy-MM-dd HH:mm:ss"));
            return false;
        }
        String holderId = UUID.randomUUID().toString().replaceAll("-", "");
        Date now = new Date();
        System.out.println(DateFormatUtils.format(now, "yyyy-MM-dd HH:mm:ss"));
        DistributeLockEntity lockEntity = new DistributeLockEntity();
        lockEntity.setUniqueMutex(lockKey);
        lockEntity.setHolderId(holderId);
        lockEntity.setCreateTime(now);
        try {
            int row = distributeLockMapper.lock(lockEntity);
            lockFlag.set(holderId);
            return row == 1;
        } catch (Exception e) {
            log.error("failed acquire lock ... ,{}", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean lock(String lockKey, long expireMillis, int retryTimes, long retryInterval) {
        return false;
    }

    @Override
    public boolean releaseLock(String lockKey) {
        int row = distributeLockMapper.releaseLock(lockKey, lockFlag.get());
        if (row > 0) {
            lockFlag.remove();
            return true;
        }
        return false;
    }
}
