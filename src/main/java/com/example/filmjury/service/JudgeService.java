package com.example.filmjury.service;

import com.example.filmjury.entity.Judge;
import com.example.filmjury.exception.ResourceNotFoundException;
import com.example.filmjury.repository.JudgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JudgeService {

    private final JudgeRepository judgeRepository;

    @Autowired
    public JudgeService(JudgeRepository judgeRepository) {
        this.judgeRepository = judgeRepository;
    }

    @Transactional(readOnly = true)
    public List<Judge> getAllJudges() {
        return judgeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Judge getJudgeById(Long id) {
        return judgeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Judge not found with ID: " + id));
    }

    public Judge saveJudge(Judge judge) {
        if (judgeRepository.existsByEmail(judge.getEmail())) {
            throw new IllegalArgumentException("A judge with email " + judge.getEmail() + " already exists.");
        }
        return judgeRepository.save(judge);
    }

    public Judge updateJudge(Long id, Judge judgeDetails) {
        Judge existing = getJudgeById(id);
        if (judgeRepository.existsByEmailAndIdNot(judgeDetails.getEmail(), id)) {
            throw new IllegalArgumentException("Email " + judgeDetails.getEmail() + " is already used by another judge.");
        }
        existing.setName(judgeDetails.getName());
        existing.setEmail(judgeDetails.getEmail());
        return judgeRepository.save(existing);
    }

    public void deleteJudge(Long id) {
        Judge existing = getJudgeById(id);
        judgeRepository.delete(existing);
    }
}
