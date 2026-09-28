package com.example.filmjury.service;

import com.example.filmjury.dto.LeaderboardItemDTO;
import com.example.filmjury.entity.Entry;
import com.example.filmjury.repository.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LeaderboardService {

    private final EntryRepository entryRepository;

    @Autowired
    public LeaderboardService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    public List<LeaderboardItemDTO> getLeaderboard() {
        List<Entry> entries = entryRepository.findAll();

        // Only show films that have at least one submitted score
        List<Entry> scoredEntries = entries.stream()
                .filter(e -> e.getScoreCards() != null && !e.getScoreCards().isEmpty())
                .sorted(Comparator.comparingDouble(Entry::getAverageScore).reversed())
                .collect(Collectors.toList());

        List<LeaderboardItemDTO> leaderboard = new ArrayList<>();
        int rank = 1;
        for (Entry entry : scoredEntries) {
            LeaderboardItemDTO item = new LeaderboardItemDTO(
                    rank++,
                    entry.getId(),
                    entry.getTitle(),
                    entry.getTeamName(),
                    entry.getGenre(),
                    entry.getAverageScore(),
                    entry.getJudgesScoredCount()
            );
            leaderboard.add(item);
        }

        return leaderboard;
    }
}
