package sirkostya009.posterapp.model.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Poster {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String text;
    @JoinColumn(nullable = false)
    @ManyToOne
    private AppUser author;

    @ManyToMany
    @ToString.Exclude
    private Set<AppUser> likes;

    @ManyToMany
    @ToString.Exclude
    private Set<Hashtag> hashtags;

    private Date postedAt;
    private Date lastEditedAt;

    public Poster(String text, AppUser author, Set<Hashtag> hashtags) {
        this.text = text;
        this.author = author;
        this.hashtags = hashtags;

        this.likes = Collections.emptySet();
        this.postedAt = new Date();
    }
}
