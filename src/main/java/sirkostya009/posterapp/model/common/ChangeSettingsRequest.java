package sirkostya009.posterapp.model.common;

import lombok.Data;

/**
 * A POJO class sent from user with all user fields that may be changed
 */
public @Data class ChangeSettingsRequest {
    private String newEmail, newPassword, oldPassword, bio;
}
