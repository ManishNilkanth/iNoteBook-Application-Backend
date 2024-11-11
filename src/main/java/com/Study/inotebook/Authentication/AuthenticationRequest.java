package com.Study.inotebook.Authentication;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthenticationRequest {

    @NotNull(message = "username is required ")
    private String username;

    @NotNull(message = "password is required")
    private String password;
}
