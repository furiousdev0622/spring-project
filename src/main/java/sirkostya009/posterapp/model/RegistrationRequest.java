package sirkostya009.posterapp.model;

import lombok.Data;

public @Data class RegistrationRequest {
    private String email;
    private String username;
    private String password;
}
