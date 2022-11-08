package sirkostya009.posterapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
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
    private final Set<AppUser> likes = Set.of();

    private Date postedAt;
    private Date lastEditedAt;

    public Poster(String text, AppUser author) {
        this.text = text;
        this.author = author;
        postedAt = new Date();
    }
}
