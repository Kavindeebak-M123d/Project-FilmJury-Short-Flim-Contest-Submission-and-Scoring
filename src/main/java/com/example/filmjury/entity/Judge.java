package com.example.filmjury.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.*;

@Entity
@Table(name = "judges")
public class Judge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Judge name is required.")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email address.")
    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany(mappedBy = "judges")
    @JsonIgnore
    private Set<Entry> entries = new HashSet<>();

    @OneToMany(mappedBy = "judge", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ScoreCard> scoreCards = new ArrayList<>();

    public Judge() {
    }

    public Judge(String name, String email) {
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Entry> getEntries() {
        return entries;
    }

    public void setEntries(Set<Entry> entries) {
        this.entries = entries;
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
        Judge judge = (Judge) o;
        return Objects.equals(id, judge.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
