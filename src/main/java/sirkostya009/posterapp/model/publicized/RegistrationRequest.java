package sirkostya009.posterapp.model.publicized;

import lombok.Data;

public @Data class RegistrationRequest {
    private String email, username, password;
}
