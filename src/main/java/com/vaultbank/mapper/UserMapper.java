package com.vaultbank.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.vaultbank.dto.request.RegisterRequest;
import com.vaultbank.dto.response.UserResponse;
import com.vaultbank.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    User toEntity(RegisterRequest request);

    UserResponse toResponse(User user);
}