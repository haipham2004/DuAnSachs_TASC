package com.example.users_service.mapper;

import com.example.users_service.dto.NoteDto;
import com.example.users_service.entity.Note;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface NoteMapper {

    NoteDto mapToDto(Note note);

    Note mapToNote(NoteDto noteDto);
}
