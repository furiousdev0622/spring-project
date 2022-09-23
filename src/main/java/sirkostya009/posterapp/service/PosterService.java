package sirkostya009.posterapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sirkostya009.posterapp.model.AppUser;
import sirkostya009.posterapp.model.Poster;
import sirkostya009.posterapp.model.PosterModel;
import sirkostya009.posterapp.repo.PosterRepo;

@Service
@RequiredArgsConstructor
public class PosterService {

    private final PosterRepo repo;
    private final static int postsPerSlice = 10;

    public Poster getPoster(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("poster with " + id + " not found"));
    }

    public Poster save(PosterModel poster, AppUser user) {
        return repo.save(new Poster(
                poster.getContent(),
                user.getUsername()
        ));
    }

    public Page<Poster> findAll(int page) {
        return repo.findAll(PageRequest.of(page, postsPerSlice));
    }
}
