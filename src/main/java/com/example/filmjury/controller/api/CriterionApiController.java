package com.example.filmjury.controller.api;

import com.example.filmjury.entity.Criterion;
import com.example.filmjury.service.CriterionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/criteria")
public class CriterionApiController {

    private final CriterionService criterionService;

    @Autowired
    public CriterionApiController(CriterionService criterionService) {
        this.criterionService = criterionService;
    }

    @GetMapping
    public List<Criterion> getAllCriteria() {
        return criterionService.getAllCriteria();
    }

    @PostMapping
    public ResponseEntity<Criterion> createCriterion(@Valid @RequestBody Criterion criterion) {
        Criterion saved = criterionService.saveCriterion(criterion);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Criterion updateCriterion(@PathVariable Long id, @Valid @RequestBody Criterion criterion) {
        return criterionService.updateCriterion(id, criterion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCriterion(@PathVariable Long id) {
        criterionService.deleteCriterion(id);
        return ResponseEntity.noContent().build();
    }
}
