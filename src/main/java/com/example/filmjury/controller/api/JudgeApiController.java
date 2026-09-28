package com.example.filmjury.controller.api;

import com.example.filmjury.entity.Judge;
import com.example.filmjury.service.JudgeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/judges")
public class JudgeApiController {

    private final JudgeService judgeService;

    @Autowired
    public JudgeApiController(JudgeService judgeService) {
        this.judgeService = judgeService;
    }

    @GetMapping
    public List<Judge> getAllJudges() {
        return judgeService.getAllJudges();
    }

    @GetMapping("/{id}")
    public Judge getJudgeById(@PathVariable Long id) {
        return judgeService.getJudgeById(id);
    }

    @PostMapping
    public ResponseEntity<Judge> createJudge(@Valid @RequestBody Judge judge) {
        Judge saved = judgeService.saveJudge(judge);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Judge updateJudge(@PathVariable Long id, @Valid @RequestBody Judge judge) {
        return judgeService.updateJudge(id, judge);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJudge(@PathVariable Long id) {
        judgeService.deleteJudge(id);
        return ResponseEntity.noContent().build();
    }
}
