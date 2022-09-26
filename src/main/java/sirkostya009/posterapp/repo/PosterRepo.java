package sirkostya009.posterapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import sirkostya009.posterapp.model.Poster;

import java.util.List;

public interface PosterRepo extends JpaRepository<Poster, Long> {
    List<Poster> findAllByAuthor(String author);
}
