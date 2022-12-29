package ua.sirkostya009.posterapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.sirkostya009.posterapp.dao.AppUser;

/**
 * A user model that is served to the client
 */
@AllArgsConstructor
public @Data class AppUserModel {
    private long id;
    private String username;
    private String photoPath;
    private String email;
    private String bio;
    private int following;
    private int followers;

    public static AppUserModel of(AppUser user, boolean showEmail) {
        return new AppUserModel(
                user.getId(),
                user.getUsername(),
                user.getProfilePictureFilename(),
                showEmail ? user.getEmail() : null,
                user.getBio(),
                user.getFollowing().size(),
                user.getFollowers().size()
        );
    }
}
