package com.sporty.settlement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// This is a Kafka message payload, not a DB entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventOutcome {

    private String eventId;

    // Actual result of the event, e.g. "TEAM_A_WIN", "DRAW", "TEAM_B_WIN"
    private String outcome;

    private LocalDateTime resolvedAt;
}
