package org.example.avito_testtask_1.service;

import org.example.avito_testtask_1.dto.PrCreateDto;
import org.example.avito_testtask_1.model.Pr;

import java.util.List;
import java.util.UUID;

public interface PrServiceImpl {
    Pr createPr(PrCreateDto dto);
    Pr reassignReviewer(UUID prId, UUID oldReviewerId);
    Pr mergePr(UUID prId);
    List<Pr> getPrsForReviewer(UUID userId);
}

