package sirkostya009.posterapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sirkostya009.posterapp.model.Poster;
import sirkostya009.posterapp.repo.PosterRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PosterService {

    private final PosterRepo repo;

    public Poster getPoster(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("poster with " + id + " not found"));
    }

    public Poster save(Poster poster) {
        return repo.save(poster);
    }

    public List<Poster> findAll() {
        return repo.findAll();
    }
}
