package sirkostya009.posterapp.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
public @Data class PosterModel {
    private String text;
    private AppUserModel author;
    private boolean isLikedByYou;
    private int likes;
    private Long id;
    private Date postedAt,
                 lastEditAt;
}
