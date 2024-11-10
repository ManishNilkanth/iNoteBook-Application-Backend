package com.Study.inotebook.Controller;

import com.Study.inotebook.DTO.NoteDTO;
import com.Study.inotebook.DTO.NotebookDTO;
import com.Study.inotebook.Service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notes")
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<NoteDTO> createNote(@RequestBody NoteDTO request)
    {
        NoteDTO response = noteService.createNote(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<NoteDTO> getNoteById(@PathVariable Long noteId)
    {
        NoteDTO response = noteService.getNoteById(noteId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<NoteDTO>> getAllNote()
    {
        List<NoteDTO> response = noteService.getAllNote();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NoteDTO>> getNoteByUserId(@PathVariable Long userId)
    {
        List<NoteDTO> response = noteService.getAllNoteByUserId(userId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping("/notebook/{notebookId}")
    public ResponseEntity<List<NoteDTO>> getNoteByNotebookId(@PathVariable Long notebookId)
    {
        List<NoteDTO> response = noteService.getAllNoteByNotebookId(notebookId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @PutMapping("/{noteId}")
    public ResponseEntity<NoteDTO> updateNoteById(@PathVariable Long noteId,@RequestBody NoteDTO request)
    {
        NoteDTO response = noteService.updateNoteById(noteId,request);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNoteById(@PathVariable Long noteId)
    {
        noteService.deleteNoteById(noteId);
        return ResponseEntity.accepted().build();
    }
}
