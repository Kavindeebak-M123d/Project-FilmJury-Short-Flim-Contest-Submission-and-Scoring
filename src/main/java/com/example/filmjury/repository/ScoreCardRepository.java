package com.example.filmjury.repository;

import com.example.filmjury.entity.Criterion;
import com.example.filmjury.entity.Entry;
import com.example.filmjury.entity.Judge;
import com.example.filmjury.entity.ScoreCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreCardRepository extends JpaRepository<ScoreCard, Long> {
    boolean existsByEntryAndJudgeAndCriterion(Entry entry, Judge judge, Criterion criterion);
    boolean existsByEntryAndJudge(Entry entry, Judge judge);
    List<ScoreCard> findByEntry(Entry entry);
    List<ScoreCard> findByJudge(Judge judge);
    List<ScoreCard> findByEntryId(Long entryId);
}
