package com.Study.inotebook.Service.ServiceImpl;

import com.Study.inotebook.Entities.Note;
import com.Study.inotebook.Entities.Notebook;
import com.Study.inotebook.Entities.User;
import com.Study.inotebook.Exceptions.NoteNotFoundException;
import com.Study.inotebook.Exceptions.NotebookNotFoundException;
import com.Study.inotebook.Exceptions.UnauthorizedException;
import com.Study.inotebook.Exceptions.UserNotFoundException;
import com.Study.inotebook.Payload.NoteRequest;
import com.Study.inotebook.Payload.NoteResponse;
import com.Study.inotebook.Repository.NoteBookRepository;
import com.Study.inotebook.Repository.NoteRepository;
import com.Study.inotebook.Repository.UserRepository;
import com.Study.inotebook.Service.NoteService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NoteBookRepository noteBookRepository;


    @Override
    public NoteResponse createNote(NoteRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new UserNotFoundException("user is not found with this user Id"));
        Notebook notebook = noteBookRepository.findById(request.getNotebookId())
                .orElseThrow(()-> new NotebookNotFoundException("notebook is not found with this notebookId"));

        Note note = mapToNote(request);
        note.setUser(user);
        note.setNotebook(notebook);
        user.setNotes(new ArrayList<>(List.of(note)));
        notebook.setNotes(new ArrayList<>(List.of(note)));

        Note savedNote = noteRepository.save(note);
        userRepository.save(user);
        noteBookRepository.save(notebook);

        return mapToNoteResponse(savedNote);
    }

    @Override
    public NoteResponse getNoteById(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(()-> new NoteNotFoundException("note is not found with this note id"));
        return mapToNoteResponse(note);
    }

    @Override
    public List<NoteResponse> getAllNote(Pageable pageable) {
       return noteRepository.findAll(pageable)
               .stream()
               .map(this::mapToNoteResponse)
               .collect(Collectors.toList());
    }

    @Override
    public List<NoteResponse> getAllNoteByUserId(Long userId) {
        if(!userRepository.existsById(userId))
        {
            throw new UserNotFoundException("user is not found with this user id");
        }
        List<Note> notes = noteRepository.findAllByUserId(userId).orElseGet(Collections::emptyList);
        return notes.stream()
                .map(this::mapToNoteResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoteResponse> getAllNoteByNotebookId(Long notebookId) {
        if(!noteBookRepository.existsById(notebookId))
        {
            throw new NotebookNotFoundException("notebook is not found with this notebook id");
        }
        List<Note> notes = noteRepository.findAllByNotebookId(notebookId).orElseGet(Collections::emptyList);
        return notes.stream()
                .map(this::mapToNoteResponse)
                .collect(Collectors.toList());

    }

    @Override
    public NoteResponse updateNoteById(Long noteId, NoteRequest request) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(()-> new NoteNotFoundException("note is not found with this note id"));

        updateNoteData(note, request);
        Note saveNote = noteRepository.save(note);
        return mapToNoteResponse(note);
    }

    private void updateNoteData(Note note, NoteRequest request) {

        if(StringUtils.isNotBlank(request.getTitle()))
        {
            note.setTitle(request.getTitle());
        }
        if(StringUtils.isNotBlank(request.getContent()))
        {
            note.setContent(request.getContent());
        }
        note.setIsArchived(request.getIsArchived());

        note.setIsFavorite(request.getIsFavorite());
    }

    @Override
    public void deleteNoteById(Long noteId, String requestingUsername) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(()-> new NoteNotFoundException("Note is not found with this noteId"));

        if(!note.getUser().getUsername().equals(requestingUsername))
        {
            throw new UnauthorizedException("User not authorized to delete this note");
        }
        noteRepository.delete(note);
    }

    private NoteResponse mapToNoteResponse(Note savedNote) {
        return NoteResponse.builder()
                .id(savedNote.getId())
                .title(savedNote.getTitle())
                .content(savedNote.getContent())
                .isFavorite(savedNote.getIsFavorite())
                .createdDate(savedNote.getCreatedDate())
                .lastUpdatedDate(savedNote.getLastUpdatedDate())
                .notebookId(savedNote.getNotebook().getId())
                .build();
    }

    private Note mapToNote(NoteRequest request) {
        return Note.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .isFavorite(request.getIsFavorite())
                .isArchived(request.getIsArchived())
                .build();
    }
}
