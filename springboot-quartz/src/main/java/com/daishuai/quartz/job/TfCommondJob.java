package com.daishuai.quartz.job;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.*;

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
        JobDetail jobDetail = context.getJobDetail();
        log.info("{} -- {}", jobDetail.getKey().getName(), DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        JobDataMap mergedJobDataMap = context.getMergedJobDataMap();
        log.info("dataMap: {}", JSON.toJSONString(mergedJobDataMap));
    }
}
