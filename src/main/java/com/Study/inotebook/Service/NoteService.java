package com.Study.inotebook.Service;

import com.Study.inotebook.Payload.NoteRequest;
import com.Study.inotebook.Payload.NoteResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoteService {
    NoteResponse createNote(NoteRequest request);

    NoteResponse getNoteById(Long noteId);

    List<NoteResponse> getAllNote(Pageable pageable);

    List<NoteResponse> getAllNoteByUserId(Long userId);

    List<NoteResponse> getAllNoteByNotebookId(Long notebookId);

    NoteResponse updateNoteById(Long noteId, NoteRequest request);

    void deleteNoteById(Long noteId, String requestingUsername);
}
