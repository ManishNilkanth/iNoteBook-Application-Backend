package com.Study.inotebook.Controller;

import com.Study.inotebook.Payload.NoteBookRequest;
import com.Study.inotebook.Payload.NotebookResponse;
import com.Study.inotebook.Service.NotebookService;
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
@RequestMapping("/api/v1/notebooks")
public class NotebookController {

    private final NotebookService notebookService;

    @PostMapping
    public ResponseEntity<NotebookResponse> createNotebook(@RequestBody NoteBookRequest request)
    {
        NotebookResponse response = notebookService.createNotebook(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{notebookId}")
    public ResponseEntity<NotebookResponse> getNotebookById(@PathVariable Long notebookId)
    {
        NotebookResponse response = notebookService.getNotebookById(notebookId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<NotebookResponse>> getAllNotebooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate,desc") String[] sort
    )
    {
        Sort sortOrder = Sort.by(sort[0]).descending();
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        List<NotebookResponse> response = notebookService.getAllNotebooks(pageable);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotebookResponse>> getNotebookByUserId(@PathVariable Long userId)
    {
        List<NotebookResponse> response = notebookService.getAllNoteBooksByUserId(userId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @PutMapping("/{notebookId}")
    public ResponseEntity<NotebookResponse> updateNotebookById(@PathVariable Long notebookId,@RequestBody NoteBookRequest request)
    {
        NotebookResponse response = notebookService.updateNotebookById(notebookId,request);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @DeleteMapping("/{notebookId}")
    public ResponseEntity<Void> deleteNotebookById(@PathVariable Long notebookId, Principal principal)
    {
        String requestingUsername = principal.getName();
        notebookService.deleteNotebookById(notebookId,requestingUsername);
        return ResponseEntity.accepted().build();
    }
}
