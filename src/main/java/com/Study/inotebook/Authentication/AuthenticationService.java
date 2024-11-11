package com.Study.inotebook.Authentication;

import com.Study.inotebook.DTO.UserDTO;
import com.Study.inotebook.Entities.User;
import com.Study.inotebook.Exceptions.UserNotFoundException;
import com.Study.inotebook.Repository.UserRepository;
import com.Study.inotebook.Security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    public AuthenticationResponse registerUser(UserDTO request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .isActive(request.getIsActive())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .lastLogin(LocalDateTime.now())
                .build();
        try{
            userRepository.save(user);
        }catch (DataIntegrityViolationException e)
        {
            throw new RuntimeException("user with this username is already exists. Try another one ");
        }
        String token = jwtService.generateToken(user);
        return AuthenticationResponse.builder().accessToken(token).build();
    }

    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {

         authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(
                         request.getUsername(),
                         request.getPassword()
                 )
         );

         User user = userRepository.findByUsername(request.getUsername())
                 .orElseThrow(()-> new UserNotFoundException("user not found with this username"));

         String token = jwtService.generateToken(user);
         return AuthenticationResponse.builder().accessToken(token).build();
    }
}
