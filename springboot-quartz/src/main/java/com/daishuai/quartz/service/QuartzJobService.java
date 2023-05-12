package com.daishuai.quartz.service;

import java.util.Map;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description 定时任务
 * @createTime 2023年05月12日 10:03:00
 */
public interface QuartzJobService {

    /**
     * 添加任务可以传参数
     *
     * @param clazzName 任务类
     * @param jobName   任务名称
     * @param groupName 任务组
     * @param cronExp   执行频率
     * @param param 任务参数
     */
    void addJob(String clazzName, String jobName, String groupName, String cronExp, Map<String, Object> param);

    /**
     * 暂停任务
     *
     * @param jobName   任务名称
     * @param groupName 任务组
     */
    void pauseJob(String jobName, String groupName);

    /**
     * 恢复任务
     *
     * @param jobName   任务名称
     * @param groupName 任务组
     */
    void resumeJob(String jobName, String groupName);

    /**
     * 立即运行一次定时任务
     *
     * @param jobName   任务名称
     * @param groupName 任务组
     */
    void runOnce(String jobName, String groupName);

    /**
     * 更新任务
     *
     * @param jobName   任务名称
     * @param groupName 任务组
     * @param cronExp   执行频率
     * @param param 任务参数
     */
    void updateJob(String jobName, String groupName, String cronExp, Map<String, Object> param);

    /**
     * 删除任务
     *
     * @param jobName   任务名称
     * @param groupName 任务组
     */
    void deleteJob(String jobName, String groupName);

    /**
     * 启动所有任务
     */
    void startAllJobs();

    /**
     * 暂停所有任务
     */
    void pauseAllJobs();

    /**
     * 恢复所有任务
     */
    void resumeAllJobs();

    /**
     * 关闭所有任务
     */
    void shutdownAllJobs();
}
