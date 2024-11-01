package com.example.users_service.service;

import com.example.users_service.dto.NoteDto;
import com.example.users_service.entity.Note;

import java.util.List;

public interface NoteService {

    NoteDto createNoteForUser(NoteDto noteDto);

    NoteDto updateNoteForUser(Integer noteId, NoteDto noteDto);

    void deleteNoteForUser(Integer noteId, String username);

    List<NoteDto> getNotesForUser(String username);
}
