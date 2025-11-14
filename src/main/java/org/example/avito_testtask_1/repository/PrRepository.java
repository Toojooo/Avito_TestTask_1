package org.example.avito_testtask_1.repository;

import org.example.avito_testtask_1.model.Pr;
import org.example.avito_testtask_1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PrRepository extends JpaRepository<Pr, UUID> {
    List<Pr> findByReviewersContaining(User user);
}
