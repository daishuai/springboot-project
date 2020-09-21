package com.daishuai.distribute.controller;

import com.daishuai.distribute.lock.DistributeLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Daishuai
 * @date 2020/9/21 13:19
 */
@RestController
public class DistributeLockController {

    @Autowired
    private DistributeLock redisDistributeLock;

    @GetMapping(value = "/lock")
    public boolean getLock(String lockKey) {
        redisDistributeLock.lock(lockKey);
        return redisDistributeLock.releaseLock(lockKey);
    }

}
