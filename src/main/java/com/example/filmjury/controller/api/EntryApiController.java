package com.example.filmjury.controller.api;

import com.example.filmjury.entity.Entry;
import com.example.filmjury.service.EntryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entries")
public class EntryApiController {

    private final EntryService entryService;

    @Autowired
    public EntryApiController(EntryService entryService) {
        this.entryService = entryService;
    }

    @GetMapping
    public List<Entry> getAllEntries() {
        return entryService.getAllEntries();
    }

    @GetMapping("/{id}")
    public Entry getEntryById(@PathVariable Long id) {
        return entryService.getEntryById(id);
    }

    @PostMapping
    public ResponseEntity<Entry> createEntry(@Valid @RequestBody Entry entry) {
        Entry saved = entryService.saveEntry(entry);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Entry updateEntry(@PathVariable Long id, @Valid @RequestBody Entry entry) {
        return entryService.updateEntry(id, entry);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
        entryService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{entryId}/judges/{judgeId}")
    public ResponseEntity<Entry> assignJudge(@PathVariable Long entryId, @PathVariable Long judgeId) {
        Entry updated = entryService.assignJudgeToEntry(entryId, judgeId);
        return ResponseEntity.ok(updated);
    }
}
