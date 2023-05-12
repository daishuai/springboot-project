package com.daishuai.quartz.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description Quartz任务工厂
 * @createTime 2023年05月12日 09:18:00
 */
@Component
public class QuartzJobFactory extends AdaptableJobFactory {


    @Resource
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        // 调用父类的方法
        Object jobInstance = super.createJobInstance(bundle);
        // 进行注入
        autowireCapableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
