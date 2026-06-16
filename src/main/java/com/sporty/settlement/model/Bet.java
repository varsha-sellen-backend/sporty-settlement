package com.sporty.settlement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Links this bet to an incoming EventOutcome
    private String eventId;

    private String userId;

    // What the user predicted, e.g. "TEAM_A_WIN"
    private String predictedOutcome;

    @Enumerated(EnumType.STRING)
    private BetStatus status;

    private Double amount;

    private LocalDateTime placedAt;
}
