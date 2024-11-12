package com.Study.inotebook.Authentication;

import com.Study.inotebook.Entities.Role;
import com.Study.inotebook.Entities.User;
import com.Study.inotebook.Exceptions.InvalidPasswordException;
import com.Study.inotebook.Exceptions.UserExistsWithUsernameException;
import com.Study.inotebook.Exceptions.UserNotFoundException;
import com.Study.inotebook.Payload.UserRequest;
import com.Study.inotebook.Repository.UserRepository;
import com.Study.inotebook.Security.JwtService;
import com.Study.inotebook.Service.ServiceImpl.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AuthenticationService extends JwtService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private static final Long EXPIRATION_TIME = (long) (15 * 60 * 1000);  // 15 minutes
    public AuthenticationResponse registerUser(UserRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .isActive(request.getIsActive())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .lastLogin(LocalDateTime.now())
                .build();
        try{
            userRepository.save(user);
        }catch (DataIntegrityViolationException e)
        {
            throw new UserExistsWithUsernameException("user with this username is already exists. Try another username ");
        }
        String token = jwtService.generateToken(user);
        return AuthenticationResponse.builder().accessToken(token).build();
    }

    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {

         try {
             authenticationManager.authenticate(
                     new UsernamePasswordAuthenticationToken(
                             request.getUsername(),
                             request.getPassword()
                     )
             );

             User user = userRepository.findByUsername(request.getUsername())
                     .orElseThrow(() -> new UserNotFoundException("user not found with this username"));
             user.setLastLogin(LocalDateTime.now());
             String token = jwtService.generateToken(user);
             return AuthenticationResponse.builder().accessToken(token).build();
         }catch (Exception e)
         {
             throw new InvalidPasswordException("Incorrect password.");
         }
    }

    public String forgotPassword(String email, String username) throws MessagingException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException("User is not found with userId"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        String token = buildToken(claims,user,EXPIRATION_TIME);
        String resetLink = "http://localhost:8080/api/v1/auth/reset-password?token=" + token;
        return emailService.sendPasswordResetEmail(email,resetLink);
    }

    public void updatePassword(String token, String newPassword) {
        Map<String, Object> claims = extractAllClaims(token);
        String username = claims.get("username").toString();
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException("user is not found During updating the password"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
