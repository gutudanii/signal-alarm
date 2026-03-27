package com.alarm.signal.user.services;

import com.alarm.signal.user.dto.request.CreateProducerRequest;
import com.alarm.signal.user.dto.response.ProducerResponse;
import com.alarm.signal.user.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface ProducerService {
    ProducerResponse createProducer(CreateProducerRequest request);
    Optional<ProducerResponse> getByUserId(UUID userId);
    Optional<ProducerResponse> getById(UUID id);
    boolean existsByUserId(UUID userId);
    @Transactional
    ProducerResponse createProducerForAuthenticatedUser(User user);
}
