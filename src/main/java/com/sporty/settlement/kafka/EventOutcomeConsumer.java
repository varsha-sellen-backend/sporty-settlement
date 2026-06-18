package com.sporty.settlement.kafka;

import com.sporty.settlement.model.EventOutcome;
import com.sporty.settlement.service.BetSettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventOutcomeConsumer {

    private final BetSettlementService betSettlementService;

    @KafkaListener(
            topics = "event-outcomes",
            groupId = "bet-settlement-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(EventOutcome eventOutcome) {
        log.info("Received event outcome from Kafka: eventId={}, outcome={}",
                eventOutcome.getEventId(), eventOutcome.getOutcome());

        try {
            betSettlementService.settle(eventOutcome);
        } catch (Exception e) {
            // In production we'd use a dead-letter topic here
            log.error("Failed to process event outcome for eventId={}: {}",
                    eventOutcome.getEventId(), e.getMessage(), e);
        }
    }
}
