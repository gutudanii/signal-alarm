package com.alarm.signal.user.mapper;

import com.alarm.signal.user.dto.request.CreateUserRequest;
import com.alarm.signal.user.dto.response.UserResponse;
import com.alarm.signal.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "password", ignore = true) // password set separately after hashing
    User toEntity(CreateUserRequest dto);

    UserResponse toResponse(User user);
}

