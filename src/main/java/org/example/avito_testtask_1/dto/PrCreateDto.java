package org.example.avito_testtask_1.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PrCreateDto {
    private String title;
    private UUID authorId;
}
