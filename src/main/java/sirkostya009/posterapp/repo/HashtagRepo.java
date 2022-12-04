package sirkostya009.posterapp.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sirkostya009.posterapp.model.dao.Hashtag;

import java.util.Optional;

public interface HashtagRepo extends JpaRepository<Hashtag, Long> {
    @Query("select h from Hashtag h where upper(h.tag) = upper(?1)")
    Optional<Hashtag> findByTagIgnoreCase(String tag);

    @Query("select h from Hashtag h order by h.mentions")
    Page<Hashtag> findByMostMentions(Pageable pageable);
}
