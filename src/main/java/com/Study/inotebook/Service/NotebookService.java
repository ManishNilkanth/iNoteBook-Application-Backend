package com.Study.inotebook.Service;

import com.Study.inotebook.Payload.NoteBookRequest;
import com.Study.inotebook.Payload.NotebookResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotebookService {
    NotebookResponse createNotebook(NoteBookRequest request);

    NotebookResponse getNotebookById(Long notebookId);

    List<NotebookResponse> getAllNotebooks(Pageable pageable);

    List<NotebookResponse> getAllNoteBooksByUserId(Long userId);

    NotebookResponse updateNotebookById(Long notebookId, NoteBookRequest request);

    void deleteNotebookById(Long notebookId, String requestingUsername);
}
