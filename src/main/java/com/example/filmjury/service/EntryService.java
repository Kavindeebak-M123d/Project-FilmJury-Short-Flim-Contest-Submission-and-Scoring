package com.example.filmjury.service;

import com.example.filmjury.entity.Entry;
import com.example.filmjury.entity.Judge;
import com.example.filmjury.exception.ResourceNotFoundException;
import com.example.filmjury.repository.EntryRepository;
import com.example.filmjury.repository.JudgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EntryService {

    private final EntryRepository entryRepository;
    private final JudgeRepository judgeRepository;

    @Autowired
    public EntryService(EntryRepository entryRepository, JudgeRepository judgeRepository) {
        this.entryRepository = entryRepository;
        this.judgeRepository = judgeRepository;
    }

    @Transactional(readOnly = true)
    public List<Entry> getAllEntries() {
        return entryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Entry getEntryById(Long id) {
        return entryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with ID: " + id));
    }

    public Entry saveEntry(Entry entry) {
        if (entry.getSubmissionDate() == null) {
            entry.setSubmissionDate(LocalDate.now());
        }
        return entryRepository.save(entry);
    }

    public Entry updateEntry(Long id, Entry updated) {
        Entry existing = getEntryById(id);
        existing.setTitle(updated.getTitle());
        existing.setGenre(updated.getGenre());
        existing.setDescription(updated.getDescription());
        existing.setVideoLink(updated.getVideoLink());
        existing.setTeamName(updated.getTeamName());
        existing.setTeamMembers(updated.getTeamMembers());
        return entryRepository.save(existing);
    }

    public void deleteEntry(Long id) {
        Entry existing = getEntryById(id);
        entryRepository.delete(existing);
    }

    @Transactional(readOnly = true)
    public List<Entry> filterAndSortEntries(String keyword, String genre, String sortBy) {
        List<Entry> entries = entryRepository.findAll();

        if (keyword != null && !keyword.trim().isEmpty()) {
            String lower = keyword.toLowerCase().trim();
            entries = entries.stream()
                    .filter(e -> e.getTitle().toLowerCase().contains(lower) || e.getTeamName().toLowerCase().contains(lower))
                    .collect(Collectors.toList());
        }

        if (genre != null && !genre.trim().isEmpty() && !"ALL".equalsIgnoreCase(genre)) {
            entries = entries.stream()
                    .filter(e -> e.getGenre().equalsIgnoreCase(genre))
                    .collect(Collectors.toList());
        }

        if (sortBy != null) {
            switch (sortBy.toLowerCase()) {
                case "score":
                    entries.sort(Comparator.comparingDouble(Entry::getAverageScore).reversed());
                    break;
                case "title":
                    entries.sort(Comparator.comparing(Entry::getTitle, String.CASE_INSENSITIVE_ORDER));
                    break;
                case "date":
                default:
                    entries.sort(Comparator.comparing(Entry::getSubmissionDate).reversed());
                    break;
            }
        }

        return entries;
    }

    public Entry assignJudgeToEntry(Long entryId, Long judgeId) {
        Entry entry = getEntryById(entryId);
        Judge judge = judgeRepository.findById(judgeId)
                .orElseThrow(() -> new ResourceNotFoundException("Judge not found with ID: " + judgeId));

        if (!entry.getJudges().contains(judge)) {
            entry.getJudges().add(judge);
            return entryRepository.save(entry);
        }
        return entry;
    }

    public Entry updateAssignedJudges(Long entryId, List<Long> judgeIds) {
        Entry entry = getEntryById(entryId);
        Set<Judge> selectedJudges = new HashSet<>();
        if (judgeIds != null && !judgeIds.isEmpty()) {
            for (Long judgeId : judgeIds) {
                Judge judge = judgeRepository.findById(judgeId)
                        .orElseThrow(() -> new ResourceNotFoundException("Judge not found with ID: " + judgeId));
                selectedJudges.add(judge);
            }
        }
        entry.setJudges(selectedJudges);
        return entryRepository.save(entry);
    }

    public Entry removeJudgeFromEntry(Long entryId, Long judgeId) {
        Entry entry = getEntryById(entryId);
        Judge judge = judgeRepository.findById(judgeId)
                .orElseThrow(() -> new ResourceNotFoundException("Judge not found with ID: " + judgeId));

        entry.getJudges().remove(judge);
        return entryRepository.save(entry);
    }
}
