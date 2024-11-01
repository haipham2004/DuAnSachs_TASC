package com.example.users_service.service.imp;

import com.example.users_service.dto.NoteDto;
import com.example.users_service.entity.Note;
import com.example.users_service.exception.ResourceNotfound;
import com.example.users_service.mapper.NoteMapper;
import com.example.users_service.repository.NoteRepository;
import com.example.users_service.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteServiceImp implements NoteService {

    private NoteRepository noteRepository;

    private NoteMapper noteMapper;

    @Autowired
    public NoteServiceImp(NoteRepository noteRepository, NoteMapper noteMapper) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
    }


    @Override
    public NoteDto createNoteForUser(NoteDto noteDto) {
        Note note=noteMapper.mapToNote(noteDto);
        Note noteSave=noteRepository.save(note);
        return noteMapper.mapToDto(noteSave);
    }

    @Override
    public NoteDto updateNoteForUser(Integer noteId, NoteDto noteDto) {
        Note note=noteRepository.findById(noteId).
                orElseThrow(() ->new ResourceNotfound("Không tồn tại ID note: "+noteId));
        note.setContent(noteDto.getContent());
        Note noteUpdate=noteRepository.save(note);
        return noteMapper.mapToDto(noteUpdate);
    }

    @Override
    public void deleteNoteForUser(Integer noteId , String username) {
        Note note=noteRepository.findById(noteId).
                orElseThrow(() ->new ResourceNotfound("Không tồn tại ID note: "+noteId));
        noteRepository.deleteById(noteId);
    }

    @Override
    public List<NoteDto> getNotesForUser(String username) {
        return  noteRepository.findByOwnerUsername(username).stream()
                .map((note) ->noteMapper.mapToDto(note)).collect(Collectors.toList());
    }
}
