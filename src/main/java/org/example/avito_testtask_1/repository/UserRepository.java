package org.example.avito_testtask_1.repository;

import org.example.avito_testtask_1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u WHERE u.team.id = :teamId AND u.isActive = true AND u.id != :excludeUserId")
    List<User> findActiveUsersInTeamExcluding(UUID teamId, UUID excludeUserId);
}
