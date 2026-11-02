package com.socompanion.backend.incident;

import java.util.Collection;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentRepository extends JpaRepository<Incident, UUID> {

    boolean existsByDetectionRuleAndSourceIpAndStatusIn(
            String detectionRule, String sourceIp, Collection<IncidentStatus> statuses);
}
