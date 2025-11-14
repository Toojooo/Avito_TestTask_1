package org.example.avito_testtask_1.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.avito_testtask_1.dto.TeamDto;
import org.example.avito_testtask_1.service.TeamService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
@Slf4j
public class TeamController {
    private final TeamService teamService;

    @Operation(
            summary = "Создать новую команду",
            description = "Создает новую команду с указанным именем"
    )
    @PostMapping
    public TeamDto create(@Valid @RequestBody TeamDto dto) {
        log.info("API: Creating team name={}", dto.getName());
        TeamDto created = teamService.createTeam(dto);
        log.info("API: Team created id={} name={}", created.getId(), created.getName());
        return created;
    }
}
