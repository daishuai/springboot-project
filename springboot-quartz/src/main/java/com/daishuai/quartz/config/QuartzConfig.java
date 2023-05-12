package com.daishuai.quartz.config;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description 注入调度工厂
 * @createTime 2023年05月12日 09:40:00
 */
@Configuration
public class QuartzConfig {

    @Resource
    private QuartzJobFactory quartzJobFactory;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        // 获取配置属性
        PropertiesFactoryBean propertiesFactory = new PropertiesFactoryBean();
        propertiesFactory.setLocation(new ClassPathResource("/quartz.properties"));
        // 在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactory.afterPropertiesSet();
        // 创建SchedulerFactoryBean
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setQuartzProperties(Objects.requireNonNull(propertiesFactory.getObject()));
        // 支持在JOB实例中注入其他的业务对象
        factory.setJobFactory(quartzJobFactory);
        factory.setApplicationContextSchedulerContextKey("applicationContextKey");
        // 当Spring关闭时,会等待所有已启动的quartz job结束后spring才能完全shutdown
        factory.setWaitForJobsToCompleteOnShutdown(true);
        // 是否覆盖已存在的Job
        factory.setOverwriteExistingJobs(true);
        // QuartzScheduler延时启动,应用启动完后QuartzScheduler再启动
        factory.setStartupDelay(10);
        return factory;
    }

    @Bean(name = "scheduler")
    public Scheduler scheduler() throws IOException {
        return schedulerFactoryBean().getScheduler();
    }
}
