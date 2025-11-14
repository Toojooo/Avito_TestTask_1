package org.example.avito_testtask_1.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.avito_testtask_1.dto.PrCreateDto;
import org.example.avito_testtask_1.model.Pr;
import org.example.avito_testtask_1.model.User;
import org.example.avito_testtask_1.model.enums.PrStatus;
import org.example.avito_testtask_1.repository.PrRepository;
import org.example.avito_testtask_1.repository.UserRepository;
import org.example.avito_testtask_1.factory.PrFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrService implements PrServiceImpl {
    private final PrRepository prRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Pr createPr(PrCreateDto dto) {
        log.info("Creating PR title='{}' authorId={}", dto.getTitle(), dto.getAuthorId());

        User author = loadAuthor(dto.getAuthorId());
        validateAuthorHasTeam(author);

        List<User> candidates = loadReviewerCandidates(author);
        Set<User> reviewers = pickReviewers(candidates);

        Pr pr = PrFactory.createWithReviewers(dto.getTitle(), author, reviewers);
        pr = savePr(pr);

        log.info("PR created id={} reviewersCount={}", pr.getId(), reviewers.size());
        return pr;
    }

    @Override
    @Transactional
    public Pr reassignReviewer(UUID prId, UUID oldReviewerId) {
        log.info("Reassigning reviewer prId={} oldReviewerId={}", prId, oldReviewerId);

        Pr pr = loadPr(prId);
        validateNotMerged(pr);

        User oldReviewer = loadReviewer(oldReviewerId);
        removeOldReviewer(pr, oldReviewer);

        List<User> candidates = loadReviewerCandidates(oldReviewer);

        if (candidates.isEmpty()) {
            log.warn("No active candidates for reassignment teamId={}", oldReviewer.getTeam().getId());
            throw new ValidationException("No active candidates in team");
        }

        User newReviewer = pickOneRandom(candidates);
        pr.getReviewers().add(newReviewer);

        pr = savePr(pr);

        log.info("Reviewer reassigned prId={} newReviewerId={}", prId, newReviewer.getId());
        return pr;
    }

    @Override
    public List<Pr> getPrsForReviewer(UUID userId) {
        log.info("Getting PR list for reviewer userId={}", userId);

        User user = loadUser(userId);
        return prRepository.findByReviewersContaining(user);
    }

    @Override
    @Transactional
    public Pr mergePr(UUID prId) {
        log.info("Merging PR prId={}", prId);

        Pr pr = loadPr(prId);
        validateNotMerged(pr);

        pr.setStatus(PrStatus.MERGED);
        pr = savePr(pr);

        log.info("PR merged prId={}", prId);
        return pr;
    }

    private User loadAuthor(UUID authorId) {
        return userRepository.findById(authorId)
                .orElseThrow(() -> {
                    log.warn("Author not found id={}", authorId);
                    return new RuntimeException("Author not found");
                });
    }

    private void validateAuthorHasTeam(User author) {
        if (author.getTeam() == null) {
            log.warn("Author has no team authorId={}", author.getId());
            throw new ValidationException("Author has no team");
        }
    }

    private List<User> loadReviewerCandidates(User baseUser) {
        UUID teamId = baseUser.getTeam().getId();
        UUID excludeId = baseUser.getId();

        log.debug("Loading reviewer candidates teamId={} excludeUserId={}", teamId, excludeId);

        return userRepository.findActiveUsersInTeamExcluding(teamId, excludeId);
    }

    private Set<User> pickReviewers(List<User> candidates) {
        Collections.shuffle(candidates);
        return candidates.stream().limit(2).collect(Collectors.toSet());
    }

    private User pickOneRandom(List<User> candidates) {
        Collections.shuffle(candidates);
        return candidates.get(0);
    }

    private Pr savePr(Pr pr) {
        return prRepository.save(pr);
    }

    private Pr loadPr(UUID prId) {
        return prRepository.findById(prId)
                .orElseThrow(() -> {
                    log.warn("PR not found id={}", prId);
                    return new RuntimeException("PR not found");
                });
    }

    private User loadReviewer(UUID reviewerId) {
        return userRepository.findById(reviewerId)
                .orElseThrow(() -> {
                    log.warn("Reviewer not found id={}", reviewerId);
                    return new RuntimeException("Reviewer not found");
                });
    }

    private User loadUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found id={}", userId);
                    return new RuntimeException("User not found");
                });
    }

    private void validateNotMerged(Pr pr) {
        if (pr.getStatus() == PrStatus.MERGED) {
            log.warn("Cannot modify merged PR prId={}", pr.getId());
            throw new ValidationException("Cannot reassign on merged PR");
        }
    }

    private void removeOldReviewer(Pr pr, User oldReviewer) {
        boolean removed = pr.getReviewers().remove(oldReviewer);
        if (!removed) {
            log.warn("Reviewer {} not found in PR {}", oldReviewer.getId(), pr.getId());
            throw new ValidationException("Reviewer not in this PR");
        }
    }
}




