package org.example.avito_testtask_1.service;

import lombok.RequiredArgsConstructor;
import org.example.avito_testtask_1.dto.AssignmentStatsDto;
import org.example.avito_testtask_1.model.Pr;
import org.example.avito_testtask_1.model.User;
import org.example.avito_testtask_1.repository.PrRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService implements StatsServiceImpl {
    private final PrRepository prRepository;

    @Override
    public AssignmentStatsDto getStats() {
        var prs = prRepository.findAll();

        Map<UUID, Long> byUser = prs.stream()
                .flatMap(pr -> pr.getReviewers().stream())
                .collect(Collectors.groupingBy(User::getId, Collectors.counting()));

        Map<UUID, Long> byPr = prs.stream()
                .collect(Collectors.toMap(
                        Pr::getId,
                        pr -> (long) pr.getReviewers().size()
                ));

        AssignmentStatsDto dto = new AssignmentStatsDto();
        dto.setAssignmentsByUser(byUser);
        dto.setAssignmentsByPr(byPr);
        return dto;
    }
}
