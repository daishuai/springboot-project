package com.daishuai.alert.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

/**
 * @author Daishuai
 * @description
 * @date 2019/11/26 14:56
 */
@Slf4j
@Component
public class GatewayStarter implements ApplicationRunner {
    
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        String path = ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX).getPath();
        path = path.substring(1);
        log.info(path);
        Runtime.getRuntime().exec(path + "gw/alertgw.exe");
    }
}
