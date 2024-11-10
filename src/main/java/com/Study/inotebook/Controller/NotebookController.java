package com.Study.inotebook.Controller;

import com.Study.inotebook.DTO.NotebookDTO;
import com.Study.inotebook.Entities.Notebook;
import com.Study.inotebook.Service.NotebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notebooks")
public class NotebookController {

    private final NotebookService notebookService;

    @PostMapping
    public ResponseEntity<NotebookDTO> createNotebook(@RequestBody NotebookDTO request)
    {
        NotebookDTO response = notebookService.createNotebook(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{notebookId}")
    public ResponseEntity<NotebookDTO> getNotebookById(@PathVariable Long notebookId)
    {
        NotebookDTO response = notebookService.getNotebookById(notebookId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<NotebookDTO>> getAllNotebooks()
    {
        List<NotebookDTO> response = notebookService.getAllNotebooks();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotebookDTO>> getNotebookByUserId(@PathVariable Long userId)
    {
        List<NotebookDTO> response = notebookService.getAllNoteBooksByUserId(userId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @PutMapping("/{notebookId}")
    public ResponseEntity<NotebookDTO> updateNotebookById(@PathVariable Long notebookId,@RequestBody NotebookDTO request)
    {
        NotebookDTO response = notebookService.updateNotebookById(notebookId,request);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @DeleteMapping("/{notebookId}")
    public ResponseEntity<Void> deleteNotebookById(@PathVariable Long notebookId)
    {
        notebookService.deleteNotebookById(notebookId);
        return ResponseEntity.accepted().build();
    }
}
