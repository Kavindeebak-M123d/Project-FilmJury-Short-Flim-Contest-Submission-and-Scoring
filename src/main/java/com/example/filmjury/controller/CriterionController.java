package com.example.filmjury.controller;

import com.example.filmjury.entity.Criterion;
import com.example.filmjury.service.CriterionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/criteria")
public class CriterionController {

    private final CriterionService criterionService;

    @Autowired
    public CriterionController(CriterionService criterionService) {
        this.criterionService = criterionService;
    }

    @GetMapping
    public String listCriteria(Model model) {
        model.addAttribute("criteria", criterionService.getAllCriteria());
        if (!model.containsAttribute("criterion")) {
            model.addAttribute("criterion", new Criterion("", "", 10));
        }
        return "criteria";
    }

    @PostMapping
    public String saveCriterion(@Valid @ModelAttribute("criterion") Criterion criterion,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("criteria", criterionService.getAllCriteria());
            return "criteria";
        }
        criterionService.saveCriterion(criterion);
        redirectAttributes.addFlashAttribute("successMessage", "Criterion saved successfully!");
        return "redirect:/criteria";
    }

    @GetMapping("/edit/{id}")
    public String editCriterionForm(@PathVariable Long id, Model model) {
        Criterion criterion = criterionService.getCriterionById(id);
        model.addAttribute("criterion", criterion);
        model.addAttribute("criteria", criterionService.getAllCriteria());
        model.addAttribute("isEdit", true);
        return "criteria";
    }

    @PostMapping("/edit/{id}")
    public String updateCriterion(@PathVariable Long id,
                                  @Valid @ModelAttribute("criterion") Criterion criterion,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        if (result.hasErrors()) {
            model.addAttribute("criteria", criterionService.getAllCriteria());
            model.addAttribute("isEdit", true);
            return "criteria";
        }
        criterionService.updateCriterion(id, criterion);
        redirectAttributes.addFlashAttribute("successMessage", "Criterion updated successfully!");
        return "redirect:/criteria";
    }

    @GetMapping("/delete/{id}")
    public String deleteCriterion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        criterionService.deleteCriterion(id);
        redirectAttributes.addFlashAttribute("successMessage", "Criterion deleted successfully!");
        return "redirect:/criteria";
    }
}
