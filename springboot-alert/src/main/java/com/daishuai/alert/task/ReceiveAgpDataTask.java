package com.daishuai.alert.task;

import com.alibaba.fastjson.JSON;
import com.daishuai.alert.service.IAlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Daishuai
 * @description
 * @date 2019/11/18 11:16
 */
@Slf4j
@Component
public class ReceiveAgpDataTask {
    
    @Autowired
    private IAlertService iAlertService;
    
    @Scheduled(cron = "0/10 * * * * *")
    public void receiveAgpDataTask() {
        String agpData = iAlertService.receiveAGPData();
        log.info("空呼数据：{}", agpData);
    }
}
