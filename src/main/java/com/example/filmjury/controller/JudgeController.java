package com.example.filmjury.controller;

import com.example.filmjury.entity.Judge;
import com.example.filmjury.service.JudgeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/judges")
public class JudgeController {

    private final JudgeService judgeService;

    @Autowired
    public JudgeController(JudgeService judgeService) {
        this.judgeService = judgeService;
    }

    @GetMapping
    public String listJudges(Model model) {
        model.addAttribute("judges", judgeService.getAllJudges());
        if (!model.containsAttribute("judge")) {
            model.addAttribute("judge", new Judge());
        }
        return "judges";
    }

    @PostMapping
    public String saveJudge(@Valid @ModelAttribute("judge") Judge judge,
                            BindingResult result,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("judges", judgeService.getAllJudges());
            return "judges";
        }
        try {
            judgeService.saveJudge(judge);
            redirectAttributes.addFlashAttribute("successMessage", "Judge added successfully!");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/judges";
    }

    @GetMapping("/edit/{id}")
    public String editJudgeForm(@PathVariable Long id, Model model) {
        Judge judge = judgeService.getJudgeById(id);
        model.addAttribute("judge", judge);
        model.addAttribute("judges", judgeService.getAllJudges());
        model.addAttribute("isEdit", true);
        return "judges";
    }

    @PostMapping("/edit/{id}")
    public String updateJudge(@PathVariable Long id,
                             @Valid @ModelAttribute("judge") Judge judge,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("judges", judgeService.getAllJudges());
            model.addAttribute("isEdit", true);
            return "judges";
        }
        try {
            judgeService.updateJudge(id, judge);
            redirectAttributes.addFlashAttribute("successMessage", "Judge updated successfully!");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/judges";
    }

    @GetMapping("/delete/{id}")
    public String deleteJudge(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        judgeService.deleteJudge(id);
        redirectAttributes.addFlashAttribute("successMessage", "Judge removed successfully!");
        return "redirect:/judges";
    }
}
