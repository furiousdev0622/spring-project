package sirkostya009.posterapp.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sirkostya009.posterapp.model.dao.AppUser;
import sirkostya009.posterapp.model.dao.Poster;

public interface PosterRepo extends JpaRepository<Poster, Long> {
    @Query("select p from Poster p where p.author = ?1")
    Page<Poster> findAllByAuthor(AppUser author, Pageable pageable);

    @Query("select p from Poster p ORDER BY size(p.likes)")
    Page<Poster> findMostLikedPosters(Pageable pageable);
}
