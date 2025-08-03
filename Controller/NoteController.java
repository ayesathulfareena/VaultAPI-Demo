package com.nearli.backend.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nearli.backend.model.Note;
import com.nearli.backend.service.NoteService;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    /**
     * Save a new note for the logged-in user.
     */
    @PostMapping("/save")
    public Note saveNote(@RequestParam String placeName,
                         @RequestParam String note,
                         Principal principal) {
        String email = principal.getName();
        return noteService.saveNote(email, placeName, note);
    }

    /**
     * Get all notes for the logged-in user.
     */
    @GetMapping("/get")
    public List<Note> getNotes(Principal principal) {
        return noteService.getNotesByEmail(principal.getName());
    }

    /**
     * Get today's reminders for the logged-in user.
     */
    @GetMapping("/reminders/today")
    public List<Note> getTodayReminders(Principal principal) {
        return noteService.getTodayReminders(principal.getName());
    }

    /**
     * Delete a note by ID.
     */
    @DeleteMapping("/delete")
    public String deleteNote(@RequestParam Long id) {
        boolean deleted = noteService.deleteNoteById(id);
        return deleted ? "Note deleted" : "Note not found";
    }

    /**
     * Update a note.
     */
    @PutMapping("/update")
    public ResponseEntity<String> updateNote(@RequestParam Long id,
                                             @RequestParam String title,
                                             @RequestParam String message,
                                             @RequestParam(required = false) LocalDateTime reminderTime) {
        boolean updated = noteService.updateNote(id, title, message, reminderTime);
        if (updated) {
            return ResponseEntity.ok("Note updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found.");
        }
    }

}