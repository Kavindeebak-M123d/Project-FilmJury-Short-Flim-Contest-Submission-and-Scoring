package com.example.filmjury.controller;

import com.example.filmjury.service.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final EntryService entryService;

    @Autowired
    public HomeController(EntryService entryService) {
        this.entryService = entryService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("recentEntries", entryService.getAllEntries().stream().limit(3).toList());
        return "index";
    }
}
