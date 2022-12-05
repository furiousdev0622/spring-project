package sirkostya009.posterapp.model.common;

import lombok.Data;

public @Data class ChangeSettingsRequest {
    private String newEmail, newPassword, oldPassword;
}
