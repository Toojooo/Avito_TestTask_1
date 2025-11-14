package org.example.avito_testtask_1.dto;

import lombok.Data;
import org.example.avito_testtask_1.model.enums.PrStatus;

import java.util.Set;
import java.util.UUID;

@Data
public class PrResponseDto {
    private UUID id;
    private String title;
    private UUID authorId;
    private PrStatus status;
    private Set<UUID> reviewerIds;
}
