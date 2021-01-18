package com.daishuai.stomp.service;

import com.daishuai.stomp.config.InstantTask;
import com.daishuai.stomp.dto.InstantRequest;

/**
 * @author Daishuai
 * @date 2021/1/14 19:15
 */
public interface InstantSocketTaskService {

    /**
     * 开始一个任务
     *
     * @param taskName
     * @param task
     * @param initialDelay
     * @param delay
     */
    void startInstantTask(String taskName, InstantTask task, long initialDelay, long delay);

    /**
     * 结束一个任务
     *
     * @param taskNames
     */
    void stopInstantTask(String ... taskNames);

    /**
     * 加入定时任务
     *
     * @param clientId
     * @param subscribeId
     * @param businessType
     * @param instantRequest
     */
    void joinInScheduledTask(String clientId, String subscribeId, String businessType, InstantRequest instantRequest);

    /**
     * 立即触发一次
     *
     * @param socketRequest
     * @param clientIds
     */
    void startAtOnce(InstantRequest socketRequest, String ... clientIds);

    /**
     * 任务中移除指定
     *
     * @param clientId
     * @param subscribeId
     */
    void removeFromScheduledTask(String clientId, String subscribeId);

    /**
     * 任务中移除
     *
     * @param clientId
     */
    void removeAllFromScheduledTask(String clientId);
}
