package com.example.filmjury.repository;

import com.example.filmjury.entity.Criterion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CriterionRepository extends JpaRepository<Criterion, Long> {
    Optional<Criterion> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
