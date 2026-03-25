package com.alarm.signal.user.services.impl;

import com.alarm.signal.common.exception.ServiceException;
import com.alarm.signal.user.dto.request.CreateProducerRequest;
import com.alarm.signal.user.dto.response.ProducerResponse;
import com.alarm.signal.user.services.ProducerService;
import com.alarm.signal.user.repository.ProducerRepository;
import com.alarm.signal.user.repository.UserRepository;
import com.alarm.signal.user.mapper.ProducerMapper;
import com.alarm.signal.user.model.Producer;
import com.alarm.signal.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {
    private final ProducerRepository producerRepository;
    private final UserRepository userRepository;
    private final ProducerMapper producerMapper;

    @Override
    public ProducerResponse createProducer(CreateProducerRequest request) {
        // 1. Validate request
        if (request == null ||
                request.getUserId() == null ||
                request.getName() == null ||
                request.getName().isBlank()) {
            throw new ServiceException("Invalid producer creation request");
        }
        // 2. Ensure user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ServiceException("User does not exist"));
        // 3. Ensure producer does not already exist for this user (idempotent)
        if (producerRepository.existsByUserId(request.getUserId())) {
            throw new ServiceException("Producer already exists for this user");
        }
        // 4. Map request to entity
        Producer producer = producerMapper.toEntity(request);
        producer.setUserId(request.getUserId());
        // 5. Save producer to DB (handle race condition via DB constraint)
        try {
            producer = producerRepository.save(producer);
        } catch (Exception e) {
            throw new ServiceException("Producer already exists (race condition)");
        }
        // 6. Return ProducerResponse
        return producerMapper.toResponse(producer);
    }

    @Override
    public Optional<ProducerResponse> getByUserId(UUID userId) {
        // 1. Validate input
        if (userId == null) {
            return Optional.empty();
        }
        // 2. Fetch producer by userId
        return producerRepository.findByUserId(userId)
                .map(producerMapper::toResponse);
    }

    @Override
    public Optional<ProducerResponse> getById(UUID id) {
        // 1. Validate input
        if (id == null) {
            return Optional.empty();
        }
        // 2. Fetch producer by id
        return producerRepository.findById(id)
                .map(producerMapper::toResponse);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        // 1. Validate input
        if (userId == null) {
            return false;
        }
        // 2. Fast lookup (indexed column recommended)
        return producerRepository.existsByUserId(userId);
    }
}
