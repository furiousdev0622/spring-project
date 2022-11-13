package sirkostya009.posterapp.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import sirkostya009.posterapp.model.common.PosterModel;
import sirkostya009.posterapp.model.dao.Poster;
import sirkostya009.posterapp.service.PosterService;
import sirkostya009.posterapp.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posters")
public class PosterApi {

    private final PosterService posterService;
    private final UserService userService;

    @PostMapping
    public PosterModel post(@RequestBody String posterText,
                            JwtAuthenticationToken token) {
        return PosterModel.of(posterService.save(posterText, userService.findByUsername(token.getName())), false);
    }

    @GetMapping
    public Page<PosterModel> all(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        return pageOfPostersToPosterModels(
                posterService.allPosters(page),
                true
        );
    }

    @GetMapping("/popular")
    public Page<PosterModel> popular(@RequestParam(value = "page",  defaultValue = "0") Integer page) {
        return pageOfPostersToPosterModels(
                posterService.mostPopularPosters(page),
                true
        );
    }

    @GetMapping("/{id}")
    public PosterModel id(@PathVariable Long id) {
        return PosterModel.of(posterService.getPoster(id), true);
    }

    @GetMapping("/like/{id}")
    public boolean like(@PathVariable Long id,
                        JwtAuthenticationToken token) {
        return posterService.likePoster(id, userService.findByUsername(token.getName()));
    }

    @PostMapping("/edit/{id}")
    public void edit(@RequestBody String newText,
                     @PathVariable Long id,
                     JwtAuthenticationToken token) {
        posterService.editPoster(newText, id, userService.findByUsername(token.getName()));
    }

    @GetMapping("/by/{username}")
    public Page<PosterModel> userPosters(@PathVariable String username,
                                         @RequestParam(value = "value", defaultValue = "0") Integer page) {
        return pageOfPostersToPosterModels(
                posterService.postersOfUser(userService.findByUsername(username), page),
                false
        );
    }

    private Page<PosterModel> pageOfPostersToPosterModels(Page<Poster> page, boolean includeUserInfo) {
        return new PageImpl<>(listOfPostersToPosterModels(page.getContent(), includeUserInfo), page.getPageable(), page.getTotalElements());
    }

    private List<PosterModel> listOfPostersToPosterModels(List<Poster> posters, boolean includeUserInfo) {
        return posters.stream().map(poster -> PosterModel.of(poster, includeUserInfo)).toList();
    }


}
