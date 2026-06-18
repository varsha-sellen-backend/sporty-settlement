package com.sporty.settlement.rocketmq;

import com.sporty.settlement.model.BetSettlementResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * RocketMQ consumer — mocked for this assignment.
 *
 * In production this would be annotated with @RocketMQMessageListener
 * and react to messages on the "bet-settlements" topic (e.g. triggering
 * payout processing, notifications, or audit logging).
 */
@Slf4j
@Component
public class BetSettlementConsumer {

    // Called directly from BetSettlementProducer mock to simulate end-to-end flow
    public void onMessage(BetSettlementResult result) {
        log.info("[RocketMQ MOCK] Received settlement: betId={} userId={} result={} amount={}",
                result.getBetId(), result.getUserId(), result.getResult(), result.getAmount());

        // In production: trigger payout, send notification, update audit log, etc.
    }
}
