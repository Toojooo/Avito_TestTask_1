package org.example.avito_testtask_1.repository;

import org.example.avito_testtask_1.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {
    Team findByName(String name);
}
