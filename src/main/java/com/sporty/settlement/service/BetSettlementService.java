package com.sporty.settlement.service;

import com.sporty.settlement.model.Bet;
import com.sporty.settlement.model.BetSettlementResult;
import com.sporty.settlement.model.BetStatus;
import com.sporty.settlement.model.EventOutcome;
import com.sporty.settlement.repository.BetRepository;
import com.sporty.settlement.rocketmq.BetSettlementProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BetSettlementService {

    private final BetRepository betRepository;
    private final BetSettlementProducer betSettlementProducer;

    @Transactional
    public void settle(EventOutcome eventOutcome) {
        List<Bet> pendingBets = betRepository.findByEventIdAndStatus(
                eventOutcome.getEventId(), BetStatus.PENDING);

        if (pendingBets.isEmpty()) {
            log.info("No pending bets found for eventId={}", eventOutcome.getEventId());
            return;
        }

        log.info("Settling {} bets for eventId={}", pendingBets.size(), eventOutcome.getEventId());

        for (Bet bet : pendingBets) {
            BetStatus result = resolveBet(bet, eventOutcome.getOutcome());
            bet.setStatus(result);
            betRepository.save(bet);

            log.info("Bet id={} userId={} settled as {}", bet.getId(), bet.getUserId(), result);

            // Notify downstream via RocketMQ (mocked)
            betSettlementProducer.send(new BetSettlementResult(
                    bet.getId(),
                    bet.getUserId(),
                    bet.getEventId(),
                    result,
                    bet.getAmount()
            ));
        }
    }

    private BetStatus resolveBet(Bet bet, String actualOutcome) {
        return bet.getPredictedOutcome().equalsIgnoreCase(actualOutcome)
                ? BetStatus.WON
                : BetStatus.LOST;
    }
}
