package org.example.avito_testtask_1.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.avito_testtask_1.dto.PrCreateDto;
import org.example.avito_testtask_1.dto.PrResponseDto;
import org.example.avito_testtask_1.dto.ReassignDto;
import org.example.avito_testtask_1.factory.PrResponseFactory;
import org.example.avito_testtask_1.model.Pr;
import org.example.avito_testtask_1.service.PrService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/prs")
@Slf4j
public class PrController {
    private final PrService prService;

    @Operation(
            summary = "Создать новый Pull Request",
            description = "Создаёт новый PR от указанного автора и назначает указанных ревьюеров."
    )
    @PostMapping
    public PrResponseDto create(@Valid @RequestBody PrCreateDto dto) {
        log.info("API: Creating PR title={}", dto.getTitle());
        Pr pr = prService.createPr(dto);
        return PrResponseFactory.fromEntity(pr);
    }

    @Operation(
            summary = "Переназначить ревьюера",
            description = """
                    Заменяет существующего ревьюера на нового.
                   
                    Правила:
                    • PR должен существовать.
                    • Старый ревьюер должен быть среди текущих.
                    • Новый ревьюер должен быть активным пользователем.
                    • Статус PR должен позволять изменения.
                    """
    )
    @PostMapping("/{id}/reassign")
    public PrResponseDto reassign(
            @PathVariable UUID id,
            @Valid @RequestBody ReassignDto dto
    ) {
        log.info("API: Reassign reviewer prId={} oldReviewerId={}", id, dto.getOldReviewerId());
        Pr pr = prService.reassignReviewer(id, dto.getOldReviewerId());
        return PrResponseFactory.fromEntity(pr);
    }

    @Operation(
            summary = "Получить PR, назначенные ревьюеру",
            description = "Возвращает список всех PR, на которые назначен указанный пользователь."
    )
    @GetMapping("/reviewer/{userId}")
    public List<PrResponseDto> getForReviewer(@PathVariable UUID userId) {
        log.info("API: Get PRs for reviewer userId={}", userId);
        return prService.getPrsForReviewer(userId).stream()
                .map(PrResponseFactory::fromEntity)
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Смержить PR",
            description = """
                    Объединяет PR и переводит его в финальный статус MERGED.
                    
                    Правила:
                    • PR должен существовать.
                    • Все ревьюеры должны одобрить PR.
                    • PR должен находиться в состоянии OPEN.
                    """
    )
    @PatchMapping("/{id}/merge")
    public PrResponseDto merge(@PathVariable UUID id) {
        log.info("API: Merge PR prId={}", id);

        Pr pr = prService.mergePr(id);

        return PrResponseFactory.fromEntity(pr);
    }
}

