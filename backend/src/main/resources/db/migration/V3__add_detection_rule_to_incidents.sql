ALTER TABLE incidents
    ADD COLUMN detection_rule VARCHAR(50) NOT NULL DEFAULT 'MANUAL';

CREATE INDEX idx_incidents_rule_ip ON incidents (detection_rule, source_ip);
