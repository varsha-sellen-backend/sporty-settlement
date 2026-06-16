package com.sporty.settlement.repository;

import com.sporty.settlement.model.Bet;
import com.sporty.settlement.model.BetStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Long> {

    // Fetch all pending bets for a given event — used during settlement
    List<Bet> findByEventIdAndStatus(String eventId, BetStatus status);
}
