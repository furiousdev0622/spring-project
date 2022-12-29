package ua.sirkostya009.posterapp.dto;

import lombok.Data;

/**
 * Sent by client upon registration
 */
public @Data class RegistrationRequest {
    private String email, username, password;
}
