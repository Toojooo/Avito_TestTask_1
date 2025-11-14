package org.example.avito_testtask_1.service;

import jakarta.validation.ValidationException;
import org.example.avito_testtask_1.dto.PrCreateDto;
import org.example.avito_testtask_1.model.Pr;
import org.example.avito_testtask_1.model.Team;
import org.example.avito_testtask_1.model.User;
import org.example.avito_testtask_1.model.enums.PrStatus;
import org.example.avito_testtask_1.repository.PrRepository;
import org.example.avito_testtask_1.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PrServiceTest {

    @Mock
    private PrRepository prRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private PrService service;

    @Test
    void createPr_success() {
        User author = new User();
        author.setId(UUID.randomUUID());
        Team team = new Team();
        team.setId(UUID.randomUUID());
        author.setTeam(team);

        User reviewer1 = new User();
        reviewer1.setId(UUID.randomUUID());

        User reviewer2 = new User();
        reviewer2.setId(UUID.randomUUID());

        Mockito.when(userRepository.findById(author.getId()))
                .thenReturn(Optional.of(author));

        Mockito.when(userRepository.findActiveUsersInTeamExcluding(team.getId(), author.getId()))
                .thenReturn(List.of(reviewer1, reviewer2));

        Mockito.when(prRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PrCreateDto dto = new PrCreateDto("Test PR", author.getId());

        Pr pr = service.createPr(dto);

        assertEquals("Test PR", pr.getTitle());
        assertEquals(author, pr.getAuthor());
        assertEquals(2, pr.getReviewers().size());
    }

    @Test
    void createPr_authorHasNoTeam() {
        User author = new User();
        author.setId(UUID.randomUUID());
        author.setTeam(null);

        Mockito.when(userRepository.findById(author.getId()))
                .thenReturn(Optional.of(author));

        PrCreateDto dto = new PrCreateDto("Test", author.getId());

        assertThrows(ValidationException.class, () -> service.createPr(dto));
    }

    @Test
    void reassignReviewer_success() {
        User reviewerOld = new User();
        reviewerOld.setId(UUID.randomUUID());
        Team team = new Team();
        team.setId(UUID.randomUUID());
        reviewerOld.setTeam(team);

        User reviewerNew = new User();
        reviewerNew.setId(UUID.randomUUID());

        Pr pr = new Pr();
        pr.setId(UUID.randomUUID());
        pr.getReviewers().add(reviewerOld);

        Mockito.when(prRepository.findById(pr.getId()))
                .thenReturn(Optional.of(pr));

        Mockito.when(userRepository.findById(reviewerOld.getId()))
                .thenReturn(Optional.of(reviewerOld));

        Mockito.when(userRepository.findActiveUsersInTeamExcluding(team.getId(), reviewerOld.getId()))
                .thenReturn(List.of(reviewerNew));

        Mockito.when(prRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pr updated = service.reassignReviewer(pr.getId(), reviewerOld.getId());

        assertTrue(updated.getReviewers().contains(reviewerNew));
        assertFalse(updated.getReviewers().contains(reviewerOld));
    }

    @Test
    void reassignReviewer_prIsMerged_shouldThrow() {
        Pr pr = new Pr();
        pr.setId(UUID.randomUUID());
        pr.setStatus(PrStatus.MERGED);

        Mockito.when(prRepository.findById(pr.getId()))
                .thenReturn(Optional.of(pr));

        assertThrows(ValidationException.class, () -> service.reassignReviewer(pr.getId(), UUID.randomUUID()));
    }

    @Test
    void reassignReviewer_reviewerNotInPr_shouldThrow() {
        Pr pr = new Pr();
        pr.setId(UUID.randomUUID());

        User reviewer = new User();
        reviewer.setId(UUID.randomUUID());

        Mockito.when(prRepository.findById(pr.getId()))
                .thenReturn(Optional.of(pr));

        Mockito.when(userRepository.findById(reviewer.getId()))
                .thenReturn(Optional.of(reviewer));

        assertThrows(ValidationException.class, () -> service.reassignReviewer(pr.getId(), reviewer.getId()));
    }

    @Test
    void getPrsForReviewer_success() {
        User reviewer = new User();
        reviewer.setId(UUID.randomUUID());

        Pr pr = new Pr();
        pr.setId(UUID.randomUUID());
        pr.getReviewers().add(reviewer);

        Mockito.when(userRepository.findById(reviewer.getId()))
                .thenReturn(Optional.of(reviewer));

        Mockito.when(prRepository.findByReviewersContaining(reviewer))
                .thenReturn(List.of(pr));

        List<Pr> result = service.getPrsForReviewer(reviewer.getId());

        assertEquals(1, result.size());
        assertEquals(pr.getId(), result.get(0).getId());
    }

    @Test
    void mergePr_success() {
        Pr pr = new Pr();
        pr.setId(UUID.randomUUID());
        pr.setStatus(PrStatus.OPEN);

        Mockito.when(prRepository.findById(pr.getId()))
                .thenReturn(Optional.of(pr));

        Mockito.when(prRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pr merged = service.mergePr(pr.getId());

        assertEquals(PrStatus.MERGED, merged.getStatus());
    }

    @Test
    void mergePr_alreadyMerged_shouldThrow() {
        Pr pr = new Pr();
        pr.setId(UUID.randomUUID());
        pr.setStatus(PrStatus.MERGED);

        Mockito.when(prRepository.findById(pr.getId()))
                .thenReturn(Optional.of(pr));

        assertThrows(ValidationException.class, () -> service.mergePr(pr.getId()));
    }
}
