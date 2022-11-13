package sirkostya009.posterapp.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import sirkostya009.posterapp.model.dao.Hashtag;
import sirkostya009.posterapp.model.dao.Poster;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public @Data class PosterModel {
    private String text;
    private AppUserModel author;
    private boolean isLikedByYou;
    private int likes;
    private Long id;
    private Date postedAt;
    private Date lastEditAt;
    private Set<String> hashtags;

    public static PosterModel of(Poster poster, boolean includeUserInfo) {
        return new PosterModel(
                poster.getText(),
                includeUserInfo ? AppUserModel.of(poster.getAuthor(), false) : null,
                poster.getLikes().contains(poster.getAuthor()),
                poster.getLikes().size(),
                poster.getId(),
                poster.getPostedAt(),
                poster.getLastEditedAt(),
                poster.getHashtags().stream().map(Hashtag::getTag).collect(Collectors.toSet())
        );
    }
}
