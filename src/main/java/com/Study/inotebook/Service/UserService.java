package com.Study.inotebook.Service;

import com.Study.inotebook.DTO.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO getUserById(Long id);

    List<UserDTO> getAllUsers();

    UserDTO updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);
}
