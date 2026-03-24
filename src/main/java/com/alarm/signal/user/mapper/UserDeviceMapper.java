package com.alarm.signal.user.mapper;

import com.alarm.signal.user.dto.request.RegisterDeviceRequest;
import com.alarm.signal.user.dto.response.UserDeviceResponse;
import com.alarm.signal.user.model.UserDevice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserDeviceMapper {
    UserDeviceMapper INSTANCE = Mappers.getMapper(UserDeviceMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    UserDevice toEntity(RegisterDeviceRequest dto);

    UserDeviceResponse toResponse(UserDevice entity);
}

