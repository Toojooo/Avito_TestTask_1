package org.example.avito_testtask_1.service;

import org.example.avito_testtask_1.dto.UserDto;

import java.util.UUID;

public interface UserServiceImpl {
    UserDto createUser(UserDto dto);
    void toggleActive(UUID userId, boolean isActive);
}
