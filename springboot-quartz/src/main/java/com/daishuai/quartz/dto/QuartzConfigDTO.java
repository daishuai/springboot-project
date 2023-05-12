package com.daishuai.quartz.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description Quartz任务
 * @createTime 2023年05月12日 10:08:00
 */
@Data
public class QuartzConfigDTO {

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务所属组
     */
    private String groupName;

    /**
     * 任务执行类
     */
    private String jobClass;

    /**
     * 任务调度时间表达式
     */
    private String cronExpression;

    /**
     * 附加参数
     */
    private Map<String, Object> param;
}
