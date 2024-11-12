package com.Study.inotebook.Service.ServiceImpl;

import com.Study.inotebook.Exceptions.UnauthorizedException;
import com.Study.inotebook.Payload.UpdateUserRequest;
import com.Study.inotebook.Entities.User;
import com.Study.inotebook.Exceptions.UserNotFoundException;
import com.Study.inotebook.Payload.UserResponse;
import com.Study.inotebook.Repository.UserRepository;
import com.Study.inotebook.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import io.micrometer.common.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("user not found with this userId "));
        return mapToUserResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("user not found with this userId"));
        updateUserData(user,request);
        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    private void updateUserData(User user, UpdateUserRequest request) {
         if(StringUtils.isNotBlank(request.getFirstName()))
         {
             user.setFirstName(request.getFirstName());
         }
        if(StringUtils.isNotBlank(request.getLastName()))
        {
            user.setLastName(request.getLastName());
        }
        if(StringUtils.isNotBlank(request.getEmail()))
        {
            user.setEmail(request.getEmail());
        }
        user.setIsActive(request.getIsActive());
    }

    @Override
    public void deleteUser(Long id, String requestingUsername) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with this user Id"));

        if (!user.getUsername().equals(requestingUsername)) {
            throw new UnauthorizedException("User not authorized to delete this user.");
        }
        userRepository.delete(user);
    }
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .role(user.getRole())
                .registerDate(user.getRegisterDate())
                .build();
    }
}
