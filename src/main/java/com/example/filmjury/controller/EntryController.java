package com.example.filmjury.controller;

import com.example.filmjury.entity.Entry;
import com.example.filmjury.entity.Judge;
import com.example.filmjury.service.EntryService;
import com.example.filmjury.service.JudgeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/entries")
public class EntryController {

    private final EntryService entryService;
    private final JudgeService judgeService;

    @Autowired
    public EntryController(EntryService entryService, JudgeService judgeService) {
        this.entryService = entryService;
        this.judgeService = judgeService;
    }

    @GetMapping
    public String listEntries(@RequestParam(required = false) String search,
                              @RequestParam(required = false) String genre,
                              @RequestParam(required = false, defaultValue = "date") String sort,
                              Model model) {
        List<Entry> entries = entryService.filterAndSortEntries(search, genre, sort);
        model.addAttribute("entries", entries);
        model.addAttribute("search", search != null ? search : "");
        model.addAttribute("selectedGenre", genre != null ? genre : "ALL");
        model.addAttribute("selectedSort", sort);
        return "entries";
    }

    @GetMapping("/new")
    public String showSubmissionForm(Model model) {
        model.addAttribute("entry", new Entry());
        return "entry-form";
    }

    @PostMapping
    public String submitFilm(@Valid @ModelAttribute("entry") Entry entry,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            return "entry-form";
        }
        entryService.saveEntry(entry);
        redirectAttributes.addFlashAttribute("successMessage", "Film submitted successfully!");
        return "redirect:/entries";
    }

    @GetMapping("/{id}")
    public String viewEntryDetails(@PathVariable Long id, Model model) {
        Entry entry = entryService.getEntryById(id);
        model.addAttribute("entry", entry);
        return "entry-details";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Entry entry = entryService.getEntryById(id);
        model.addAttribute("entry", entry);
        model.addAttribute("isEdit", true);
        return "entry-form";
    }

    @PostMapping("/{id}/edit")
    public String updateFilm(@PathVariable Long id,
                             @Valid @ModelAttribute("entry") Entry entry,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "entry-form";
        }
        entryService.updateEntry(id, entry);
        redirectAttributes.addFlashAttribute("successMessage", "Film details updated successfully!");
        return "redirect:/entries/" + id;
    }

    @GetMapping("/{id}/delete")
    public String deleteFilm(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        entryService.deleteEntry(id);
        redirectAttributes.addFlashAttribute("successMessage", "Film deleted successfully!");
        return "redirect:/entries";
    }

    @GetMapping("/{id}/assign")
    public String showAssignJudgesForm(@PathVariable Long id, Model model) {
        Entry entry = entryService.getEntryById(id);
        List<Judge> allJudges = judgeService.getAllJudges();
        model.addAttribute("entry", entry);
        model.addAttribute("allJudges", allJudges);
        return "assign-judges";
    }

    @PostMapping("/{id}/assign")
    public String processAssignJudges(@PathVariable Long id,
                                      @RequestParam(required = false) List<Long> judgeIds,
                                      RedirectAttributes redirectAttributes) {
        entryService.updateAssignedJudges(id, judgeIds);
        redirectAttributes.addFlashAttribute("successMessage", "Judges updated for " + entryService.getEntryById(id).getTitle() + "!");
        return "redirect:/entries/" + id;
    }
}
