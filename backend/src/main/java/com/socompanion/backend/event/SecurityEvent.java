package com.socompanion.backend.event;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "security_events")
public class SecurityEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 50)
    private EventType eventType;

    @Column(name = "source_ip", length = 45)
    private String sourceIp;

    @Column(length = 100)
    private String username;

    @Column(length = 500)
    private String message;

    @Column(nullable = false, length = 100)
    private String source;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name = "received_at", nullable = false, updatable = false)
    private Instant receivedAt;

    protected SecurityEvent() { }

    public SecurityEvent(EventType eventType, String sourceIp, String username,
                         String message, String source, Instant occurredAt) {
        this.eventType = eventType;
        this.sourceIp = sourceIp;
        this.username = username;
        this.message = message;
        this.source = source;
        this.occurredAt = occurredAt;
    }

    @PrePersist
    void onCreate() {
        this.receivedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public EventType getEventType() { return eventType; }
    public String getSourceIp() { return sourceIp; }
    public String getUsername() { return username; }
    public String getMessage() { return message; }
    public String getSource() { return source; }
    public Instant getOccurredAt() { return occurredAt; }
    public Instant getReceivedAt() { return receivedAt; }
}
