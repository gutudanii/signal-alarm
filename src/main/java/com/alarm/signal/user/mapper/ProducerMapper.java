package com.alarm.signal.user.mapper;

import com.alarm.signal.user.dto.request.CreateProducerRequest;
import com.alarm.signal.user.dto.response.ProducerResponse;
import com.alarm.signal.user.model.Producer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProducerMapper {
    ProducerMapper INSTANCE = Mappers.getMapper(ProducerMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Producer toEntity(CreateProducerRequest dto);

    default String safe(String value) {
        return value == null ? "" : value;
    }

    default ProducerResponse toResponse(Producer entity) {
        if (entity == null) return null;
        return ProducerResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .name(safe(entity.getName()))
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
