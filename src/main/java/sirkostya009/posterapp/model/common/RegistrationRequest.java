package sirkostya009.posterapp.model.common;

import lombok.Data;

public @Data class RegistrationRequest {
    private String email, username, password;
}
