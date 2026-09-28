package com.example.filmjury.controller;

import com.example.filmjury.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final EntryService entryService;
    private final JudgeService judgeService;
    private final CriterionService criterionService;
    private final ScoreCardService scoreCardService;
    private final LeaderboardService leaderboardService;

    @Autowired
    public DashboardController(EntryService entryService,
                               JudgeService judgeService,
                               CriterionService criterionService,
                               ScoreCardService scoreCardService,
                               LeaderboardService leaderboardService) {
        this.entryService = entryService;
        this.judgeService = judgeService;
        this.criterionService = criterionService;
        this.scoreCardService = scoreCardService;
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalFilms", entryService.getAllEntries().size());
        model.addAttribute("totalJudges", judgeService.getAllJudges().size());
        model.addAttribute("totalCriteria", criterionService.getAllCriteria().size());
        model.addAttribute("totalScorecards", scoreCardService.getAllScoreCards().size());

        model.addAttribute("recentSubmissions", entryService.getAllEntries().stream()
                .sorted((a, b) -> b.getSubmissionDate().compareTo(a.getSubmissionDate()))
                .limit(5).toList());

        model.addAttribute("topFilms", leaderboardService.getLeaderboard().stream().limit(5).toList());

        model.addAttribute("recentScores", scoreCardService.getAllScoreCards().stream()
                .sorted((a, b) -> b.getSubmittedAt().compareTo(a.getSubmittedAt()))
                .limit(5).toList());

        return "dashboard";
    }
}
