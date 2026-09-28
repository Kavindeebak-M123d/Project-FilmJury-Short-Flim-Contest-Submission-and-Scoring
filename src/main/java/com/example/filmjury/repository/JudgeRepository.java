package com.example.filmjury.repository;

import com.example.filmjury.entity.Judge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JudgeRepository extends JpaRepository<Judge, Long> {
    Optional<Judge> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
}
