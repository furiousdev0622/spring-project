package sirkostya009.posterapp.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class AppUserModel {
    private long id;
    private String username;
    private String photoPath;
    private String email;
    private String bio;
}
