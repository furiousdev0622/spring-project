package sirkostya009.posterapp.model.common;

import lombok.Data;

/**
 * Sent by client upon registration
 */
public @Data class RegistrationRequest {
    private String email, username, password;
}
