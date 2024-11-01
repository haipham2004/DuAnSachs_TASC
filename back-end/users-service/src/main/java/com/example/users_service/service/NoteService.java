package com.example.users_service.service;

import com.example.users_service.dto.NoteDto;
import com.example.users_service.entity.Note;

import java.util.List;

public interface NoteService {

    Note createNoteForUser(String username, String content);

    Note updateNoteForUser(Integer noteId, String content, String username);

    void deleteNoteForUser(Integer noteId, String username);

    List<Note> getNotesForUser(String username);
}
