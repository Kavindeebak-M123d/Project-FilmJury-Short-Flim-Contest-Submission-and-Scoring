package com.example.filmjury.service;

import com.example.filmjury.entity.Criterion;
import com.example.filmjury.entity.Entry;
import com.example.filmjury.entity.Judge;
import com.example.filmjury.entity.ScoreCard;
import com.example.filmjury.exception.DuplicateScoreException;
import com.example.filmjury.exception.InvalidScoreException;
import com.example.filmjury.exception.ResourceNotFoundException;
import com.example.filmjury.repository.CriterionRepository;
import com.example.filmjury.repository.EntryRepository;
import com.example.filmjury.repository.JudgeRepository;
import com.example.filmjury.repository.ScoreCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ScoreCardService {

    private final ScoreCardRepository scoreCardRepository;
    private final EntryRepository entryRepository;
    private final JudgeRepository judgeRepository;
    private final CriterionRepository criterionRepository;

    @Autowired
    public ScoreCardService(ScoreCardRepository scoreCardRepository,
                            EntryRepository entryRepository,
                            JudgeRepository judgeRepository,
                            CriterionRepository criterionRepository) {
        this.scoreCardRepository = scoreCardRepository;
        this.entryRepository = entryRepository;
        this.judgeRepository = judgeRepository;
        this.criterionRepository = criterionRepository;
    }

    public List<ScoreCard> getAllScoreCards() {
        return scoreCardRepository.findAll();
    }

    public List<ScoreCard> getScoreCardsByEntry(Long entryId) {
        return scoreCardRepository.findByEntryId(entryId);
    }

    @Transactional
    public List<ScoreCard> submitScores(Long entryId, Long judgeId, Map<Long, Double> criterionScores, String comments) {
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with ID: " + entryId));

        Judge judge = judgeRepository.findById(judgeId)
                .orElseThrow(() -> new ResourceNotFoundException("Judge not found with ID: " + judgeId));

        // RULE 3: A judge can score only an entry assigned to that judge
        if (!entry.getJudges().contains(judge)) {
            throw new InvalidScoreException("This judge is not assigned to this entry.");
        }

        // RULE 2: A judge can submit only ONE scorecard for the same entry and criterion
        if (scoreCardRepository.existsByEntryAndJudge(entry, judge)) {
            throw new DuplicateScoreException("Score already submitted for this entry.");
        }

        if (criterionScores == null || criterionScores.isEmpty()) {
            throw new InvalidScoreException("At least one criterion score must be provided.");
        }

        List<ScoreCard> createdCards = new ArrayList<>();

        for (Map.Entry<Long, Double> item : criterionScores.entrySet()) {
            Long criterionId = item.getKey();
            Double scoreValue = item.getValue();

            if (scoreValue == null) {
                throw new InvalidScoreException("Score value cannot be null.");
            }

            Criterion criterion = criterionRepository.findById(criterionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Criterion not found with ID: " + criterionId));

            // RULE 4: Scores must be between 0 and maxScore
            if (scoreValue < 0 || scoreValue > criterion.getMaxScore()) {
                throw new InvalidScoreException("Score must be between 0 and " + criterion.getMaxScore() + ".");
            }

            if (scoreCardRepository.existsByEntryAndJudgeAndCriterion(entry, judge, criterion)) {
                throw new DuplicateScoreException("Score already submitted for this entry.");
            }

            ScoreCard card = new ScoreCard(entry, judge, criterion, scoreValue, comments);
            card.setSubmittedAt(LocalDateTime.now());
            createdCards.add(scoreCardRepository.save(card));
        }

        return createdCards;
    }

    public ScoreCard saveSingleScoreCard(Long entryId, Long judgeId, Long criterionId, Double score, String comments) {
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with ID: " + entryId));

        Judge judge = judgeRepository.findById(judgeId)
                .orElseThrow(() -> new ResourceNotFoundException("Judge not found with ID: " + judgeId));

        if (!entry.getJudges().contains(judge)) {
            throw new InvalidScoreException("This judge is not assigned to this entry.");
        }

        Criterion criterion = criterionRepository.findById(criterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Criterion not found with ID: " + criterionId));

        if (score < 0 || score > criterion.getMaxScore()) {
            throw new InvalidScoreException("Score must be between 0 and " + criterion.getMaxScore() + ".");
        }

        if (scoreCardRepository.existsByEntryAndJudgeAndCriterion(entry, judge, criterion)) {
            throw new DuplicateScoreException("Score already submitted for this entry.");
        }

        ScoreCard card = new ScoreCard(entry, judge, criterion, score, comments);
        return scoreCardRepository.save(card);
    }
}
