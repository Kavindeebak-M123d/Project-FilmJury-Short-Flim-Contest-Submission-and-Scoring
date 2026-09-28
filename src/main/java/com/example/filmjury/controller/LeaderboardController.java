package com.example.filmjury.controller;

import com.example.filmjury.dto.LeaderboardItemDTO;
import com.example.filmjury.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @Autowired
    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/leaderboard")
    public String viewLeaderboard(Model model) {
        List<LeaderboardItemDTO> leaderboard = leaderboardService.getLeaderboard();
        model.addAttribute("leaderboard", leaderboard);
        return "leaderboard";
    }
}
