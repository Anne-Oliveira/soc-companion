package com.socompanion.backend.event;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    private final SecurityEventRepository repository;

    public EventService(SecurityEventRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public SecurityEvent ingest(SecurityEventRequest req) {
        return repository.save(new SecurityEvent(
                req.eventType(), req.sourceIp(), req.username(),
                req.message(), req.source(), req.occurredAt()));
    }
}
