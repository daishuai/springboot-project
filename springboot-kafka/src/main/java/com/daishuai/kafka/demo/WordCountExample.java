package com.daishuai.kafka.demo;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;


/**
 * @author Daishuai
 * @version 1.0.0
 * @description Kafka流处理-字数统计
 * @createTime 2023年05月25日 19:36:00
 */
public class WordCountExample {

    public static void main(String[] args) throws InterruptedException {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordCount");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "10.165.77.64:19195,10.165.77.65:19195,10.165.77.66:19195");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> stream = builder.stream("wordCount-input");
        Pattern pattern = Pattern.compile("\\W+");
        KStream<String, String> count = stream.flatMapValues(value -> Arrays.asList(pattern.split(value.toLowerCase())))
                .map((key, value) -> new KeyValue<>(value, value))
                .filter((key, value) -> !value.equals("the"))
                .groupByKey()
                .count()
                .mapValues(value -> Long.toString(value))
                .toStream();
        count.to("wordCount-output");
        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), props);
        kafkaStreams.cleanUp();;
        kafkaStreams.start();
        //Thread.sleep(5000L);
        //kafkaStreams.close();
    }
}
