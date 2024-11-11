package com.Study.inotebook.Authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Setter
@Builder
public class AuthenticationResponse {
    @JsonProperty("access_token")
    String accessToken;
}
