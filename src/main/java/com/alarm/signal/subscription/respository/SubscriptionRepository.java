package com.alarm.signal.subscription.respository;

import com.alarm.signal.subscription.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Optional<Subscription> findByUserIdAndProducerId(UUID userId, UUID producerId);
    List<Subscription> findAllByUserId(UUID userId);
    List<Subscription> findAllByProducerId(UUID producerId);
    List<Subscription> findAllByProducerIdAndActiveTrueAndMutedFalse(UUID producerId);
}
