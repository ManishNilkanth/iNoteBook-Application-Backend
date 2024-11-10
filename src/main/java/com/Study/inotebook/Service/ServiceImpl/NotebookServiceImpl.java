package com.Study.inotebook.Service.ServiceImpl;

import com.Study.inotebook.DTO.NotebookDTO;
import com.Study.inotebook.Entities.Notebook;
import com.Study.inotebook.Entities.User;
import com.Study.inotebook.Exceptions.NotebookNotFoundException;
import com.Study.inotebook.Exceptions.UserNotFoundException;
import com.Study.inotebook.Repository.NoteBookRepository;
import com.Study.inotebook.Repository.UserRepository;
import com.Study.inotebook.Service.NotebookService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotebookServiceImpl implements NotebookService {

    private final NoteBookRepository noteBookRepository;

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public NotebookDTO createNotebook(NotebookDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new UserNotFoundException("user is not found with this userId"));

        Notebook notebook = modelMapper.map(request,Notebook.class);
        notebook.setUser(user);
        user.setNotebooks(new ArrayList<>(List.of(notebook)));
        Notebook savedNotebook = noteBookRepository.save(notebook);
        userRepository.save(user);
        return modelMapper.map(savedNotebook,NotebookDTO.class);
    }

    @Override
    public NotebookDTO getNotebookById(Long notebookId) {
        Notebook notebook = noteBookRepository.findById(notebookId).
                orElseThrow(()-> new NotebookNotFoundException("notebook is not found with this notebookId"));
        return modelMapper.map(notebook,NotebookDTO.class);
    }

    @Override
    public List<NotebookDTO> getAllNotebooks() {
        return noteBookRepository.findAll()
                .stream()
                .map(notebook -> modelMapper.map(notebook,NotebookDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<NotebookDTO> getAllNoteBooksByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("user is not found with this user Id"));
        List<Notebook> notebooks = noteBookRepository.findAllByUserId(userId).orElseGet(Collections::emptyList);
        return notebooks.stream()
                .map(notebooks1 -> modelMapper.map(notebooks1,NotebookDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public NotebookDTO updateNotebookById(Long notebookId, NotebookDTO request) {
        Notebook notebook = noteBookRepository.findById(notebookId)
                .orElseThrow(()-> new NotebookNotFoundException("notebook is not found with this notebookId"));
        updateNotebookData(notebook,request);
        Notebook savedNotebook = noteBookRepository.save(notebook);
        return modelMapper.map(savedNotebook,NotebookDTO.class);
    }

    private void updateNotebookData(Notebook notebook, NotebookDTO request) {
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
    public void deleteNotebookById(Long notebookId) {
        if(userRepository.existsById(notebookId))
        {
            userRepository.deleteById(notebookId);
        }
        throw new NotebookNotFoundException("notebook is not found with this notebookId");
    }
}
