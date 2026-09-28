package com.example.filmjury.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class ScoreCardRequestDTO {
    @NotNull(message = "Entry ID is required.")
    private Long entryId;

    @NotNull(message = "Judge ID is required.")
    private Long judgeId;

    // Map of criterionId -> score value
    private Map<Long, Double> scores;

    private String comments;

    public ScoreCardRequestDTO() {
    }

    public ScoreCardRequestDTO(Long entryId, Long judgeId, Map<Long, Double> scores, String comments) {
        this.entryId = entryId;
        this.judgeId = judgeId;
        this.scores = scores;
        this.comments = comments;
    }

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public Long getJudgeId() {
        return judgeId;
    }

    public void setJudgeId(Long judgeId) {
        this.judgeId = judgeId;
    }

    public Map<Long, Double> getScores() {
        return scores;
    }

    public void setScores(Map<Long, Double> scores) {
        this.scores = scores;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
