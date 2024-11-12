package com.Study.inotebook.Service.ServiceImpl;

import com.Study.inotebook.Exceptions.UnauthorizedException;
import com.Study.inotebook.Payload.NoteBookRequest;
import com.Study.inotebook.Entities.Notebook;
import com.Study.inotebook.Entities.User;
import com.Study.inotebook.Exceptions.NotebookNotFoundException;
import com.Study.inotebook.Exceptions.UserNotFoundException;
import com.Study.inotebook.Payload.NotebookResponse;
import com.Study.inotebook.Repository.NoteBookRepository;
import com.Study.inotebook.Repository.UserRepository;
import com.Study.inotebook.Service.NotebookService;
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
public class NotebookServiceImpl implements NotebookService {

    private final NoteBookRepository noteBookRepository;

    private final UserRepository userRepository;

    @Override
    public NotebookResponse createNotebook(NoteBookRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new UserNotFoundException("user is not found with this userId"));

        Notebook notebook = mapToNoteBook(request);
        notebook.setUser(user);
        user.setNotebooks(new ArrayList<>(List.of(notebook)));
        Notebook savedNotebook = noteBookRepository.save(notebook);
        userRepository.save(user);
        return mapToNoteBookResponse(savedNotebook);
    }



    @Override
    public NotebookResponse getNotebookById(Long notebookId) {
        Notebook notebook = noteBookRepository.findById(notebookId).
                orElseThrow(()-> new NotebookNotFoundException("notebook is not found with this notebookId"));
        return mapToNoteBookResponse(notebook);
    }

    @Override
    public List<NotebookResponse> getAllNotebooks(Pageable pageable) {
        return noteBookRepository.findAll(pageable)
                .stream()
                .map(this::mapToNoteBookResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotebookResponse> getAllNoteBooksByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("user is not found with this user Id"));
        List<Notebook> notebooks = noteBookRepository.findAllByUserId(userId).orElseGet(Collections::emptyList);
        return notebooks.stream()
                .map(this::mapToNoteBookResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NotebookResponse updateNotebookById(Long notebookId, NoteBookRequest request) {
        Notebook notebook = noteBookRepository.findById(notebookId)
                .orElseThrow(()-> new NotebookNotFoundException("notebook is not found with this notebookId"));
        updateNotebookData(notebook,request);
        Notebook savedNotebook = noteBookRepository.save(notebook);
        return mapToNoteBookResponse(savedNotebook);
    }

    private void updateNotebookData(Notebook notebook, NoteBookRequest request) {
        if(StringUtils.isNotBlank(request.getTitle()))
        {
            notebook.setTitle(request.getTitle());
        }
        if(StringUtils.isNotBlank(request.getDescription()))
        {
            notebook.setDescription(request.getDescription());
        }
        notebook.setIsArchived(request.getIsArchived());
    }

    @Override
    public void deleteNotebookById(Long notebookId, String requestingUsername) {
        Notebook notebook = noteBookRepository.findById(notebookId)
                .orElseThrow(()-> new NotebookNotFoundException("notebook is not found with this notebookId"));

        if(!notebook.getUser().getUsername().equals(requestingUsername))
        {
            throw new UnauthorizedException("User not authorized to delete this note");
        }
        noteBookRepository.delete(notebook);
    }

    private NotebookResponse mapToNoteBookResponse(Notebook notebook) {
        return NotebookResponse.builder()
                .id(notebook.getId())
                .userId(notebook.getUser().getId())
                .title(notebook.getTitle())
                .description(notebook.getDescription())
                .isArchived(notebook.getIsArchived())
                .createdDate(notebook.getCreatedDate())
                .lastUpdatedDate(notebook.getLastUpdatedDate())
                .build();
    }

    private Notebook mapToNoteBook(NoteBookRequest request) {
        return Notebook.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .isArchived(request.getIsArchived())
                .build();
    }
}
