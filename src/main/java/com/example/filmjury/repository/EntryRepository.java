package com.example.filmjury.repository;

import com.example.filmjury.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findByTitleContainingIgnoreCaseOrTeamNameContainingIgnoreCase(String title, String teamName);
    List<Entry> findByGenreIgnoreCase(String genre);
    
    @Query("SELECT DISTINCT e FROM Entry e JOIN e.scoreCards s")
    List<Entry> findEntriesWithScores();
}
