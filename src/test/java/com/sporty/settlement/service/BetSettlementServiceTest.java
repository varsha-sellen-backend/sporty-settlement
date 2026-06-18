package com.sporty.settlement.service;

import com.sporty.settlement.model.Bet;
import com.sporty.settlement.model.BetStatus;
import com.sporty.settlement.model.EventOutcome;
import com.sporty.settlement.repository.BetRepository;
import com.sporty.settlement.rocketmq.BetSettlementProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BetSettlementServiceTest {

    @Mock
    private BetRepository betRepository;

    @Mock
    private BetSettlementProducer betSettlementProducer;

    @InjectMocks
    private BetSettlementService betSettlementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldMarkBetAsWonWhenPredictionMatches() {
        Bet bet = buildBet("TEAM_A_WIN");
        EventOutcome outcome = new EventOutcome("EVT-001", "TEAM_A_WIN", LocalDateTime.now());

        when(betRepository.findByEventIdAndStatus("EVT-001", BetStatus.PENDING))
                .thenReturn(List.of(bet));

        betSettlementService.settle(outcome);

        assert bet.getStatus() == BetStatus.WON;
        verify(betRepository, times(1)).save(bet);
        verify(betSettlementProducer, times(1)).send(any());
    }

    @Test
    void shouldMarkBetAsLostWhenPredictionDoesNotMatch() {
        Bet bet = buildBet("DRAW");
        EventOutcome outcome = new EventOutcome("EVT-001", "TEAM_A_WIN", LocalDateTime.now());

        when(betRepository.findByEventIdAndStatus("EVT-001", BetStatus.PENDING))
                .thenReturn(List.of(bet));

        betSettlementService.settle(outcome);

        assert bet.getStatus() == BetStatus.LOST;
        verify(betRepository, times(1)).save(bet);
    }

    @Test
    void shouldDoNothingWhenNoPendingBetsExist() {
        EventOutcome outcome = new EventOutcome("EVT-999", "TEAM_A_WIN", LocalDateTime.now());

        when(betRepository.findByEventIdAndStatus("EVT-999", BetStatus.PENDING))
                .thenReturn(List.of());

        betSettlementService.settle(outcome);

        verify(betRepository, never()).save(any());
        verify(betSettlementProducer, never()).send(any());
    }

    private Bet buildBet(String predictedOutcome) {
        return Bet.builder()
                .id(1L)
                .eventId("EVT-001")
                .userId("user-101")
                .predictedOutcome(predictedOutcome)
                .status(BetStatus.PENDING)
                .amount(50.0)
                .placedAt(LocalDateTime.now())
                .build();
    }
}
