package sirkostya009.posterapp.model.publicized;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sirkostya009.posterapp.model.privatized.AppUser;

@NoArgsConstructor
@AllArgsConstructor
public @Data class UserInfo {
    private long id;
    private String username;
    private String photoPath;
    private String email;
    private String bio;

    public static UserInfo fromAppUser(AppUser user, boolean showEmail) {
        return new UserInfo(
                user.getId(),
                user.getUsername(),
                user.getProfilePictureFilename(),
                showEmail ? user.getEmail() : null,
                user.getBio()
        );
    }

    public static UserInfo fromAppUser(AppUser user) {
        return fromAppUser(user, false);
    }
}
