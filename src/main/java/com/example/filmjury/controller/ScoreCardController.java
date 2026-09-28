package com.example.filmjury.controller;

import com.example.filmjury.entity.Criterion;
import com.example.filmjury.entity.Entry;
import com.example.filmjury.entity.Judge;
import com.example.filmjury.service.CriterionService;
import com.example.filmjury.service.EntryService;
import com.example.filmjury.service.ScoreCardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ScoreCardController {

    private final ScoreCardService scoreCardService;
    private final EntryService entryService;
    private final CriterionService criterionService;

    @Autowired
    public ScoreCardController(ScoreCardService scoreCardService,
                              EntryService entryService,
                              CriterionService criterionService) {
        this.scoreCardService = scoreCardService;
        this.entryService = entryService;
        this.criterionService = criterionService;
    }

    @GetMapping("/entries/{entryId}/score")
    public String showScoreForm(@PathVariable Long entryId, Model model) {
        Entry entry = entryService.getEntryById(entryId);
        List<Criterion> criteria = criterionService.getAllCriteria();
        model.addAttribute("entry", entry);
        model.addAttribute("assignedJudges", entry.getJudges());
        model.addAttribute("criteria", criteria);
        return "score-entry";
    }

    @PostMapping("/scorecards")
    public String submitScore(@RequestParam Long entryId,
                             @RequestParam Long judgeId,
                             @RequestParam(required = false) String comments,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        List<Criterion> criteria = criterionService.getAllCriteria();
        Map<Long, Double> criterionScores = new HashMap<>();

        for (Criterion c : criteria) {
            String paramVal = request.getParameter("criterion_" + c.getId());
            if (paramVal != null && !paramVal.trim().isEmpty()) {
                try {
                    criterionScores.put(c.getId(), Double.parseDouble(paramVal));
                } catch (NumberFormatException e) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Invalid score value for " + c.getName());
                    return "redirect:/entries/" + entryId + "/score";
                }
            }
        }

        try {
            scoreCardService.submitScores(entryId, judgeId, criterionScores, comments);
            redirectAttributes.addFlashAttribute("successMessage", "Score submitted successfully!");
            return "redirect:/leaderboard";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/entries/" + entryId + "/score";
        }
    }
}
