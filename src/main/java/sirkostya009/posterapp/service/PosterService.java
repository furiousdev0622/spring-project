package sirkostya009.posterapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sirkostya009.posterapp.model.AppUser;
import sirkostya009.posterapp.model.Poster;
import sirkostya009.posterapp.model.PosterModel;
import sirkostya009.posterapp.model.UserInfo;
import sirkostya009.posterapp.repo.PosterRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PosterService {

    private final PosterRepo repo;
    private final static int postsPerSlice = 10;
    private final UserService userService;

    public PosterModel getPoster(Long id) {
        return posterToModel(repo.findById(id)
                .orElseThrow(() -> new RuntimeException("poster with " + id + " not found")));
    }

    public PosterModel save(String posterText, AppUser user) {
        return posterToModel(repo.save(new Poster(
                posterText,
                user.getUsername()
        )));
    }

    public Page<PosterModel> findAll(int page) {
        var result = repo.findAll(PageRequest.of(page, postsPerSlice));
        var contents = postersToModels(result.getContent());
        return new PageImpl<>(contents, result.getPageable(), result.getTotalElements());
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

    public List<PosterModel> findAllByUsername(String username) {
        return postersToModels(repo.findAllByAuthor(username), false);
    }

    private PosterModel posterToModel(Poster poster, boolean includeUserInfo) {
        var user = (AppUser) userService.loadUserByUsername(poster.getAuthor());
        return new PosterModel(
                poster.getText(),
                includeUserInfo ? UserInfo.fromAppUser(user) : null,
                poster.getLikes().contains(user),
                poster.getLikes().size(),
                poster.getId()
        );
    }

    private PosterModel posterToModel(Poster poster) {
        return posterToModel(poster, true);
    }

    private List<PosterModel> postersToModels(List<Poster> posters, boolean includeUserInfo) {
        return posters.stream().map(poster -> posterToModel(poster, includeUserInfo)).toList();
    }

    private List<PosterModel> postersToModels(List<Poster> posters) {
        return postersToModels(posters, true);
    }
}
