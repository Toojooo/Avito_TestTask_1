package org.example.avito_testtask_1.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.avito_testtask_1.dto.UserDto;
import org.example.avito_testtask_1.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Создать пользователя",
            description = "Создаёт нового пользователя. Можно сразу назначить его в команду."
    )
    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto dto) {
        log.info("API: Creating user name={} teamId={}", dto.getName(), dto.getTeamId());
        UserDto created = userService.createUser(dto);
        log.info("API: User created id={} name={}", created.getId(), created.getName());
        return created;
    }

    @Operation(
            summary = "Поставить статус пользователя",
            description = "Меняет статус пользователю."
    )
    @PatchMapping("/{id}/active")
    public ResponseEntity<Void> toggleActive(@PathVariable UUID id, @RequestParam boolean active) {
        log.info("API: Toggling user active state userId={} active={}", id, active);
        userService.toggleActive(id, active);
        log.info("API: User active state updated userId={}", id);
        return ResponseEntity.ok().build();
    }
}