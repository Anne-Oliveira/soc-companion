CREATE TABLE incidents (
    id          UUID PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    description TEXT,
    severity    VARCHAR(20)  NOT NULL,
    status      VARCHAR(20)  NOT NULL,
    source_ip   VARCHAR(45),
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_incidents_status   ON incidents (status);
CREATE INDEX idx_incidents_severity ON incidents (severity);
