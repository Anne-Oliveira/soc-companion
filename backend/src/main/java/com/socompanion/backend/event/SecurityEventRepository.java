package com.socompanion.backend.event;

import java.time.Instant;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityEventRepository extends JpaRepository<SecurityEvent, UUID> {

    long countByEventTypeAndSourceIpAndOccurredAtBetween(
            EventType eventType, String sourceIp, Instant from, Instant to);
}
