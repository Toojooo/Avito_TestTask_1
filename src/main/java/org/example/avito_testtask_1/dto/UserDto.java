package org.example.avito_testtask_1.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String name;
    private boolean isActive;
    private UUID teamId;
}
