package ua.sirkostya009.posterapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.sirkostya009.posterapp.dao.AppUser;
import ua.sirkostya009.posterapp.dao.Hashtag;
import ua.sirkostya009.posterapp.dao.Poster;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A poster model that is served to the client
 */
@AllArgsConstructor
public @Data class PosterModel {
    private String text;
    private AppUserModel author;
    private boolean isLikedByYou;
    private int likes;
    private Long id;
    private LocalDateTime postedAt;
    private LocalDateTime lastEditAt;
    private Set<String> hashtags;

    public static PosterModel of(Poster poster, AppUser requester, boolean includeUserInfo) {
        return new PosterModel(
                poster.getText(),
                includeUserInfo ? AppUserModel.of(poster.getAuthor(), false) : null,
                poster.getLikes().contains(requester),
                poster.getLikes().size(),
                poster.getId(),
                poster.getPostedAt(),
                poster.getLastEditedAt(),
                poster.getHashtags().stream().map(Hashtag::getTag).collect(Collectors.toSet())
        );
    }
}
