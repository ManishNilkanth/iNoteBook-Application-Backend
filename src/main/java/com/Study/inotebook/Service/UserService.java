package com.Study.inotebook.Service;

import com.Study.inotebook.Payload.UpdateUserRequest;
import com.Study.inotebook.Payload.UserResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers(Pageable pageable);

    UserResponse updateUser(Long id, UpdateUserRequest request);

    void deleteUser(Long id, String requestingUsername);
}
