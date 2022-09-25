package sirkostya009.posterapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data class UserInfo {
    private long id;
    private String username;

    public static UserInfo fromAppUser(AppUser user) {
        return new UserInfo(
                user.getId(),
                user.getUsername()
        );
    }
}
