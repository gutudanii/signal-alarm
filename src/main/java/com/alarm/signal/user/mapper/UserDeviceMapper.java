package com.alarm.signal.user.mapper;

import com.alarm.signal.user.dto.request.RegisterDeviceRequest;
import com.alarm.signal.user.dto.response.UserDeviceResponse;
import com.alarm.signal.user.model.UserDevice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDeviceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    UserDevice toEntity(RegisterDeviceRequest dto);

    default String safe(String value) {
        return value == null ? "" : value;
    }

    default UserDeviceResponse toResponse(UserDevice entity) {
        if (entity == null) return null;
        return UserDeviceResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .fcmToken(safe(entity.getFcmToken()))
                .platform(entity.getPlatform())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
