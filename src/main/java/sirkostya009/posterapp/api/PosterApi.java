package sirkostya009.posterapp.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import sirkostya009.posterapp.model.common.PosterModel;
import sirkostya009.posterapp.model.dao.AppUser;
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
        var user = userService.findByUsername(token.getName());
        return PosterModel.of(posterService.save(posterText, user), user, false);
    }

    @GetMapping
    public Page<PosterModel> all(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                 JwtAuthenticationToken token) {
        return pageOfPostersToPosterModels(
                posterService.recommendation(page, userService.findByUsername(token.getName())),
                userService.findByUsername(token.getName()),
                true
        );
    }

    @GetMapping("/popular")
    public Page<PosterModel> popular(@RequestParam(name = "page",  defaultValue = "0") Integer page,
                                     JwtAuthenticationToken token) {
        return pageOfPostersToPosterModels(
                posterService.popular(page),
                userService.findByUsername(token.getName()),
                true
        );
    }

    @GetMapping("/{id}")
    public PosterModel id(@PathVariable Long id,
                          JwtAuthenticationToken token) {
        return PosterModel.of(posterService.getPoster(id), userService.findByUsername(token.getName()), true);
    }

    @GetMapping("/like/{id}")
    public boolean like(@PathVariable Long id,
                        JwtAuthenticationToken token) {
        return posterService.likePoster(id, userService.findByUsername(token.getName()));
    }

    @PostMapping("/edit/{id}")
    public PosterModel edit(@RequestBody String newText,
                            @PathVariable Long id,
                            JwtAuthenticationToken token) {
        var requester = userService.findByUsername(token.getName());
        return PosterModel.of(
                posterService.editPoster(newText, id, requester),
                requester,
                false
        );
    }

    @GetMapping("/by/{username}")
    public Page<PosterModel> userPosters(@PathVariable String username,
                                         @RequestParam(name = "value", defaultValue = "0") Integer page,
                                         JwtAuthenticationToken token) {
        return pageOfPostersToPosterModels(
                posterService.postersByUser(userService.findByUsername(username), page),
                userService.findByUsername(token.getName()),
                false
        );
    }

    private Page<PosterModel> pageOfPostersToPosterModels(Page<Poster> page, AppUser requester, boolean includeUserInfo) {
        return new PageImpl<>(
                listOfPostersToPosterModels(page.getContent(), requester, includeUserInfo),
                page.getPageable(),
                page.getTotalElements()
        );
    }

    private List<PosterModel> listOfPostersToPosterModels(List<Poster> posters, AppUser requester, boolean includeUserInfo) {
        return posters.stream().map(poster -> PosterModel.of(poster, requester, includeUserInfo)).toList();
    }

}
