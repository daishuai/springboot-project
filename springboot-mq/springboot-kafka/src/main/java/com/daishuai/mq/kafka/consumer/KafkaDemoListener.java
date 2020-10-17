package com.daishuai.mq.kafka.consumer;

import com.daishuai.mq.kafka.constant.KafkaConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Daishuai
 * @date 2020/10/17 20:43
 */
@Slf4j
@Component
public class KafkaDemoListener {

    @KafkaListener(topics = KafkaConstant.KAFKA_DEMO_TOPIC)
    public void demo(String message) {
        log.info("pull a message: {}", message);
    }
}
