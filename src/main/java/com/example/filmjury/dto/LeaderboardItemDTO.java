package com.example.filmjury.dto;

public class LeaderboardItemDTO {
    private int rank;
    private Long entryId;
    private String title;
    private String teamName;
    private String genre;
    private double averageScore;
    private int judgesScored;

    public LeaderboardItemDTO() {
    }

    public LeaderboardItemDTO(int rank, Long entryId, String title, String teamName, String genre, double averageScore, int judgesScored) {
        this.rank = rank;
        this.entryId = entryId;
        this.title = title;
        this.teamName = teamName;
        this.genre = genre;
        this.averageScore = averageScore;
        this.judgesScored = judgesScored;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public int getJudgesScored() {
        return judgesScored;
    }

    public void setJudgesScored(int judgesScored) {
        this.judgesScored = judgesScored;
    }
}
