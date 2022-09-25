package sirkostya009.posterapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
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
    @Column(nullable = false)
    private String author;

    @ManyToMany
    @ToString.Exclude
    private final Set<AppUser> likes = Set.of();

    public Poster(String text, String author) {
        this.text = text;
        this.author = author;
    }
}
