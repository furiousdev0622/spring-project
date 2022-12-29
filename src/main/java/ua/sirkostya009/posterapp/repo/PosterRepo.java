package ua.sirkostya009.posterapp.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.sirkostya009.posterapp.dao.AppUser;
import ua.sirkostya009.posterapp.dao.Poster;

public interface PosterRepo extends JpaRepository<Poster, Long> {
    @Query("select p from Poster p where p.author = ?1 order by p.postedAt desc")
    Slice<Poster> findAllByAuthor(AppUser author, Pageable pageable);

    @Query("select p from Poster p where upper(p.text) like upper(concat('%', ?1, '%')) order by size(p.likes), p.postedAt desc")
    Slice<Poster> findMostPopularWithTag(String hashtag, Pageable pageable);
}
