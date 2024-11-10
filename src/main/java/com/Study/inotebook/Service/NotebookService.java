package com.Study.inotebook.Service;

import com.Study.inotebook.DTO.NotebookDTO;

import java.util.List;

public interface NotebookService {
    NotebookDTO createNotebook(NotebookDTO request);

    NotebookDTO getNotebookById(Long notebookId);

    List<NotebookDTO> getAllNotebooks();

    List<NotebookDTO> getAllNoteBooksByUserId(Long userId);

    NotebookDTO updateNotebookById(Long notebookId, NotebookDTO request);

    void deleteNotebookById(Long notebookId);
}
