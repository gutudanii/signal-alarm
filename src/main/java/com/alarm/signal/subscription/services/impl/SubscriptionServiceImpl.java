package com.alarm.signal.subscription.services.impl;

import com.alarm.signal.subscription.dto.SubscriptionCreateRequest;
import com.alarm.signal.subscription.dto.SubscriptionDto;
import com.alarm.signal.subscription.dto.SubscriptionUpdateRequest;
import com.alarm.signal.subscription.mapper.SubscriptionMapper;
import com.alarm.signal.subscription.model.Subscription;
import com.alarm.signal.subscription.respository.SubscriptionRepository;
import com.alarm.signal.subscription.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public Optional<SubscriptionDto> getSubscription(UUID userId, UUID producerId) {
        return subscriptionRepository.findByUserIdAndProducerId(userId, producerId)
                .map(subscriptionMapper::toDto);
    }

    @Override
    public Optional<SubscriptionDto> getSubscription(UUID id) {
        return subscriptionRepository.findById(id)
                .map(subscriptionMapper::toDto);
    }

    @Override
    public List<SubscriptionDto> getSubscriptionsByUser(UUID userId) {
        return subscriptionRepository.findAllByUserId(userId)
                .stream().map(subscriptionMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<SubscriptionDto> getSubscriptionsByProducer(UUID producerId) {
        return subscriptionRepository.findAllByProducerId(producerId)
                .stream().map(subscriptionMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<SubscriptionDto> getActiveUnmutedSubscribers(UUID producerId) {
        return subscriptionRepository.findAllByProducerIdAndActiveTrueAndMutedFalse(producerId)
                .stream().map(subscriptionMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public SubscriptionDto subscribe(SubscriptionCreateRequest request) {
        Subscription subscription = Subscription.builder()
                .userId(request.getUserId())
                .producerId(request.getProducerId())
                .active(true)
                .muted(false)
                .priorityFilter(request.getPriorityFilter())
                .build();
        return subscriptionMapper.toDto(subscriptionRepository.save(subscription));
    }

    @Override
    public void unsubscribe(UUID userId, UUID producerId) {
        subscriptionRepository.findByUserIdAndProducerId(userId, producerId)
                .ifPresent(subscriptionRepository::delete);
    }

    @Override
    public SubscriptionDto updateSubscription(SubscriptionUpdateRequest updateRequest) {
        Subscription subscription = subscriptionRepository.findById(updateRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));
        if (updateRequest.getMuted() != null) {
            subscription.setMuted(updateRequest.getMuted());
        }
        if (updateRequest.getPriorityFilter() != null) {
            subscription.setPriorityFilter(updateRequest.getPriorityFilter());
        }
        if (updateRequest.getActive() != null) {
            subscription.setActive(updateRequest.getActive());
        }
        return subscriptionMapper.toDto(subscriptionRepository.save(subscription));
    }

    @Override
    public SubscriptionDto muteProducer(UUID userId, UUID producerId, boolean muted) {
        Subscription subscription = subscriptionRepository.findByUserIdAndProducerId(userId, producerId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));
        subscription.setMuted(muted);
        return subscriptionMapper.toDto(subscriptionRepository.save(subscription));
    }
}
