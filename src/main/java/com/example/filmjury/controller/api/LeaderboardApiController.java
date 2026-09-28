package com.example.filmjury.controller.api;

import com.example.filmjury.dto.LeaderboardItemDTO;
import com.example.filmjury.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardApiController {

    private final LeaderboardService leaderboardService;

    @Autowired
    public LeaderboardApiController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping
    public List<LeaderboardItemDTO> getLeaderboard() {
        return leaderboardService.getLeaderboard();
    }
}
