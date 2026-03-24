package com.alarm.signal.user.repository;

import com.alarm.signal.user.model.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ProducerRepository extends JpaRepository<Producer, UUID> {
    Optional<Producer> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
}
