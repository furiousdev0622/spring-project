package sirkostya009.posterapp.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import sirkostya009.posterapp.model.PosterModel;
import sirkostya009.posterapp.service.PosterService;

import java.util.List;

import static sirkostya009.posterapp.util.UserUtils.userFromToken;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posters")
public class PosterApi {

    private final PosterService service;

    @PostMapping
    public PosterModel post(@RequestBody String posterText, UsernamePasswordAuthenticationToken token) {
        return service.save(posterText, userFromToken(token));
    }

    @GetMapping
    @RequestMapping("/{id}")
    public PosterModel getById(@PathVariable Long id) {
        return service.getPoster(id);
    }

    @GetMapping
    public Page<PosterModel> all(@RequestParam(value = "page", defaultValue = "0") int page) {
        return service.findAll(page);
    }

    @GetMapping("/like/{id}")
    public boolean like(@PathVariable long id, UsernamePasswordAuthenticationToken token) {
        return service.likePoster(id, userFromToken(token));
    }

    @GetMapping("/by/{username}")
    public List<PosterModel> userPosters(@PathVariable String username) {
        return service.findAllByUsername(username);
    }

}
