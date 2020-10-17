package com.daishuai.mq.kafka.producer;

import com.daishuai.mq.kafka.constant.KafkaConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * @author Daishuai
 * @date 2020/10/17 20:44
 */
@Component
public class KafkaDemoProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(cron = "0/10 * * * * ?")
    public void demoTask() {
        kafkaTemplate.send(KafkaConstant.KAFKA_DEMO_TOPIC, "producer send message " + LocalTime.now().toString());
    }
}
