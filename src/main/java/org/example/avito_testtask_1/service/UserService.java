package org.example.avito_testtask_1.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.avito_testtask_1.dto.UserDto;
import org.example.avito_testtask_1.model.Team;
import org.example.avito_testtask_1.model.User;
import org.example.avito_testtask_1.repository.TeamRepository;
import org.example.avito_testtask_1.repository.UserRepository;
import org.example.avito_testtask_1.factory.UserFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserServiceImpl {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public UserDto createUser(UserDto dto) {
        log.info("Creating user name={} active={} teamId={}",
                dto.getName(), dto.isActive(), dto.getTeamId());

        Team team = loadTeamIfPresent(dto.getTeamId());
        User user = createUserEntity(dto, team);
        user = saveUser(user);

        dto.setId(user.getId());

        log.info("User created id={}", user.getId());
        return dto;
    }

    @Override
    @Transactional
    public void toggleActive(UUID userId, boolean isActive) {
        log.info("Toggle active userId={} -> {}", userId, isActive);

        User user = loadUser(userId);
        updateActiveStatus(user, isActive);
        saveUser(user);

        log.info("User updated userId={} active={}", userId, isActive);
    }

    private Team loadTeamIfPresent(UUID teamId) {
        if (teamId == null) return null;
        log.debug("Loading team id={}", teamId);

        return teamRepository.findById(teamId)
                .orElseThrow(() -> {
                    log.warn("Team not found id={}", teamId);
                    return new RuntimeException("Team not found");
                });
    }

    private User createUserEntity(UserDto dto, Team team) {
        return dto.isActive()
                ? UserFactory.create(dto.getName(), team)
                : UserFactory.createInactive(dto.getName(), team);
    }

    private User saveUser(User user) {
        return userRepository.save(user);
    }

    private User loadUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found id={}", userId);
                    return new RuntimeException("User not found");
                });
    }

    private void updateActiveStatus(User user, boolean isActive) {
        user.setActive(isActive);
    }
}



