CREATE TABLE security_events (
    id          UUID PRIMARY KEY,
    event_type  VARCHAR(50)  NOT NULL,
    source_ip   VARCHAR(45),
    username    VARCHAR(100),
    message     VARCHAR(500),
    source      VARCHAR(100) NOT NULL,
    occurred_at TIMESTAMPTZ  NOT NULL,
    received_at TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_events_ip_type_time
    ON security_events (source_ip, event_type, occurred_at);
