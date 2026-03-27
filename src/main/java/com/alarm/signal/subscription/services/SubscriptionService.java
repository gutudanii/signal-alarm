package com.alarm.signal.subscription.services;

import com.alarm.signal.subscription.dto.SubscriptionCreateRequest;
import com.alarm.signal.subscription.dto.SubscriptionDto;
import com.alarm.signal.subscription.dto.SubscriptionUpdateRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionService {
    Optional<SubscriptionDto> getSubscription(UUID userId, UUID producerId);
    Optional<SubscriptionDto> getSubscription(UUID id);
    List<SubscriptionDto> getSubscriptionsByUser(UUID userId);
    List<SubscriptionDto> getSubscriptionsByProducer(UUID producerId);
    List<SubscriptionDto> getActiveUnmutedSubscribers(UUID producerId);
    SubscriptionDto subscribe(SubscriptionCreateRequest request);
    void unsubscribe(UUID userId, UUID producerId);
    SubscriptionDto updateSubscription(SubscriptionUpdateRequest updateRequest);
    SubscriptionDto muteProducer(UUID userId, UUID producerId, boolean muted);
}
