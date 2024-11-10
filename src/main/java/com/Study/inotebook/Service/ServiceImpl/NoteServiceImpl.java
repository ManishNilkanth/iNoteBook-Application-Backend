package com.Study.inotebook.Service.ServiceImpl;

import com.Study.inotebook.DTO.NoteDTO;
import com.Study.inotebook.Entities.Note;
import com.Study.inotebook.Entities.Notebook;
import com.Study.inotebook.Entities.User;
import com.Study.inotebook.Exceptions.NoteNotFoundException;
import com.Study.inotebook.Exceptions.NotebookNotFoundException;
import com.Study.inotebook.Exceptions.UserNotFoundException;
import com.Study.inotebook.Repository.NoteBookRepository;
import com.Study.inotebook.Repository.NoteRepository;
import com.Study.inotebook.Repository.UserRepository;
import com.Study.inotebook.Service.NoteService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Override
    public NoteDTO createNote(NoteDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new UserNotFoundException("user is not found with this user Id"));
        Notebook notebook = noteBookRepository.findById(request.getNotebookId())
                .orElseThrow(()-> new NotebookNotFoundException("notebook is not found with this notebookId"));

        Note note = modelMapper.map(request,Note.class);
        note.setUser(user);
        note.setNotebook(notebook);
        user.setNotes(new ArrayList<>(List.of(note)));
        notebook.setNotes(new ArrayList<>(List.of(note)));

        Note savedNote = noteRepository.save(note);
        userRepository.save(user);
        noteBookRepository.save(notebook);

        return modelMapper.map(savedNote,NoteDTO.class);
    }

    @Override
    public NoteDTO getNoteById(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(()-> new NoteNotFoundException("note is not found with this note id"));
        return modelMapper.map(note,NoteDTO.class);
    }

    @Override
    public List<NoteDTO> getAllNote() {
       return noteRepository.findAll()
               .stream()
               .map(note -> modelMapper.map(note,NoteDTO.class))
               .collect(Collectors.toList());
    }

    @Override
    public List<NoteDTO> getAllNoteByUserId(Long userId) {
        if(!userRepository.existsById(userId))
        {
            throw new UserNotFoundException("user is not found with this user id");
        }
        List<Note> notes = noteRepository.findAllByUserId(userId).orElseGet(Collections::emptyList);
        return notes.stream()
                .map(note -> modelMapper.map(note,NoteDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<NoteDTO> getAllNoteByNotebookId(Long notebookId) {
        if(!noteBookRepository.existsById(notebookId))
        {
            throw new NotebookNotFoundException("notebook is not found with this notebook id");
        }
        List<Note> notes = noteRepository.findAllByNotebookId(notebookId).orElseGet(Collections::emptyList);
        return notes.stream()
                .map(note -> modelMapper.map(note,NoteDTO.class))
                .collect(Collectors.toList());

    }

    @Override
    public NoteDTO updateNoteById(Long noteId, NoteDTO request) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(()-> new NoteNotFoundException("note is not found with this note id"));

        updateNoteData(note, request);
        Note saveNote = noteRepository.save(note);
        return modelMapper.map(saveNote,NoteDTO.class);
    }

    private void updateNoteData(Note note, NoteDTO request) {

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
    public void deleteNoteById(Long noteId) {
        if(noteRepository.existsById(noteId))
        {
            noteRepository.deleteById(noteId);
        }
        throw new NoteNotFoundException("Note is not found with this noteId");
    }
}
