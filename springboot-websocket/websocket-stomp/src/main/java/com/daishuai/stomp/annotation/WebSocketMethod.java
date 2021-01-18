package com.daishuai.stomp.annotation;

import java.lang.annotation.*;

/**
 * @author Daishuai
 * @date 2021/1/15 9:47
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebSocketMethod {

    /**
     * 业务类型
     *
     * @return 业务类型
     */
    String businessType();

    /**
     * 任务名称
     *
     * @return 任务名称
     */
    String taskName();

    /**
     * 任务执行频率 单位秒
     *
     * @return 任务执行频率 单位秒
     */
    long intervalSeconds();
}
