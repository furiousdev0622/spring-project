package sirkostya009.posterapp.model.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Poster implements Comparable<Poster> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String text;
    @JoinColumn(nullable = false)
    @ManyToOne
    private AppUser author;

    @ManyToMany(fetch = FetchType.EAGER) // i better find some other way to proxy no session exception
    @ToString.Exclude
    private Set<AppUser> likes;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Hashtag> hashtags;

    private Date postedAt;
    private Date lastEditedAt;

    public Poster(String text, AppUser author, Set<Hashtag> hashtags) {
        this.text = text;
        this.author = author;
        this.hashtags = hashtags;

        this.likes = new HashSet<>();
        this.postedAt = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Poster poster)) return false;
        return id.equals(poster.id) && text.equals(poster.text) && author.equals(poster.author) && postedAt.equals(poster.postedAt) && Objects.equals(lastEditedAt, poster.lastEditedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, author, postedAt, lastEditedAt);
    }

    @Override
    public int compareTo(@NotNull Poster o) {
        return postedAt.compareTo(o.postedAt);
    }
}
