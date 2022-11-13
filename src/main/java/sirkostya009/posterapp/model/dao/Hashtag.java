package sirkostya009.posterapp.model.dao;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String tag;
    private int mentions;

    public Hashtag(String word) {
        this.tag = word;
        this.mentions = 0;
    }

    public void incrementMentions() {
        mentions++;
    }

    public void decrementMentions() {
        mentions--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hashtag hashtag)) return false;
        return tag.equals(hashtag.tag) || id.equals(hashtag.id) || mentions == hashtag.mentions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tag, mentions);
    }
}
