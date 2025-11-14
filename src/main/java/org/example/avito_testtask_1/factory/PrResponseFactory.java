package org.example.avito_testtask_1.factory;

import org.example.avito_testtask_1.dto.PrResponseDto;
import org.example.avito_testtask_1.model.Pr;
import org.example.avito_testtask_1.model.User;

import java.util.stream.Collectors;

public class PrResponseFactory {
    public static PrResponseDto fromEntity(Pr pr) {
        PrResponseDto dto = new PrResponseDto();
        dto.setId(pr.getId());
        dto.setTitle(pr.getTitle());
        dto.setAuthorId(pr.getAuthor().getId());
        dto.setStatus(pr.getStatus());
        dto.setReviewerIds(
                pr.getReviewers().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet())
        );
        return dto;
    }
}
