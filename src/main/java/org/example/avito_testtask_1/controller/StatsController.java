package org.example.avito_testtask_1.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.avito_testtask_1.dto.AssignmentStatsDto;
import org.example.avito_testtask_1.service.StatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stats")
@Slf4j
public class StatsController {

    private final StatsService statsService;

    @Operation(
            summary = "Получить статистику назначений",
            description = """
                    Возвращает статистику по назначенным ревьюерам:
                    • количество назначений по пользователям
                    • количество назначений по каждому PR
                    """
    )
    @GetMapping
    public AssignmentStatsDto getStats() {
        log.info("API: Fetching assignment statistics");
        return statsService.getStats();
    }
}
