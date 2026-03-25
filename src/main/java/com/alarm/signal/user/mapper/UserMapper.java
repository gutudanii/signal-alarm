package com.alarm.signal.user.mapper;

import com.alarm.signal.user.dto.request.CreateUserRequest;
import com.alarm.signal.user.dto.response.UserResponse;
import com.alarm.signal.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "password", ignore = true) // password set separately after hashing
    @Mapping(target = "emailVerified", source = "emailVerified")
    User toEntity(CreateUserRequest dto);

    default String safe(String value) {
        return value == null ? "" : value;
    }

    default UserResponse toResponse(User user) {
        if (user == null) return null;
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(safe(user.getFirstName()))
                .lastName(safe(user.getLastName()))
                .provider(user.getProvider())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
