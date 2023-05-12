package com.daishuai.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.Date;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description 测试Job
 * @createTime 2023年05月12日 09:52:00
 */
@Slf4j
@DisallowConcurrentExecution
public class TfCommondJob implements Job {
    @Override
    public void execute(JobExecutionContext context) {
        log.info("{} -- {}", context.getJobDetail().getKey().getName(), DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }
}
