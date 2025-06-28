package com.example.evostyle.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendWithCallBack(String topic, String partition, String payload){
        kafkaTemplate.send(topic, partition, payload).
                whenComplete((result, ex) -> {
                    if(ex == null){
                        log.info("kafka event 발행 성공 -> topic:{}", result.getProducerRecord().topic());
                    }else {
                        log.error("kafka event 발행 실패 -> topic:{}, value:{}",
                                result.getProducerRecord().topic(), result.getProducerRecord().value());
                    }
                });
    }
}
