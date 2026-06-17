package com.sporty.settlement.kafka;

import com.sporty.settlement.config.KafkaConfig;
import com.sporty.settlement.model.EventOutcome;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventOutcomeProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(EventOutcome eventOutcome) {
        log.info("Publishing event outcome to Kafka: eventId={}, outcome={}",
                eventOutcome.getEventId(), eventOutcome.getOutcome());

        kafkaTemplate.send(KafkaConfig.EVENT_OUTCOMES_TOPIC, eventOutcome.getEventId(), eventOutcome);
    }
}
