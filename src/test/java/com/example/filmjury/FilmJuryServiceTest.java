package com.example.filmjury;

import com.example.filmjury.entity.Criterion;
import com.example.filmjury.entity.Entry;
import com.example.filmjury.entity.Judge;
import com.example.filmjury.exception.DuplicateScoreException;
import com.example.filmjury.exception.InvalidScoreException;
import com.example.filmjury.repository.CriterionRepository;
import com.example.filmjury.repository.EntryRepository;
import com.example.filmjury.repository.JudgeRepository;
import com.example.filmjury.repository.ScoreCardRepository;
import com.example.filmjury.service.EntryService;
import com.example.filmjury.service.JudgeService;
import com.example.filmjury.service.ScoreCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class FilmJuryServiceTest {

    @Autowired
    private EntryService entryService;

    @Autowired
    private JudgeService judgeService;

    @Autowired
    private CriterionRepository criterionRepository;

    @Autowired
    private ScoreCardService scoreCardService;

    private Entry testEntry;
    private Judge testJudgeAssigned;
    private Judge testJudgeUnassigned;
    private Criterion criterionStory;

    @BeforeEach
    void setUp() {
        testEntry = entryService.saveEntry(new Entry("Test Film", "Drama", "Desc", "https://youtube.com/v1", "Team Test", "Alice, Bob"));
        testJudgeAssigned = judgeService.saveJudge(new Judge("Judge Assigned", "assigned@test.org"));
        testJudgeUnassigned = judgeService.saveJudge(new Judge("Judge Unassigned", "unassigned@test.org"));
        
        criterionStory = criterionRepository.findByNameIgnoreCase("Story")
                .orElseGet(() -> criterionRepository.save(new Criterion("Story", "Story description", 10)));

        entryService.assignJudgeToEntry(testEntry.getId(), testJudgeAssigned.getId());
    }

    @Test
    void testScoreSubmissionSuccessAndAverageCalculation() {
        Map<Long, Double> scores = new HashMap<>();
        scores.put(criterionStory.getId(), 8.5);

        scoreCardService.submitScores(testEntry.getId(), testJudgeAssigned.getId(), scores, "Great plot!");

        Entry reloaded = entryService.getEntryById(testEntry.getId());
        assertEquals(8.5, reloaded.getAverageScore());
        assertEquals(1, reloaded.getJudgesScoredCount());
    }

    @Test
    void testUnassignedJudgeScoringThrowsException() {
        Map<Long, Double> scores = new HashMap<>();
        scores.put(criterionStory.getId(), 9.0);

        Exception exception = assertThrows(InvalidScoreException.class, () -> {
            scoreCardService.submitScores(testEntry.getId(), testJudgeUnassigned.getId(), scores, "Unassigned test");
        });

        assertEquals("This judge is not assigned to this entry.", exception.getMessage());
    }

    @Test
    void testDuplicateScoreSubmissionThrowsException() {
        Map<Long, Double> scores = new HashMap<>();
        scores.put(criterionStory.getId(), 9.0);

        scoreCardService.submitScores(testEntry.getId(), testJudgeAssigned.getId(), scores, "First submission");

        Exception exception = assertThrows(DuplicateScoreException.class, () -> {
            scoreCardService.submitScores(testEntry.getId(), testJudgeAssigned.getId(), scores, "Duplicate submission");
        });

        assertEquals("Score already submitted for this entry.", exception.getMessage());
    }
}
