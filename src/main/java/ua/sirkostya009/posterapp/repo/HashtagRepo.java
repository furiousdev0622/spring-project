package ua.sirkostya009.posterapp.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.sirkostya009.posterapp.dao.Hashtag;

import java.util.Optional;

public interface HashtagRepo extends JpaRepository<Hashtag, Long> {
    @Query("select h from Hashtag h where upper(h.tag) = upper(?1)")
    Optional<Hashtag> findByTagIgnoreCase(String tag);

    @Query("select h from Hashtag h order by h.mentions")
    Slice<Hashtag> findByMostMentions(Pageable pageable);
}
