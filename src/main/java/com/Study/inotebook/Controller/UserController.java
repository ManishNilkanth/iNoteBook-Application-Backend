package com.Study.inotebook.Controller;

import com.Study.inotebook.Payload.UpdateUserRequest;
import com.Study.inotebook.Payload.UserResponse;
import com.Study.inotebook.Service.UserService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "registerDate,desc") String[] sort
    ){
        Sort sortOrder = Sort.by(sort[0]).descending();
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        List<UserResponse> response =  userService.getAllUsers(pageable);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUserById(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        UserResponse response =  userService.updateUser(id, request);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id, Principal principal) {
        String requestingUsername = principal.getName();
        userService.deleteUser(id, requestingUsername);
        return ResponseEntity.accepted().build();
    }
}
