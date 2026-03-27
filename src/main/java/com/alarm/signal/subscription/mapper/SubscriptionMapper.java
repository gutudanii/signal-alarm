package com.alarm.signal.subscription.mapper;

import com.alarm.signal.subscription.model.Subscription;
import com.alarm.signal.subscription.dto.SubscriptionDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {

    public SubscriptionDto toDto(Subscription entity) {
        if (entity == null) return null;

        return SubscriptionDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .producerId(entity.getProducerId())
                .active(entity.isActive())
                .muted(entity.isMuted())
                .priorityFilter(entity.getPriorityFilter())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

