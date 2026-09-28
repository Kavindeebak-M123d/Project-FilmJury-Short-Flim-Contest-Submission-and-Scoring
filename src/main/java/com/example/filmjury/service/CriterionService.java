package com.example.filmjury.service;

import com.example.filmjury.entity.Criterion;
import com.example.filmjury.exception.ResourceNotFoundException;
import com.example.filmjury.repository.CriterionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CriterionService {

    private final CriterionRepository criterionRepository;

    @Autowired
    public CriterionService(CriterionRepository criterionRepository) {
        this.criterionRepository = criterionRepository;
    }

    @Transactional(readOnly = true)
    public List<Criterion> getAllCriteria() {
        return criterionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Criterion getCriterionById(Long id) {
        return criterionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Criterion not found with ID: " + id));
    }

    public Criterion saveCriterion(Criterion criterion) {
        if (criterion.getMaxScore() == null || criterion.getMaxScore() < 1) {
            criterion.setMaxScore(10);
        }
        return criterionRepository.save(criterion);
    }

    public Criterion updateCriterion(Long id, Criterion updated) {
        Criterion existing = getCriterionById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        if (updated.getMaxScore() != null && updated.getMaxScore() >= 1) {
            existing.setMaxScore(updated.getMaxScore());
        }
        return criterionRepository.save(existing);
    }

    public void deleteCriterion(Long id) {
        Criterion existing = getCriterionById(id);
        criterionRepository.delete(existing);
    }

    public void initDefaultCriteria() {
        if (criterionRepository.count() == 0) {
            saveCriterion(new Criterion("Story", "Narrative structure, emotional impact, and plot progression", 10));
            saveCriterion(new Criterion("Creativity", "Originality, innovation, and artistic vision", 10));
            saveCriterion(new Criterion("Cinematography", "Camera work, lighting, composition, and visual aesthetics", 10));
            saveCriterion(new Criterion("Acting", "Performance quality, character believability, and expression", 10));
            saveCriterion(new Criterion("Editing", "Pacing, transitions, sound sync, and overall flow", 10));
        }
    }
}
