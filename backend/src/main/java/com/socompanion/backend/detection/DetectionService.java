package com.socompanion.backend.detection;

import com.socompanion.backend.event.EventType;
import com.socompanion.backend.event.SecurityEvent;
import com.socompanion.backend.event.SecurityEventRepository;
import com.socompanion.backend.incident.Incident;
import com.socompanion.backend.incident.IncidentRepository;
import com.socompanion.backend.incident.IncidentStatus;
import com.socompanion.backend.incident.Severity;
import java.time.Duration;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DetectionService {

    private static final Logger log = LoggerFactory.getLogger(DetectionService.class);

    static final String BRUTE_FORCE = "BRUTE_FORCE";
    static final String SUCCESS_AFTER_FAILURES = "SUCCESS_AFTER_FAILURES";

    private static final int BRUTE_FORCE_THRESHOLD = 5;
    private static final Duration BRUTE_FORCE_WINDOW = Duration.ofMinutes(5);
    private static final int SUCCESS_AFTER_FAILURES_THRESHOLD = 3;
    private static final Duration SUCCESS_AFTER_FAILURES_WINDOW = Duration.ofMinutes(10);

    private static final Set<IncidentStatus> OPEN_STATUSES =
            EnumSet.of(IncidentStatus.NEW, IncidentStatus.ACKNOWLEDGED, IncidentStatus.ESCALATED);

    private final SecurityEventRepository events;
    private final IncidentRepository incidents;

    public DetectionService(SecurityEventRepository events, IncidentRepository incidents) {
        this.events = events;
        this.incidents = incidents;
    }

    @Transactional
    public void evaluate(SecurityEvent event) {
        if (event.getSourceIp() == null) {
            return;
        }
        switch (event.getEventType()) {
            case LOGIN_FAILED -> checkBruteForce(event);
            case LOGIN_SUCCESS -> checkSuccessAfterFailures(event);
            default -> { }
        }
    }

    private void checkBruteForce(SecurityEvent event) {
        String ip = event.getSourceIp();
        long failures = countFailures(ip, event.getOccurredAt(), BRUTE_FORCE_WINDOW);
        if (failures >= BRUTE_FORCE_THRESHOLD) {
            openIncident(BRUTE_FORCE, Severity.HIGH, ip,
                    "Possível brute force a partir de " + ip,
                    failures + " falhas de login em " + BRUTE_FORCE_WINDOW.toMinutes()
                            + " minutos. Último usuário alvo: " + event.getUsername());
        }
    }

    private void checkSuccessAfterFailures(SecurityEvent event) {
        String ip = event.getSourceIp();
        long failures = countFailures(ip, event.getOccurredAt(), SUCCESS_AFTER_FAILURES_WINDOW);
        if (failures >= SUCCESS_AFTER_FAILURES_THRESHOLD) {
            openIncident(SUCCESS_AFTER_FAILURES, Severity.CRITICAL, ip,
                    "Login bem-sucedido após falhas a partir de " + ip,
                    "Login bem-sucedido do usuário " + event.getUsername() + " depois de "
                            + failures + " falhas em "
                            + SUCCESS_AFTER_FAILURES_WINDOW.toMinutes() + " minutos.");
        }
    }

    private long countFailures(String ip, Instant until, Duration window) {
        return events.countByEventTypeAndSourceIpAndOccurredAtBetween(
                EventType.LOGIN_FAILED, ip, until.minus(window), until);
    }

    private void openIncident(String rule, Severity severity, String ip,
                              String title, String description) {
        if (incidents.existsByDetectionRuleAndSourceIpAndStatusIn(rule, ip, OPEN_STATUSES)) {
            return;
        }
        incidents.save(new Incident(title, description, severity, ip, rule));
        log.warn("Incidente aberto: regra={} severidade={}", rule, severity);
    }
}
