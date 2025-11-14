package org.example.avito_testtask_1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class AssignmentStatsDto {
    @Schema(description = "Количество назначений по каждому пользователю (userId -> count)")
    private Map<UUID, Long> assignmentsByUser;

    @Schema(description = "Количество назначений по каждому PR (prId -> count)")
    private Map<UUID, Long> assignmentsByPr;
}
