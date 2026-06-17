package com.sporty.settlement.api;

import com.sporty.settlement.kafka.EventOutcomeProducer;
import com.sporty.settlement.model.EventOutcome;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventOutcomeController {

    private final EventOutcomeProducer eventOutcomeProducer;

    /**
     * POST /api/events/outcome
     *
     * Publishes a sport event outcome to Kafka.
     * The Kafka consumer then picks it up and settles all matching pending bets.
     *
     * Example body:
     * {
     *   "eventId": "EVT-001",
     *   "outcome": "TEAM_A_WIN",
     *   "resolvedAt": "2024-06-10T15:00:00"
     * }
     */
    @PostMapping("/outcome")
    public ResponseEntity<String> publishOutcome(@RequestBody EventOutcome eventOutcome) {
        if (eventOutcome.getResolvedAt() == null) {
            eventOutcome.setResolvedAt(LocalDateTime.now());
        }

        log.info("Received outcome request for eventId={}", eventOutcome.getEventId());
        eventOutcomeProducer.publish(eventOutcome);

        return ResponseEntity.accepted()
                .body("Event outcome published for eventId: " + eventOutcome.getEventId());
    }

    /**
     * GET /api/events/health
     * Quick sanity check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
