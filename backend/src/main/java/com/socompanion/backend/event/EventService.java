package com.socompanion.backend.event;

import com.socompanion.backend.detection.DetectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    private final SecurityEventRepository repository;
    private final DetectionService detection;

    public EventService(SecurityEventRepository repository, DetectionService detection) {
        this.repository = repository;
        this.detection = detection;
    }

    public SecurityEvent ingest(SecurityEventRequest req) {
        SecurityEvent saved = repository.save(new SecurityEvent(
                req.eventType(), req.sourceIp(), req.username(),
                req.message(), req.source(), req.occurredAt()));
        try {
            detection.evaluate(saved);
        } catch (RuntimeException ex) {
            log.error("Falha na detecção para o evento {}", saved.getId(), ex);
        }
        return saved;
    }
}
