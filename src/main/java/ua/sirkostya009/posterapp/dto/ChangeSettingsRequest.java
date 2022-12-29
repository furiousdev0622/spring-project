package ua.sirkostya009.posterapp.dto;

import lombok.Data;

/**
 * A POJO class sent from user with all user fields that may be changed
 */
public @Data class ChangeSettingsRequest {
    private String newEmail, newPassword, oldPassword, bio;
}
