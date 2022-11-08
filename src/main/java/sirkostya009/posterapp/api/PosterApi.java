package sirkostya009.posterapp.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sirkostya009.posterapp.model.AppUser;
import sirkostya009.posterapp.model.PosterModel;
import sirkostya009.posterapp.service.PosterService;
import sirkostya009.posterapp.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posters")
public class PosterApi {

    private final PosterService posterService;
    private final UserService userService;

    @PostMapping
    public PosterModel post(@RequestBody String posterText, @AuthenticationPrincipal AppUser user) {
        return posterService.save(posterText, user);
    }

    @GetMapping
    public Page<PosterModel> all(@RequestParam(value = "page", defaultValue = "0") int page) {
        return posterService.findAll(page);
    }

    @GetMapping("/{id}")
    public PosterModel getById(@PathVariable Long id) {
        return posterService.getPoster(id);
    }

    @GetMapping("/like/{id}")
    public boolean like(@PathVariable long id, @AuthenticationPrincipal AppUser user) {
        return posterService.likePoster(id, user);
    }

    @GetMapping("/by/{username}")
    public Page<PosterModel> userPosters(@PathVariable String username, @RequestParam(value = "value", defaultValue = "0") int page) {
        return posterService.findAllByUser((AppUser) userService.loadUserByUsername(username), page);
    }

}
