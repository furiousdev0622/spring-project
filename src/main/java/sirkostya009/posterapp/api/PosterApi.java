package sirkostya009.posterapp.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import sirkostya009.posterapp.model.AppUser;
import sirkostya009.posterapp.model.Poster;
import sirkostya009.posterapp.model.PosterModel;
import sirkostya009.posterapp.service.PosterService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posters")
public class PosterApi {

    private final PosterService service;

    @PostMapping
    public Poster post(@RequestBody PosterModel poster, UsernamePasswordAuthenticationToken token) {
        return service.save(poster, (AppUser) token.getPrincipal());
    }

    @GetMapping
    @RequestMapping("/{id}")
    public Poster getById(@PathVariable Long id) {
        return service.getPoster(id);
    }

    @GetMapping
    public Page<Poster> all(@RequestParam(value = "page", defaultValue = "0") int page) {
        return service.findAll(page);
    }

}
