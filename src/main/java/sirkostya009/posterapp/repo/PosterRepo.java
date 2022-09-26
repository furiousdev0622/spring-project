package sirkostya009.posterapp.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sirkostya009.posterapp.model.Poster;

public interface PosterRepo extends JpaRepository<Poster, Long> {
    Page<Poster> findAllByAuthor(String author, Pageable pageable);
}
