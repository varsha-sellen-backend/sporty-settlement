package com.sporty.settlement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Payload sent to RocketMQ after a bet is settled
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BetSettlementResult {

    private Long betId;
    private String userId;
    private String eventId;
    private BetStatus result;
    private Double amount;
}
