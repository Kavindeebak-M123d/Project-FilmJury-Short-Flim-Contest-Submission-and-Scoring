package com.example.filmjury.controller.api;

import com.example.filmjury.dto.ScoreCardRequestDTO;
import com.example.filmjury.entity.ScoreCard;
import com.example.filmjury.service.ScoreCardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scorecards")
public class ScoreCardApiController {

    private final ScoreCardService scoreCardService;

    @Autowired
    public ScoreCardApiController(ScoreCardService scoreCardService) {
        this.scoreCardService = scoreCardService;
    }

    @GetMapping
    public List<ScoreCard> getAllScoreCards() {
        return scoreCardService.getAllScoreCards();
    }

    @PostMapping
    public ResponseEntity<List<ScoreCard>> createScoreCard(@Valid @RequestBody ScoreCardRequestDTO request) {
        List<ScoreCard> created = scoreCardService.submitScores(
                request.getEntryId(),
                request.getJudgeId(),
                request.getScores(),
                request.getComments()
        );
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
