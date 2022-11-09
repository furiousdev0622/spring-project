package sirkostya009.posterapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sirkostya009.posterapp.model.privatized.AppUser;
import sirkostya009.posterapp.model.privatized.Poster;
import sirkostya009.posterapp.model.publicized.PosterModel;
import sirkostya009.posterapp.model.publicized.UserInfo;
import sirkostya009.posterapp.repo.PosterRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PosterService {

    private final PosterRepo repo;
    private final static int POSTS_PER_SLICE = 10;

    public PosterModel getPoster(Long id) {
        return posterToModel(repo.findById(id)
                .orElseThrow(() -> new RuntimeException("poster with " + id + " not found")));
    }

    public PosterModel save(String posterText, AppUser user) {
        return posterToModel(repo.save(new Poster(
                posterText,
                user
        )));
    }

    public Page<PosterModel> findAll(int page) {
        return postersToModels(
                repo.findAll(pageRequest(page)),
                true
        );
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

    public Page<PosterModel> findAllByUser(AppUser user, int pageNumber) {
        return postersToModels(
                repo.findAllByAuthor(user, pageRequest(pageNumber)),
                false
        );
    }

    public Page<PosterModel> mostPopularPosters(Integer pageNumber) {
        return postersToModels(
                repo.findMostLikedPosters(pageRequest(pageNumber)),
                true
        );
    }

    private PageRequest pageRequest(Integer page) {
        return PageRequest.of(page, POSTS_PER_SLICE);
    }

    private Page<PosterModel> postersToModels(Page<Poster> page, boolean includeUserInfo) {
        return new PageImpl<>(postersToModels(page.getContent(), includeUserInfo), page.getPageable(), page.getTotalElements());
    }

    private PosterModel posterToModel(Poster poster, boolean includeUserInfo) {
        return new PosterModel(
                poster.getText(),
                includeUserInfo ? UserInfo.fromAppUser(poster.getAuthor()) : null,
                poster.getLikes().contains(poster.getAuthor()),
                poster.getLikes().size(),
                poster.getId(),
                poster.getPostedAt(),
                poster.getLastEditedAt()
        );
    }

    private PosterModel posterToModel(Poster poster) {
        return posterToModel(poster, true);
    }

    private List<PosterModel> postersToModels(List<Poster> posters, boolean includeUserInfo) {
        return posters.stream().map(poster -> posterToModel(poster, includeUserInfo)).toList();
    }

}
