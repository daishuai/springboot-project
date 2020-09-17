package com.daishuai.stomp.controller;

import com.daishuai.stomp.dto.RequestMessage;
import com.daishuai.stomp.dto.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author Daishuai
 * @date 2020/7/29 10:11
 */
@Slf4j
@RestController
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping(value = "/welcome")
    @SendTo(value = "/topic/say")
    public ResponseMessage say(RequestMessage requestMessage) {
        log.info("RequestMessage: {}", requestMessage);
        return new ResponseMessage();
    }

    @Scheduled(fixedRate = 1000)
    public void callback() {
        simpMessagingTemplate.convertAndSend("/topic/callback", "定时推送时间:" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }
}
