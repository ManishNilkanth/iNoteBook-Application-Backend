package com.Study.inotebook.Payload;

import com.Study.inotebook.Entities.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private Boolean isActive;

    private Role role;

    private LocalDateTime registerDate;
}
