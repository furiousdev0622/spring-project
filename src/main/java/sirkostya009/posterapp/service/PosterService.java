package sirkostya009.posterapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sirkostya009.posterapp.model.privatized.AppUser;
import sirkostya009.posterapp.model.privatized.Poster;
import sirkostya009.posterapp.repo.PosterRepo;

@Service
@RequiredArgsConstructor
public class PosterService {

    private final PosterRepo repo;
    private final static int POSTS_PER_SLICE = 10;

    public Poster getPoster(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("poster with " + id + " not found"));
    }

    public Poster save(String posterText, AppUser user) {
        return repo.save(new Poster(
                posterText,
                user
        ));
    }

    public Page<Poster> findAll(int page) {
        return repo.findAll(pageRequest(page));
    }

    @Transactional
    public boolean likePoster(Long id, AppUser user) {
        var poster = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("poster doesnt not exist"));

        if (poster.getLikes().contains(user))
            poster.getLikes().remove(user);
        else poster.getLikes().add(user);

        return poster.getLikes().contains(user);
    }

    @Transactional
    public void editPoster(String newText, Long id, AppUser user) {
        var poster = getPoster(id);

        if (poster.getAuthor().getId() != user.getId())
            throw new IllegalStateException("poster ownership unfulfilled");

        poster.setText(newText);
    }

    public Page<Poster> findAllByUser(AppUser user, int pageNumber) {
        return repo.findAllByAuthor(user, pageRequest(pageNumber));
    }

    public Page<Poster> mostPopularPosters(Integer pageNumber) {
        return repo.findMostLikedPosters(pageRequest(pageNumber));
    }

    private PageRequest pageRequest(Integer page) {
        return PageRequest.of(page, POSTS_PER_SLICE);
    }

}
