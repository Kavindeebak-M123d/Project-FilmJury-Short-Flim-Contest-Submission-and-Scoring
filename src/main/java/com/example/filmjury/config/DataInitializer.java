package com.example.filmjury.config;

import com.example.filmjury.entity.Criterion;
import com.example.filmjury.entity.Entry;
import com.example.filmjury.entity.Judge;
import com.example.filmjury.service.CriterionService;
import com.example.filmjury.service.EntryService;
import com.example.filmjury.service.JudgeService;
import com.example.filmjury.service.ScoreCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CriterionService criterionService;
    private final JudgeService judgeService;
    private final EntryService entryService;
    private final ScoreCardService scoreCardService;

    @Autowired
    public DataInitializer(CriterionService criterionService,
                           JudgeService judgeService,
                           EntryService entryService,
                           ScoreCardService scoreCardService) {
        this.criterionService = criterionService;
        this.judgeService = judgeService;
        this.entryService = entryService;
        this.scoreCardService = scoreCardService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. Initialize default criteria
        criterionService.initDefaultCriteria();

        // 2. Insert sample data if database is empty
        if (judgeService.getAllJudges().isEmpty() && entryService.getAllEntries().isEmpty()) {
            // Sample Judges
            Judge j1 = judgeService.saveJudge(new Judge("Christopher Nolan", "nolan@filmjury.org"));
            Judge j2 = judgeService.saveJudge(new Judge("Greta Gerwig", "greta@filmjury.org"));
            Judge j3 = judgeService.saveJudge(new Judge("Guillermo del Toro", "deltoro@filmjury.org"));

            // Sample Films
            Entry e1 = new Entry(
                "Echoes of Tomorrow",
                "Sci-Fi",
                "A futuristic short exploring memory transference and soul preservation in post-apocalyptic Neo-Tokyo.",
                "https://www.youtube.com/watch?v=sample1",
                "Cyberpunk Collective",
                "Alex Rivera (Director), Elena Rostova (DOP)"
            );

            Entry e2 = new Entry(
                "The Velvet Frame",
                "Drama",
                "An intimate portrait of an elderly painter discovering her final masterpiece through forgotten love letters.",
                "https://www.youtube.com/watch?v=sample2",
                "Studio Vignette",
                "Marcus Vance (Writer/Director), Sarah Jenkins (Lead)"
            );

            Entry e3 = new Entry(
                "Midnight Symphony",
                "Thriller",
                "A tense psychological thriller centered around a pianist who hears sinister notes hidden in silence.",
                "https://www.youtube.com/watch?v=sample3",
                "Nocturne Pictures",
                "David Chen (Director), Chloe Bennet (Lead)"
            );

            e1 = entryService.saveEntry(e1);
            e2 = entryService.saveEntry(e2);
            e3 = entryService.saveEntry(e3);

            // Assign Judges
            entryService.assignJudgeToEntry(e1.getId(), j1.getId());
            entryService.assignJudgeToEntry(e1.getId(), j2.getId());

            entryService.assignJudgeToEntry(e2.getId(), j2.getId());
            entryService.assignJudgeToEntry(e2.getId(), j3.getId());

            entryService.assignJudgeToEntry(e3.getId(), j1.getId());
            entryService.assignJudgeToEntry(e3.getId(), j3.getId());

            // Add sample scorecards
            List<Criterion> criteria = criterionService.getAllCriteria();

            // Scorecard 1: Christopher Nolan scores Echoes of Tomorrow
            Map<Long, Double> scores1 = new HashMap<>();
            scores1.put(criteria.get(0).getId(), 9.5); // Story
            scores1.put(criteria.get(1).getId(), 9.0); // Creativity
            scores1.put(criteria.get(2).getId(), 10.0); // Cinematography
            scores1.put(criteria.get(3).getId(), 8.5); // Acting
            scores1.put(criteria.get(4).getId(), 9.5); // Editing
            scoreCardService.submitScores(e1.getId(), j1.getId(), scores1, "Outstanding cinematography and visual pacing!");

            // Scorecard 2: Greta Gerwig scores Echoes of Tomorrow
            Map<Long, Double> scores2 = new HashMap<>();
            scores2.put(criteria.get(0).getId(), 9.0);
            scores2.put(criteria.get(1).getId(), 9.5);
            scores2.put(criteria.get(2).getId(), 9.0);
            scores2.put(criteria.get(3).getId(), 9.0);
            scores2.put(criteria.get(4).getId(), 9.0);
            scoreCardService.submitScores(e1.getId(), j2.getId(), scores2, "Incredible artistic direction and story composition.");

            // Scorecard 3: Greta Gerwig scores The Velvet Frame
            Map<Long, Double> scores3 = new HashMap<>();
            scores3.put(criteria.get(0).getId(), 10.0);
            scores3.put(criteria.get(1).getId(), 8.5);
            scores3.put(criteria.get(2).getId(), 9.0);
            scores3.put(criteria.get(3).getId(), 9.5);
            scores3.put(criteria.get(4).getId(), 8.5);
            scoreCardService.submitScores(e2.getId(), j2.getId(), scores3, "Deeply moving narrative and acting performances.");
        }
    }
}
