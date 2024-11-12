package com.Study.inotebook.Repository;

import com.Study.inotebook.Entities.Note;
import com.Study.inotebook.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note,Long> {
    Optional<List<Note>> findAllByUserId(Long userId);
    Optional<List<Note>> findAllByNotebookId(Long notebookId);
    Page<Note> findAll(Pageable pageable);
}
