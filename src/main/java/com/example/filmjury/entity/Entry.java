package com.example.filmjury.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "entries")
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Film title is required.")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Genre is required.")
    @Column(nullable = false)
    private String genre;

    @Column(length = 2000)
    private String description;

    @NotBlank(message = "Video link is required.")
    @Column(nullable = false)
    private String videoLink;

    @NotBlank(message = "Team name is required.")
    @Column(nullable = false)
    private String teamName;

    private String teamMembers;

    @Column(nullable = false)
    private LocalDate submissionDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "entry_judges",
        joinColumns = @JoinColumn(name = "entry_id"),
        inverseJoinColumns = @JoinColumn(name = "judge_id")
    )
    @JsonIgnoreProperties("entries")
    private Set<Judge> judges = new HashSet<>();

    @OneToMany(mappedBy = "entry", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("entry")
    private List<ScoreCard> scoreCards = new ArrayList<>();

    public Entry() {
        this.submissionDate = LocalDate.now();
    }

    public Entry(String title, String genre, String description, String videoLink, String teamName, String teamMembers) {
        this.title = title;
        this.genre = genre;
        this.description = description;
        this.videoLink = videoLink;
        this.teamName = teamName;
        this.teamMembers = teamMembers;
        this.submissionDate = LocalDate.now();
    }

    // Calculated getters
    public double getAverageScore() {
        if (scoreCards == null || scoreCards.isEmpty()) {
            return 0.0;
        }
        double sum = scoreCards.stream().mapToDouble(ScoreCard::getScore).sum();
        double avg = sum / scoreCards.size();
        return Math.round(avg * 10.0) / 10.0;
    }

    public int getJudgesScoredCount() {
        if (scoreCards == null || scoreCards.isEmpty()) {
            return 0;
        }
        return (int) scoreCards.stream()
                .map(sc -> sc.getJudge().getId())
                .distinct()
                .count();
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(String teamMembers) {
        this.teamMembers = teamMembers;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Set<Judge> getJudges() {
        return judges;
    }

    public void setJudges(Set<Judge> judges) {
        this.judges = judges;
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
        Entry entry = (Entry) o;
        return Objects.equals(id, entry.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
