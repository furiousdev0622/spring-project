package sirkostya009.posterapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data class PosterModel {
    private String text;
    private UserInfo author;
    private boolean isLikedByYou;
    private int likes;
    private Long id;
}
