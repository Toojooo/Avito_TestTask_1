package org.example.avito_testtask_1.service;


import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.avito_testtask_1.dto.TeamDto;
import org.example.avito_testtask_1.model.Team;
import org.example.avito_testtask_1.repository.TeamRepository;
import org.example.avito_testtask_1.factory.TeamFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService implements TeamServiceImpl {
    private final TeamRepository teamRepository;

    @Override
    public TeamDto createTeam(TeamDto dto) {
        log.info("Creating team with name={}", dto.getName());

        validateTeamName(dto.getName());
        Team team = createTeamEntity(dto);
        team = saveTeam(team);

        dto.setId(team.getId());

        log.info("Team created id={}", team.getId());
        return dto;
    }

    private void validateTeamName(String name) {
        if (teamRepository.findByName(name) != null) {
            log.warn("Team name '{}' already exists", name);
            throw new ValidationException("Team name already exists");
        }
    }

    private Team createTeamEntity(TeamDto dto) {
        return TeamFactory.create(dto.getName());
    }
    private Team saveTeam(Team team) {
        return teamRepository.save(team);
    }
}



