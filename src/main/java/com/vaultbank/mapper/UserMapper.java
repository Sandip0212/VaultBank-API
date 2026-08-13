package com.vaultbank.mapper;

import org.mapstruct.Mapper;

import com.vaultbank.dto.request.RegisterRequest;
import com.vaultbank.dto.response.UserResponse;
import com.vaultbank.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(RegisterRequest request);

    UserResponse toResponse(User user);
}