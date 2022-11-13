package sirkostya009.posterapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import sirkostya009.posterapp.model.dao.Hashtag;

import java.util.Optional;

public interface HashtagRepo extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByTagIsIgnoreCase(String tag);
}
