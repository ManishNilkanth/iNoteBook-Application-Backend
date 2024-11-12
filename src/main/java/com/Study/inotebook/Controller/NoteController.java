package com.Study.inotebook.Controller;

import com.Study.inotebook.Payload.NoteRequest;
import com.Study.inotebook.Payload.NoteResponse;
import com.Study.inotebook.Service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notes")
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<NoteResponse> createNote(@RequestBody NoteRequest request)
    {
        NoteResponse response = noteService.createNote(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<NoteResponse> getNoteById(@PathVariable Long noteId)
    {
        NoteResponse response = noteService.getNoteById(noteId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<NoteResponse>> getAllNote(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate,desc") String[] sort
    )
    {
        Sort sortOrder = Sort.by(sort[0]).descending();
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        List<NoteResponse> response = noteService.getAllNote(pageable);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NoteResponse>> getNoteByUserId(@PathVariable Long userId)
    {
        List<NoteResponse> response = noteService.getAllNoteByUserId(userId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping("/notebook/{notebookId}")
    public ResponseEntity<List<NoteResponse>> getNoteByNotebookId(@PathVariable Long notebookId)
    {
        List<NoteResponse> response = noteService.getAllNoteByNotebookId(notebookId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @PutMapping("/{noteId}")
    public ResponseEntity<NoteResponse> updateNoteById(@PathVariable Long noteId,@RequestBody NoteRequest request)
    {
        NoteResponse response = noteService.updateNoteById(noteId,request);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNoteById(@PathVariable Long noteId, Principal principal)
    {
        String requestingUsername = principal.getName();
        noteService.deleteNoteById(noteId,requestingUsername);
        return ResponseEntity.accepted().build();
    }
}
