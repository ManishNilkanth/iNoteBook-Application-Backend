package com.Study.inotebook.Service;

import com.Study.inotebook.DTO.NoteDTO;

import java.util.List;

public interface NoteService {
    NoteDTO createNote(NoteDTO request);

    NoteDTO getNoteById(Long noteId);

    List<NoteDTO> getAllNote();

    List<NoteDTO> getAllNoteByUserId(Long userId);

    List<NoteDTO> getAllNoteByNotebookId(Long notebookId);

    NoteDTO updateNoteById(Long noteId, NoteDTO request);

    void deleteNoteById(Long noteId);
}
