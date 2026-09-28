package com.example.filmjury.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "criteria")
public class Criterion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Criterion name is required.")
    @Column(nullable = false)
    private String name;

    private String description;

    @NotNull(message = "Maximum score is required.")
    @Min(value = 1, message = "Max score must be at least 1.")
    @Max(value = 100, message = "Max score cannot exceed 100.")
    @Column(nullable = false)
    private Integer maxScore = 10;

    @OneToMany(mappedBy = "criterion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ScoreCard> scoreCards = new ArrayList<>();

    public Criterion() {
    }

    public Criterion(String name, String description, Integer maxScore) {
        this.name = name;
        this.description = description;
        this.maxScore = maxScore != null ? maxScore : 10;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public List<ScoreCard> getScoreCards() {
        return scoreCards;
    }

    public void setScoreCards(List<ScoreCard> scoreCards) {
        this.scoreCards = scoreCards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Criterion criterion = (Criterion) o;
        return Objects.equals(id, criterion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
