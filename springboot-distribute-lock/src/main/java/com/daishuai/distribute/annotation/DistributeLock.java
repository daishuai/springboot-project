package com.daishuai.distribute.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author Daishuai
 * @date 2020/9/21 17:19
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
public @interface DistributeLock {

    /**
     * 锁的资源
     *
     * @return
     */
    @AliasFor(value = "key")
    String value() default "";

    /**
     * 锁的资源
     *
     * @return
     */
    @AliasFor(value = "value")
    String key() default "";

    /**
     * 锁的有效期
     *
     * @return
     */
    long expireMillis() default 30000;

    /**
     * 重试的间隔时间，设置GIVE_UP时忽略
     *
     * @return
     */
    long sleepMillis() default 200;

    /**
     * 重试次数
     *
     * @return
     */
    int retryTimes() default 5;

    /**
     * 获取锁失败时，放弃Or重试
     *
     * @return
     */
    LockFailAction action() default LockFailAction.RETRY;

    enum LockFailAction {

        /**
         * 放弃获取锁
         */
        GIVE_UP,

        /**
         * 重试
         */
        RETRY;
    }
}
