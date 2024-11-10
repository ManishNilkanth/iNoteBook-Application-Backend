package com.Study.inotebook.Repository;

import com.Study.inotebook.Entities.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteBookRepository extends JpaRepository<Notebook,Long> {
    Optional<List<Notebook>> findAllByUserId(Long userId);
}
