package com.sporty.settlement.rocketmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.settlement.model.BetSettlementResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * RocketMQ producer — mocked for this assignment.
 *
 * In production this would use RocketMQ Spring Boot Starter and publish
 * to a real "bet-settlements" topic. For now we log the payload to keep
 * the project runnable without a RocketMQ broker.
 *
 * Design decision: mocking at the component level (not via a flag or interface)
 * keeps the code readable and the integration point obvious to any reviewer.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BetSettlementProducer {

    private final ObjectMapper objectMapper;

    public void send(BetSettlementResult result) {
        try {
            String payload = objectMapper.writeValueAsString(result);
            // TODO: replace with real RocketMQ send when broker is available
            log.info("[RocketMQ MOCK] Sending to bet-settlements: {}", payload);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize BetSettlementResult for betId={}", result.getBetId(), e);
        }
    }
}
