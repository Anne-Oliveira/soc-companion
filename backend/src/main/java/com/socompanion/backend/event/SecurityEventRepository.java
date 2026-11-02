package com.socompanion.backend.event;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SecurityEventRepository extends JpaRepository<SecurityEvent, UUID> { }
