package sirkostya009.posterapp.registration;

import lombok.Data;

public @Data class RegistrationRequest {
    private String email;
    private String username;
    private String password;
}
