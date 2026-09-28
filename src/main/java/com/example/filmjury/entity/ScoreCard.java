package com.example.filmjury.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "scorecards", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"entry_id", "judge_id", "criterion_id"})
})
public class ScoreCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entry_id", nullable = false)
    @JsonIgnoreProperties("scoreCards")
    private Entry entry;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "judge_id", nullable = false)
    @JsonIgnoreProperties("scoreCards")
    private Judge judge;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "criterion_id", nullable = false)
    @JsonIgnoreProperties("scoreCards")
    private Criterion criterion;

    @NotNull(message = "Score is required.")
    @Column(nullable = false)
    private Double score;

    @Column(length = 1000)
    private String comments;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    public ScoreCard() {
        this.submittedAt = LocalDateTime.now();
    }

    public ScoreCard(Entry entry, Judge judge, Criterion criterion, Double score, String comments) {
        this.entry = entry;
        this.judge = judge;
        this.criterion = criterion;
        this.score = score;
        this.comments = comments;
        this.submittedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public Judge getJudge() {
        return judge;
    }

    public void setJudge(Judge judge) {
        this.judge = judge;
    }

    public Criterion getCriterion() {
        return criterion;
    }

    public void setCriterion(Criterion criterion) {
        this.criterion = criterion;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScoreCard scoreCard = (ScoreCard) o;
        return Objects.equals(id, scoreCard.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
