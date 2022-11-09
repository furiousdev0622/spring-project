package sirkostya009.posterapp.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import sirkostya009.posterapp.model.publicized.PosterModel;
import sirkostya009.posterapp.service.PosterService;
import sirkostya009.posterapp.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posters")
public class PosterApi {

    private final PosterService posterService;
    private final UserService userService;

    @PostMapping
    public PosterModel post(@RequestBody String posterText,
                            JwtAuthenticationToken token) {
        return posterService.save(posterText, userService.findByUsername(token.getName()));
    }

    @GetMapping
    public Page<PosterModel> all(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        return posterService.findAll(page);
    }

    @GetMapping("/popular")
    public Page<PosterModel> popular(@RequestParam(value = "page",  defaultValue = "0") Integer page) {
        return posterService.mostPopularPosters(page);
    }

    @GetMapping("/{id}")
    public PosterModel id(@PathVariable Long id) {
        return posterService.getPoster(id);
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
        return posterService.findAllByUser(userService.findByUsername(username), page);
    }

}
